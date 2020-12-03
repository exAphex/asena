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
        }
    });
});
