sap.ui.define([
    "controller/core/BaseController",
    "sap/ui/model/json/JSONModel",
    "com/asena/ui5/formatter/Formatter"
], function (Controller, JSONModel, Formatter) {
    "use strict"; 
    return Controller.extend("com.asena.ui5.controller.views.Systems", {
        formatter: Formatter,

        onInit: function () {
            var target = sap.ui.core.UIComponent.getRouterFor(this).getTarget("Systems");
            target.attachDisplay(this._onDisplay, this);
            sap.ui.core.UIComponent.getRouterFor(this).getRoute("systems").attachPatternMatched(this._onObjectMatched, this);
        },

        _onObjectMatched: function() {
            this.loadRemoteSystems();
    	},

        _onDisplay: function () {
            var mainModel = sap.ui.getCore().getModel("mainModel");
            mainModel.setProperty("/showNavButton", true);
        },
        
        _onAddSystem: function() {
            var mdl = new JSONModel({});
            this.getView().setModel(mdl, "mdlSystemDialog");

            this.loadTemplates();

            this.isEdit = false;
            this.loadFragment("SystemsDialog");
        },

        _onSaveDialog: function() {
            var mdl = this.getView().getModel("mdlSystemDialog");
            var systemObj = mdl.getProperty("/");

            this.createSystem(systemObj);
        },

        _onRefreshSystems: function() {
            this.loadRemoteSystems();
        },

        loadRemoteSystems: function() {
            var sQuery = "/api/v1/remotesystem";
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

        loadTemplates: function() {
            var sQuery = "/api/v1/remotesystem/templates";
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.loadJsonWithAjaxP(sQuery, mParameters)
                .then(function (oData) {
                    var oMainModel = new JSONModel(oData);
                    this.getView().setModel(oMainModel, "mdlTemplates");
                }.bind(this))
                .catch(function (oError) {
                    this.messageBoxGenerator("Status Code: "+oError.status+ " \n Error Message: "+ JSON.stringify(oError.responseJSON), false);
                }.bind(this));
        },

        createSystem: function(obj) {
            var sQuery = "/api/v1/remotesystem";
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.createDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
                .then(function () {
                    this.genericDialog.close();
                    this.messageBoxGenerator("System created!", true);
                    this.loadRemoteSystems();
                }.bind(this))
                .catch(function (oError) {
                    this.showError(oError); 
                }.bind(this));
        },
    });
});
