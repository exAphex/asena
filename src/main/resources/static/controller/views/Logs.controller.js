sap.ui.define([
    "controller/core/BaseController",
    "sap/ui/model/json/JSONModel",
    "sap/m/MessageBox",
    "com/asena/ui5/formatter/Formatter"
], function (Controller, JSONModel, MessageBox, Formatter) {
    "use strict"; 
    return Controller.extend("com.asena.ui5.controller.views.Logs", {
        formatter: Formatter,

        onInit: function () {
            var target = sap.ui.core.UIComponent.getRouterFor(this).getTarget("Scripts");
            target.attachDisplay(this._onDisplay, this);
            sap.ui.core.UIComponent.getRouterFor(this).getRoute("logs").attachPatternMatched(this._onObjectMatched, this);
        },

        _onObjectMatched: function() {
            //this.loadLogs();
    	},

        _onDisplay: function () {
            var mainModel = sap.ui.getCore().getModel("mainModel");
            mainModel.setProperty("/showNavButton", true);
        }
    });
});
