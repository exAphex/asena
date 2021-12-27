sap.ui.define(["controller/views/job/BasePassController", "sap/ui/model/json/JSONModel", "sap/m/MessageBox", "com/asena/ui5/formatter/Formatter"], function (Controller, JSONModel, MessageBox, Formatter) {
  "use strict";
  return Controller.extend("com.asena.ui5.controller.views.job.ReaderPass", {
    formatter: Formatter,

    onInit: function () {
      var target = sap.ui.core.UIComponent.getRouterFor(this).getTarget("ReaderPass");
      target.attachDisplay(this._onDisplay, this);
      sap.ui.core.UIComponent.getRouterFor(this).getRoute("readerpass").attachPatternMatched(this._onObjectMatched, this);
    },

    _onObjectMatched: function (oEvent) {
      this.lol();
    },

    _onDisplay: function () {
      var mainModel = sap.ui.getCore().getModel("mainModel");
      mainModel.setProperty("/showNavButton", true);
    },
  });
});