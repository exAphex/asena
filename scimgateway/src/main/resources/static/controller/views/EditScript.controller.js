sap.ui.define([
    "controller/core/BaseController",
    "sap/ui/model/json/JSONModel",
    "sap/m/MessageBox",
    "com/asena/ui5/formatter/Formatter"
], function (Controller, JSONModel, MessageBox, Formatter) {
    "use strict"; 
    return Controller.extend("com.asena.ui5.controller.views.EditScript", {
        formatter: Formatter,

        onInit: function () {
            var target = sap.ui.core.UIComponent.getRouterFor(this).getTarget("EditScript");
            target.attachDisplay(this._onDisplay, this);
            sap.ui.core.UIComponent.getRouterFor(this).getRoute("editscript").attachPatternMatched(this._onObjectMatched, this);
        },

        _onObjectMatched: function(oEvent) {
            this.id = oEvent.getParameter("arguments").id; 
            this.loadScripts(this.id);
    	},

        _onDisplay: function () {
            var mainModel = sap.ui.getCore().getModel("mainModel");
            mainModel.setProperty("/showNavButton", true);
        },

        _onSaveScript: function() {
            var mdl = this.getView().getModel();
            var obj = mdl.getProperty("/");
            this.updateScript(obj);
        },

        loadScripts: function(id) {
            var sQuery = "/api/v1/script/" + id;
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

        updateScript: function(obj) {
            var sQuery = "/api/v1/script/" + obj.id;
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.updateDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
                .then(function () {
                    this.messageBoxGenerator("Script saved!", true);
                    this.loadScripts(this.id);
                }.bind(this))
                .catch(function (oError) {
                    this.showError(oError); 
                }.bind(this));
        },
    });
});
