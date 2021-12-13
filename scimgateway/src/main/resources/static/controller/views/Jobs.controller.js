sap.ui.define(["controller/core/BaseController", "sap/ui/model/json/JSONModel", "sap/m/MessageBox", "com/asena/ui5/formatter/Formatter"], function (Controller, JSONModel, MessageBox, Formatter) {
  "use strict";
  return Controller.extend("com.asena.ui5.controller.views.Jobs", {
    formatter: Formatter,

    onInit: function () {
      var target = sap.ui.core.UIComponent.getRouterFor(this).getTarget("Jobs");
      target.attachDisplay(this._onDisplay, this);
      sap.ui.core.UIComponent.getRouterFor(this).getRoute("jobs").attachPatternMatched(this._onObjectMatched, this);
    },

    _onObjectMatched: function () {
      var data = new JSONModel({ packages: [{ name: "com.ibsolution.ad" }] });
      this.getView().setModel(data);
    },

    _onDisplay: function () {
      var mainModel = sap.ui.getCore().getModel("mainModel");
      mainModel.setProperty("/showNavButton", true);
    },

    _onAddPackage: function () {
      var mdl = new JSONModel({});
      this.getView().setModel(mdl, "mdlAddPackage");

      this.loadFragment("AddPackage");
    },
  });
});
