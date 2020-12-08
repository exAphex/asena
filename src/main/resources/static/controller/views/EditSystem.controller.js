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
            this.loadRemoteSystem(this.id);
    	},

        _onDisplay: function () {
            var mainModel = sap.ui.getCore().getModel("mainModel");
            mainModel.setProperty("/showNavButton", true);
        },

        _onAddWriteMapping: function() {
            var mdl = new JSONModel({});
            this.getView().setModel(mdl, "mdlAttributeDialog");

            this.isEdit = false;

            this.loadRemoteSystemSuggestions(this.id);
            this.loadScriptSuggestions();
            this.loadSCIMSuggestions();
            this.loadFragment("AttributeDialog");
        },

        _onSaveAttribute: function() {
            var mdl = this.getView().getModel("mdlAttributeDialog");
            var obj = mdl.getProperty("/");
            if (this.isEdit) {
                this.updateMapping(obj);
            } else {
                this.addWriteMapping(this.id, obj);
            }
        },

        _onWriteMappingEdit: function(oEvent) {
            var rowItem = oEvent.getSource();
            var ctx = rowItem.getBindingContext();
            var obj = ctx.getModel().getProperty(ctx.getPath());

            var mdl = new JSONModel(obj);
            this.getView().setModel(mdl, "mdlAttributeDialog");

            this.isEdit = true;
            this.loadRemoteSystemSuggestions(this.id);
            this.loadSCIMSuggestions();
            this.loadScriptSuggestions();
            this.loadFragment("AttributeDialog");
        },

        _onWriteMappingDelete: function(oEvent) {
            var rowItem = oEvent.getSource();
            var ctx = rowItem.getBindingContext();
            var obj = ctx.getModel().getProperty(ctx.getPath());

            MessageBox.warning("Are you sure you want to delete the selected write mapping?", {
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
                                this.loadRemoteSystem(this.id);
                            }.bind(this))
                            .catch(function (oError) {
                                this.showError(oError); 
                            }.bind(this));
                    }
				}.bind(this)
			});
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

        addWriteMapping: function(id, obj) {
            var sQuery = "/api/v1/remotesystem/" + id + "/write";
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.createDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
                .then(function () {
                    this.genericDialog.close();
                    this.messageBoxGenerator("Write mapping added!", true);
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
            this.updateDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
                .then(function () {
                    this.genericDialog.close();
                    this.messageBoxGenerator("Mapping saved!", true);
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

        _onSearchWriteMappings: function(oEvent) {
            var sQuery = oEvent.getParameter("query");

            var oFilter = this.getWriteMappingFilters(sQuery);
            var oTable = this.getView().byId("tblWriteMapping");
            oTable.getBinding("rows").filter(oFilter, "Application");
        }
    });
});
