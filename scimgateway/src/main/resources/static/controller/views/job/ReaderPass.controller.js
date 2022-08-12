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
      this.id = oEvent.getParameter("arguments").id;
      this.loadPass(this.id);
    },

    _onDisplay: function () {
      var mainModel = sap.ui.getCore().getModel("mainModel");
      mainModel.setProperty("/showNavButton", true);
    },

    _onAddProperty: function () {
      this.openAddProperty();
    },

    _onSavePassProperty: function () {
      this.addPassProperty();
    },

    _onPassPropertyDelete: function (oEvent) {
      this.deletePassProperty(oEvent);
    },

    _onPassPropertyEdit: function (oEvent) {
      this.editPassProperty(oEvent);
    },

    _onAddPassMapping: function () {
      this.openAddMapping();
    },

    _onSavePassMapping: function () {
      this.addPassMapping();
    },

    _onPassMappingDelete: function (oEvent) {
      this.deletePassMapping(oEvent);
    },

    _onPassMappingEdit: function (oEvent) {
      this.editPassMapping(oEvent);
    },

    _onSavePass: function () {
      this.savePass();
    },
  });
});
