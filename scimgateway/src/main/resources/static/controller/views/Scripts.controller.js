sap.ui.define([
    "controller/core/BaseController",
    "sap/ui/model/json/JSONModel",
    "sap/m/MessageBox",
    "com/asena/ui5/formatter/Formatter"
], function (Controller, JSONModel, MessageBox, Formatter) {
    "use strict"; 
    return Controller.extend("com.asena.ui5.controller.views.Scripts", {
        formatter: Formatter,

        onInit: function () {
            var target = sap.ui.core.UIComponent.getRouterFor(this).getTarget("Scripts");
            target.attachDisplay(this._onDisplay, this);
            sap.ui.core.UIComponent.getRouterFor(this).getRoute("scripts").attachPatternMatched(this._onObjectMatched, this);
        },

        _onObjectMatched: function() {
            this.loadScripts();
    	},

        _onDisplay: function () {
            var mainModel = sap.ui.getCore().getModel("mainModel");
            mainModel.setProperty("/showNavButton", true);
        },

        _onAddScript: function() {
            var mdl = new JSONModel({});
            this.getView().setModel(mdl, "mdlScriptDialog");

            this.loadFragment("ScriptDialog");
        },

        _onSaveScript: function() {
            var mdl = this.getView().getModel("mdlScriptDialog");
            var scriptObj = mdl.getProperty("/");
            scriptObj.content = "/*\n * Author: \n * Description: \n * Created on: " + new Date().toISOString().slice(0, 10) + "\n*/\n\nfunction " + scriptObj.name + "(param) {\n\treturn \"\";\n}";
            this.createScript(scriptObj);
        },

        _onRefreshScript: function() {
            this.loadScripts();
        },

        _onScriptEdit: function(oEvent) {
            var rowItem = oEvent.getSource();
            var ctx = rowItem.getBindingContext();
            var script = ctx.getModel().getProperty(ctx.getPath());

            sap.ui.core.UIComponent.getRouterFor(this).navTo("editscript", {
                app: "editscript",
                id: script.id
            }, false)
        },

        _onScriptDelete: function(oEvent) {
            var rowItem = oEvent.getSource();
            var ctx = rowItem.getBindingContext();
            var script = ctx.getModel().getProperty(ctx.getPath());

            MessageBox.warning("Are you sure you want to delete the script " + script.name + "?", {
				actions: [MessageBox.Action.OK, MessageBox.Action.CANCEL],
				emphasizedAction: MessageBox.Action.OK,
				onClose: function (sAction) {
                    if (sAction == MessageBox.Action.OK) {
                        var sQuery = "/api/v1/script/" + script.id;
                        var mParameters = {
                            bShowBusyIndicator: true
                        };
                        this.deleteWithJSONP(sQuery, mParameters)
                            .then(function () {
                                this.messageBoxGenerator("Script was deleted successfully!", true);
                                this.loadScripts();
                            }.bind(this))
                            .catch(function (oError) {
                                this.showError(oError); 
                            }.bind(this));
                    }
				}.bind(this)
			});
        },

        loadScripts: function() {
            var sQuery = "/api/v1/script";
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

        createScript: function(obj) {
            var sQuery = "/api/v1/script";
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.createDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
                .then(function () {
                    this.genericDialog.close();
                    this.messageBoxGenerator("Script created!", true);
                    this.loadScripts();
                }.bind(this))
                .catch(function (oError) {
                    this.showError(oError); 
                }.bind(this));
        },

        _onSearchScripts: function(oEvent) {
            var sQuery = oEvent.getParameter("query");

            var oFilter = this.getScriptFilters(sQuery);
            var oTable = this.getView().byId("tblScripts");
            oTable.getBinding("rows").filter(oFilter, "Application");
        }
    });
});
