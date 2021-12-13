sap.ui.define(["controller/core/BaseController", "sap/ui/core/routing/History", "sap/ui/model/json/JSONModel", "../../formatter/Formatter", "sap/ui/core/Fragment", "sap/ui/core/Popup"], function (Controller, History, JSONModel, Formatter, Fragment, Popup) {
  "use strict";
  return Controller.extend("com.asena.ui5.controller.core.Shell", {
    onInit: function () {
      jQuery.ajaxSetup({ cache: false }); // This will prevent all future AJAX calls from being cached!

      this.getView().setModel(sap.ui.getCore().getModel("mainModel"), "mainModel");

      this.loadVersion();
    },

    onPressHeaderUserItem: function (oEvent) {
      sap.ui.getCore().byId("actionSheet").openBy(oEvent.getSource());
    },

    // TODO this is not working correctly
    onPressLogOff: function () {},

    loadVersion: function () {
      var sQuery = "/api/v1/version";
      var mParameters = {
        bShowBusyIndicator: false,
      };
      this.loadJsonWithAjaxP(sQuery, mParameters)
        .then(
          function (oData) {
            var oMainModel = new JSONModel(oData);
            this.getView().setModel(oMainModel, "mdlVersion");
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.messageBoxGenerator("Status Code: " + oError.status + " \n Error Message: " + JSON.stringify(oError.responseJSON), false);
          }.bind(this)
        );
    },

    onBackPressed: function () {
      if (History.getInstance().getPreviousHash() !== undefined) {
        window.history.go(-1);
      } else {
        // nav to home screen if history is lost
        sap.ui.core.UIComponent.getRouterFor(this).navTo(
          "applications",
          {
            app: "applications",
          },
          false
        );
      }
    },

    onMenuPressed: function () {
      var tpMain = this.getView().byId("tpMain");
      tpMain.setSideExpanded(!tpMain.getSideExpanded());
    },

    _onSelectSystems: function () {
      sap.ui.core.UIComponent.getRouterFor(this).navTo(
        "systems",
        {
          app: "systems",
        },
        false
      );
    },

    _onSelectScripts: function () {
      sap.ui.core.UIComponent.getRouterFor(this).navTo(
        "scripts",
        {
          app: "scripts",
        },
        false
      );
    },

    _onSelectHome: function () {
      sap.ui.core.UIComponent.getRouterFor(this).navTo(
        "applications",
        {
          app: "applications",
        },
        false
      );
    },

    _onSelectLogs: function () {
      sap.ui.core.UIComponent.getRouterFor(this).navTo(
        "logs",
        {
          app: "logs",
        },
        false
      );
    },

    _onSelectSettings: function () {
      sap.ui.core.UIComponent.getRouterFor(this).navTo(
        "settings",
        {
          app: "settings",
        },
        false
      );
    },

    _onSelectJobs: function () {
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
