sap.ui.define([
    "controller/core/BaseController",
    "sap/ui/model/json/JSONModel",
    "com/asena/ui5/formatter/Formatter"
], function (Controller, JSONModel, Formatter) {
    "use strict"; 
    return Controller.extend("com.asena.ui5.controller.views.Applications", {
        formatter: Formatter,

        onInit: function () {
            var target = sap.ui.core.UIComponent.getRouterFor(this).getTarget("Applications");
            target.attachDisplay(this._onDisplay, this);
            sap.ui.core.UIComponent.getRouterFor(this).getRoute("applications").attachPatternMatched(this._onObjectMatched, this);
        },

        _onObjectMatched: function() {
            this.loadRemoteSystems();
    	},

        _onDisplay: function () {
            var mainModel = sap.ui.getCore().getModel("mainModel");
            mainModel.setProperty("/showNavButton", false);
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

        _onSystemsPressed: function(oEvent) {
            sap.ui.core.UIComponent.getRouterFor(this).navTo("systems", {
                    app: "systems"
            }, false);
        }

    });
});
