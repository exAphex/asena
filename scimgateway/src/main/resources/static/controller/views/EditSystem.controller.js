sap.ui.define([
    "controller/core/BaseController",
    "sap/ui/model/json/JSONModel",
    "com/asena/ui5/formatter/Formatter",
    "sap/m/MessageBox"
], function (Controller, JSONModel, Formatter, MessageBox) {
    "use strict"; 
    return Controller.extend("com.asena.ui5.controller.views.EditSystem", {
        formatter: Formatter,

        onInit: function () {
            var target = sap.ui.core.UIComponent.getRouterFor(this).getTarget("EditSystem");
            target.attachDisplay(this._onDisplay, this);
            sap.ui.core.UIComponent.getRouterFor(this).getRoute("editsystem").attachPatternMatched(this._onObjectMatched, this);
        },

        _onObjectMatched: function(oEvent) {
            this.id = oEvent.getParameter("arguments").id; 
            this.loadRemoteSystemSuggestions(this.id);
            this.loadRemoteSystem(this.id);
            this.setEndpointURL(this.id);
    	},

        _onDisplay: function () {
            var mainModel = sap.ui.getCore().getModel("mainModel");
            mainModel.setProperty("/showNavButton", true);
        },

        _onEntryTypeMappingEdit: function(oEvent) {
            var rowItem = oEvent.getSource();
            var ctx = rowItem.getBindingContext();
            var obj = ctx.getModel().getProperty(ctx.getPath());

            this.currentEntryType = obj.id;

            this.loadEntryTypeMapping(obj.id);
            this.loadFragment("EditEntryType", "editEntryType"); 
            
            console.log(obj);
        },

        _onAddWriteMapping: function() {
            var mdl = new JSONModel({transformation:{}});
            this.getView().setModel(mdl, "mdlAttributeDialog");

            this.isEdit = false;

            this.loadRemoteSystemSuggestions(this.id);
            this.loadScriptSuggestions();
            this.loadSCIMSuggestions();
            this.loadFragment("AttributeDialog");
        },

        _onAddReadMapping: function() {
            var mdl = new JSONModel({transformation:{}});
            this.getView().setModel(mdl, "mdlReadAttributeDialog");

            this.isEdit = false;

            this.loadRemoteSystemSuggestions(this.id);
            this.loadScriptSuggestions();
            this.loadSCIMSuggestions();
            this.loadFragment("ReadAttributeDialog");
        },

        _onAddConnectionProperty: function() {
            var mdl = new JSONModel({});
            this.getView().setModel(mdl, "mdlConnectionProperty");

            this.isEdit = false;
            this.loadRemoteSystemSuggestions(this.id);
            this.loadFragment("ConnectionPropertyDialog");
        },

        _onSaveConnectionProperty: function() {
            var mdl = this.getView().getModel("mdlConnectionProperty");
            var obj = mdl.getProperty("/");
            if (this.isEdit) {
                this.updateProperty(obj);
            } else {
                this.addConnectionProperty(this.id, obj);
            }
        },

        _onSaveAttribute: function() {
            var mdl = this.getView().getModel("mdlAttributeDialog");
            var obj = mdl.getProperty("/");
            if (this.isEdit) {
                this.updateMapping(obj);
            } else {
                this.addWriteMapping(this.currentEntryType, obj);
            }
        },

        _onSaveReadAttribute: function() {
            var mdl = this.getView().getModel("mdlReadAttributeDialog");
            var obj = mdl.getProperty("/");
            if (this.isEdit) {
                this.updateMapping(obj);
            } else {
                this.addReadMapping(this.currentEntryType, obj);
            }
        },

        _onWriteMappingEdit: function(oEvent) {
            var rowItem = oEvent.getSource();
            var ctx = rowItem.getBindingContext("mdlEntryTypeMapping");
            var obj = ctx.getModel().getProperty(ctx.getPath());

            var mdl = new JSONModel(obj);
            if (!obj.transformation) {
                mdl.setProperty("/transformation", {});
            }
            this.getView().setModel(mdl, "mdlAttributeDialog");

            this.isEdit = true;
            this.loadRemoteSystemSuggestions(this.id);
            this.loadSCIMSuggestions();
            this.loadScriptSuggestions();
            this.loadFragment("AttributeDialog");
        },

        _onReadMappingEdit: function(oEvent) {
            var rowItem = oEvent.getSource();
            var ctx = rowItem.getBindingContext("mdlEntryTypeMapping");
            var obj = ctx.getModel().getProperty(ctx.getPath());

            var mdl = new JSONModel(obj);
            if (!obj.transformation) {
                mdl.setProperty("/transformation", {});
            }
            this.getView().setModel(mdl, "mdlReadAttributeDialog");

            this.isEdit = true;
            this.loadRemoteSystemSuggestions(this.id);
            this.loadSCIMSuggestions();
            this.loadScriptSuggestions();
            this.loadFragment("ReadAttributeDialog");
        },

        _onConnectionPropertyEdit: function(oEvent) {
            var rowItem = oEvent.getSource();
            var ctx = rowItem.getBindingContext();
            var obj = ctx.getModel().getProperty(ctx.getPath());

            var mdl = new JSONModel(obj);
            this.getView().setModel(mdl, "mdlConnectionProperty");

            this.isEdit = true;
            this.loadRemoteSystemSuggestions(this.id);
            this.loadFragment("ConnectionPropertyDialog");
        },

        _onWriteMappingDelete: function(oEvent) {
            var rowItem = oEvent.getSource();
            var ctx = rowItem.getBindingContext("mdlEntryTypeMapping");
            var obj = ctx.getModel().getProperty(ctx.getPath());

            MessageBox.warning("Are you sure you want to delete the selected mapping?", {
				actions: [MessageBox.Action.OK, MessageBox.Action.CANCEL],
				emphasizedAction: MessageBox.Action.OK,
				onClose: function (sAction) {
                    if (sAction == MessageBox.Action.OK) {
                        var sQuery = "/api/v1/attribute/" + obj.id;
                        var mParameters = {
                            bShowBusyIndicator: true
                        };
                        this.deleteWithJSONP(sQuery, mParameters)
                            .then(function () {
                                this.messageBoxGenerator("Mapping was deleted successfully!", true);
                                this.loadEntryTypeMapping(this.currentEntryType);
                            }.bind(this))
                            .catch(function (oError) {
                                this.showError(oError); 
                            }.bind(this));
                    }
				}.bind(this)
			});
        },

        _onConnectionPropertyDelete: function(oEvent) {
            var rowItem = oEvent.getSource();
            var ctx = rowItem.getBindingContext();
            var obj = ctx.getModel().getProperty(ctx.getPath());

            MessageBox.warning("Are you sure you want to delete the selected property?", {
				actions: [MessageBox.Action.OK, MessageBox.Action.CANCEL],
				emphasizedAction: MessageBox.Action.OK,
				onClose: function (sAction) {
                    if (sAction == MessageBox.Action.OK) {
                        var sQuery = "/api/v1/property/" + obj.id;
                        var mParameters = {
                            bShowBusyIndicator: true
                        };
                        this.deleteWithJSONP(sQuery, mParameters)
                            .then(function () {
                                this.messageBoxGenerator("Property was deleted successfully!", true);
                                this.loadRemoteSystem(this.id);
                            }.bind(this))
                            .catch(function (oError) {
                                this.showError(oError); 
                            }.bind(this));
                    }
				}.bind(this)
			});
        },

        _onSaveSystem: function(oEvent) {
            var mdl = this.getView().getModel();
            var obj = mdl.getProperty("/");
            this.updateRemoteSystem(obj);
        },

        loadSCIMSuggestions: function() {
            var sQuery = "/model/suggestions.json";
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.loadJsonWithAjaxP(sQuery, mParameters)
                .then(function (oData) {
                    var oMainModel = new JSONModel(oData);
                    this.getView().setModel(oMainModel, "mdlSourceSuggestions");
                }.bind(this))
                .catch(function (oError) {
                    this.messageBoxGenerator("Status Code: "+oError.status+ " \n Error Message: "+ JSON.stringify(oError.responseJSON), false);
                }.bind(this));
        },

        loadScriptSuggestions: function() {
            var sQuery = "/api/v1/script";
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.loadJsonWithAjaxP(sQuery, mParameters)
                .then(function (oData) {
                    oData.push({id:null, name:""});
                    var oMainModel = new JSONModel(oData);
                    this.getView().setModel(oMainModel, "mdlScriptSuggestions");
                }.bind(this))
                .catch(function (oError) {
                    this.messageBoxGenerator("Status Code: "+oError.status+ " \n Error Message: "+ JSON.stringify(oError.responseJSON), false);
                }.bind(this));
        }, 

        loadRemoteSystemSuggestions: function(id) {
            var sQuery = "/api/v1/remotesystem/" + id + "/template";
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.loadJsonWithAjaxP(sQuery, mParameters)
                .then(function (oData) {
                    var oMainModel = new JSONModel(oData);
                    this.getView().setModel(oMainModel, "mdlTargetSuggestions");
                }.bind(this))
                .catch(function (oError) {
                    this.messageBoxGenerator("Status Code: "+oError.status+ " \n Error Message: "+ JSON.stringify(oError.responseJSON), false);
                }.bind(this));
        },

        loadRemoteSystem: function(id) {
            var sQuery = "/api/v1/remotesystem/" + id;
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.loadJsonWithAjaxP(sQuery, mParameters)
                .then(function (oData) {
                    var oMainModel = new JSONModel(oData);
                    this.getView().setModel(oMainModel);
                }.bind(this))
                .catch(function (oError) {
                    this.messageBoxGenerator("Status Code: "+oError.status+ " \n Error Message: "+ JSON.stringify(oError.responseJSON), false);
                }.bind(this));
        },

        loadEntryTypeMapping: function(id) {
            var sQuery = "/api/v1/entrytypemapping/" + id;
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.loadJsonWithAjaxP(sQuery, mParameters)
                .then(function (oData) {
                    var oMainModel = new JSONModel(oData);
                    this.getView().setModel(oMainModel, "mdlEntryTypeMapping");
                }.bind(this))
                .catch(function (oError) {
                    this.messageBoxGenerator("Status Code: "+oError.status+ " \n Error Message: "+ JSON.stringify(oError.responseJSON), false);
                }.bind(this));
        },

        addWriteMapping: function(id, obj) {
            var sQuery = "/api/v1/entrytypemapping/" + id + "/write";
            var mParameters = {
                bShowBusyIndicator: true
            };
            if ((obj.transformation) && ((!obj.transformation.id) || (!(obj.transformation.id >= 0)))) {
                obj.transformation = null;
            }
            this.createDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
                .then(function () {
                    this.genericDialog.close();
                    this.messageBoxGenerator("Write mapping added!", true);
                    this.loadEntryTypeMapping(this.currentEntryType);
                }.bind(this))
                .catch(function (oError) {
                    this.showError(oError); 
                }.bind(this));
        },

        addReadMapping: function(id, obj) {
            var sQuery = "/api/v1/entrytypemapping/" + id + "/read";
            var mParameters = {
                bShowBusyIndicator: true
            };
            if ((obj.transformation) && ((!obj.transformation.id) || (!(obj.transformation.id >= 0)))) {
                obj.transformation = null;
            }
            this.createDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
                .then(function () {
                    this.genericDialog.close();
                    this.messageBoxGenerator("Read mapping added!", true);
                    this.loadEntryTypeMapping(this.currentEntryType);
                }.bind(this))
                .catch(function (oError) {
                    this.showError(oError); 
                }.bind(this));
        },

        updateProperty: function(obj) {
            var sQuery = "/api/v1/property/" + obj.id;
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.updateDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
                .then(function () {
                    this.genericDialog.close();
                    this.messageBoxGenerator("Connection property saved!", true);
                    this.loadRemoteSystem(this.id);;
                }.bind(this))
                .catch(function (oError) {
                    this.showError(oError); 
                }.bind(this));
        },

        addConnectionProperty: function(id, obj) {
            var sQuery = "/api/v1/remotesystem/" + id + "/connection";
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.createDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
                .then(function () {
                    this.genericDialog.close();
                    this.messageBoxGenerator("Connection property added!", true);
                    this.loadRemoteSystem(this.id);
                }.bind(this))
                .catch(function (oError) {
                    this.showError(oError); 
                }.bind(this));
        },

        addEntryType: function(id, obj) {
            var sQuery = "/api/v1/remotesystem/" + id + "/entrytypemapping";
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.createDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
                .then(function () {
                    this.genericDialog.close();
                    this.messageBoxGenerator("Entry type added!", true);
                    this.loadRemoteSystem(this.id);
                }.bind(this))
                .catch(function (oError) {
                    this.showError(oError); 
                }.bind(this));
        },

        updateMapping: function(obj) {
            var sQuery = "/api/v1/attribute/" + obj.id;
            var mParameters = {
                bShowBusyIndicator: true
            };
            if ((obj.transformation) && ((!obj.transformation.id) || (!(obj.transformation.id >= 0)))) {
                obj.transformation = null;
            }
            this.updateDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
                .then(function () {
                    this.genericDialog.close();
                    this.messageBoxGenerator("Mapping saved!", true);
                    this.loadEntryTypeMapping(this.currentEntryType);
                }.bind(this))
                .catch(function (oError) {
                    this.showError(oError); 
                }.bind(this));
        },

        updateRemoteSystem: function(obj) {
            var sQuery = "/api/v1/remotesystem/" + this.id;
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.updateDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
                .then(function () {
                    this.messageBoxGenerator("Remote system saved!", true);
                    this.loadRemoteSystem(this.id);;
                }.bind(this))
                .catch(function (oError) {
                    this.showError(oError); 
                }.bind(this));
        },

        _onSearchConnectionProperty: function(oEvent) {
            var sQuery = oEvent.getParameter("query");

            var oFilter = this.getConnectionPropertyFilters(sQuery);
            var oTable = this.getView().byId("tblConnProperties");
            oTable.getBinding("rows").filter(oFilter, "Application");
        },

        _onSearchEntryTypeMapping: function(oEvent) {
            var sQuery = oEvent.getParameter("query");
            var oFilter = this.getEntryTypeMappingFilters(sQuery);
            var oTable = this.getView().byId("tblEntryType");
            oTable.getBinding("rows").filter(oFilter, "Application");
        },

        _onSearchWriteMappings: function(oEvent) {
            var sQuery = oEvent.getParameter("query");

            var oFilter = this.getWriteMappingFilters(sQuery);
            var oTable = sap.ui.getCore().byId("tblWriteMapping");
            oTable.getBinding("rows").filter(oFilter, "Application");
        },

        _onSearchReadMappings: function(oEvent) {
            var sQuery = oEvent.getParameter("query");

            var oFilter = this.getWriteMappingFilters(sQuery);
            var oTable = sap.ui.getCore().byId("tblReadMapping");
            oTable.getBinding("rows").filter(oFilter, "Application");
        },

        _onAddEntryTypeMapping: function() {
            var mdl = new JSONModel({});
            this.getView().setModel(mdl, "mdlAddEntryType");

            this.isEdit = false;
            this.loadRemoteSystemSuggestions(this.id);
            this.loadFragment("AddEntryType");
        },

        _onEntryTypeMappingDelete: function(oEvent) {
            var rowItem = oEvent.getSource();
            var ctx = rowItem.getBindingContext();
            var obj = ctx.getModel().getProperty(ctx.getPath());

            MessageBox.warning("Are you sure you want to delete the selected entry type?", {
				actions: [MessageBox.Action.OK, MessageBox.Action.CANCEL],
				emphasizedAction: MessageBox.Action.OK,
				onClose: function (sAction) {
                    if (sAction == MessageBox.Action.OK) {
                        var sQuery = "/api/v1/entrytypemapping/" + obj.id;
                        var mParameters = {
                            bShowBusyIndicator: true
                        };
                        this.deleteWithJSONP(sQuery, mParameters)
                            .then(function () {
                                this.messageBoxGenerator("Mapping was deleted successfully!", true);
                                this.loadRemoteSystem(this.id);
                            }.bind(this))
                            .catch(function (oError) {
                                this.showError(oError); 
                            }.bind(this));
                    }
				}.bind(this)
			});
        },

        _onSaveEntryType: function() {
            var mdl = this.getView().getModel("mdlAddEntryType");
            var obj = mdl.getProperty("/");
            this.addEntryType(this.id, obj); 
        },

        _onCloseEditEntryTypeDialog: function() {
            this.closeFragment("editEntryType");
        },

        setEndpointURL: function(id) {
            var loc = window.location;
            var endpoint = loc.protocol + "//" + loc.host + "/gateway/" + id + "/scim/v2/";
            this.getView().byId("inptEndpoint").setValue(endpoint);
        }
    });
});
