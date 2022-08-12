sap.ui.define(["controller/core/BaseController", "sap/ui/model/json/JSONModel", "com/asena/ui5/formatter/Formatter"], function (Controller, JSONModel, Formatter) {
  "use strict";
  return Controller.extend("com.asena.ui5.controller.views.Applications", {
    formatter: Formatter,

    onInit: function () {
      var target = sap.ui.core.UIComponent.getRouterFor(this).getTarget("Applications");
      target.attachDisplay(this._onDisplay, this);
      sap.ui.core.UIComponent.getRouterFor(this).getRoute("applications").attachPatternMatched(this._onObjectMatched, this);
    },

    _onObjectMatched: function () {
      this.loadRemoteSystems();
      this.loadScripts();
      this.loadLogs();
      this.loadJobs();
    },

    _onDisplay: function () {
      var mainModel = sap.ui.getCore().getModel("mainModel");
      mainModel.setProperty("/showNavButton", false);
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
            this.getView().setModel(oMainModel);
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.messageBoxGenerator("Status Code: " + oError.status + " \n Error Message: " + JSON.stringify(oError.responseJSON), false);
          }.bind(this)
        );
    },

    loadJobs: function () {
      var sQuery = "/api/v1/package";
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.loadJsonWithAjaxP(sQuery, mParameters)
        .then(
          function (oData) {
            var oMainModel = new JSONModel(oData);
            this.getView().setModel(oMainModel, "mdlJobs");
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.messageBoxGenerator("Status Code: " + oError.status + " \n Error Message: " + JSON.stringify(oError.responseJSON), false);
          }.bind(this)
        );
    },

    loadScripts: function () {
      var sQuery = "/api/v1/script";
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.loadJsonWithAjaxP(sQuery, mParameters)
        .then(
          function (oData) {
            var oMainModel = new JSONModel(oData);
            this.getView().setModel(oMainModel, "mdlScripts");
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.messageBoxGenerator("Status Code: " + oError.status + " \n Error Message: " + JSON.stringify(oError.responseJSON), false);
          }.bind(this)
        );
    },

    loadLogs: function () {
      var sQuery = "/api/v1/log/count";
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.loadJsonWithAjaxP(sQuery, mParameters)
        .then(
          function (oData) {
            var oMainModel = new JSONModel(oData);
            this.getView().setModel(oMainModel, "mdlLogs");
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.messageBoxGenerator("Status Code: " + oError.status + " \n Error Message: " + JSON.stringify(oError.responseJSON), false);
          }.bind(this)
        );
    },

    _onSystemsPressed: function (oEvent) {
      sap.ui.core.UIComponent.getRouterFor(this).navTo(
        "systems",
        {
          app: "systems",
        },
        false
      );
    },

    _onScriptsPressed: function (oEvent) {
      sap.ui.core.UIComponent.getRouterFor(this).navTo(
        "scripts",
        {
          app: "scripts",
        },
        false
      );
    },

    _onLogsPressed: function (oEvent) {
      sap.ui.core.UIComponent.getRouterFor(this).navTo(
        "logs",
        {
          app: "logs",
        },
        false
      );
    },

    _onJobsPressed: function () {
      sap.ui.core.UIComponent.getRouterFor(this).navTo(
        "jobs",
        {
          app: "jobs",
        },
        false
      );
    },
  });
});
