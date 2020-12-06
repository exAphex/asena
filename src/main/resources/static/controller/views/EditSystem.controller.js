sap.ui.define([
    "controller/core/BaseController",
    "sap/ui/model/json/JSONModel",
    "com/asena/ui5/formatter/Formatter"
], function (Controller, JSONModel, Formatter) {
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
            this.isWriteMapping = true;

            this.loadRemoteSystemSuggestions(this.id);
            this.loadSCIMSuggestions();
            this.loadFragment("AttributeDialog");
        },

        _onSaveAttribute: function() {
            var mdl = this.getView().getModel("mdlAttributeDialog");
            var obj = mdl.getProperty("/");
            this.addWriteMapping(this.id, obj);
        },

        _onChangeSourceAttribute: function(oEvent) {
            var val = oEvent.getParameter("value");
            var mdl = this.getView().getModel("mdlAttributeDialog");
            mdl.setProperty("/source", val);
        },

        _onChangeDestinationAttribute: function(oEvent) {
            var val = oEvent.getParameter("value");
            var mdl = this.getView().getModel("mdlAttributeDialog");
            mdl.setProperty("/destination", val);
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
    });
});
