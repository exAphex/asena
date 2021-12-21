sap.ui.define(["controller/core/BaseController", "sap/ui/model/json/JSONModel", "sap/m/MessageBox", "com/asena/ui5/formatter/Formatter"], function (Controller, JSONModel, MessageBox, Formatter) {
  "use strict";
  return Controller.extend("com.asena.ui5.controller.views.JobDetail", {
    formatter: Formatter,

    onInit: function () {
      var target = sap.ui.core.UIComponent.getRouterFor(this).getTarget("JobDetail");
      target.attachDisplay(this._onDisplay, this);
      sap.ui.core.UIComponent.getRouterFor(this).getRoute("jobdetail").attachPatternMatched(this._onObjectMatched, this);
    },

    _onObjectMatched: function (oEvent) {
      this.id = oEvent.getParameter("arguments").id;
      this.loadJob(this.id);
      this.createAddPassModel();
    },

    _onDisplay: function () {
      var mainModel = sap.ui.getCore().getModel("mainModel");
      mainModel.setProperty("/showNavButton", true);
    },

    loadJob: function (id) {
      var sQuery = "/api/v1/job/" + id;
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.loadJsonWithAjaxP(sQuery, mParameters)
        .then(
          function (oData) {
            var oMainModel = new JSONModel(oData);
            this.getView().setModel(oMainModel);
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.messageBoxGenerator("Status Code: " + oError.status + " \n Error Message: " + JSON.stringify(oError.responseJSON), false);
          }.bind(this)
        );
    },

    loadRemoteSystems: function () {
      var sQuery = "/api/v1/remotesystem";
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.loadJsonWithAjaxP(sQuery, mParameters)
        .then(
          function (oData) {
            var oMainModel = new JSONModel(oData);
            this.getView().setModel(oMainModel, "mdlSystems");
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.messageBoxGenerator("Status Code: " + oError.status + " \n Error Message: " + JSON.stringify(oError.responseJSON), false);
          }.bind(this)
        );
    },

    createAddPassModel: function () {
      var obj = [
        { name: "Process", type: "PROCESS" },
        { name: "Reader", type: "READ" },
        { name: "Writer", type: "WRITE" },
      ];
      var mdl = new JSONModel(obj);
      this.getView().setModel(mdl, "mdlTypes");
    },

    _onAddPass: function () {
      var mdl = new JSONModel({ systemVisible: false, selectedType: "PROCESS", name: "", description: "", selectedSystem: -1 });
      this.getView().setModel(mdl, "mdlAddPass");

      this.loadRemoteSystems();
      this.loadFragment("AddPass");
    },

    _onAddPassTypeChange: function (oEvent) {
      var selItem = oEvent.getParameter("selectedItem");
      var key = selItem.getKey();
      var systemVisible = false;
      switch (key) {
        case "READ":
          systemVisible = true;
          break;
        case "WRITE":
          systemVisible = true;
          break;
      }
      this.getView().getModel("mdlAddPass").setProperty("/systemVisible", systemVisible);
    },
  });
});
