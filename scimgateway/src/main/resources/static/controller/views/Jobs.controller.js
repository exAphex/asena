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
      this.loadPackages();
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

    _onRefreshPackage: function () {
      this.loadPackages();
    },

    _onSaveAddPackage: function () {
      var mdl = this.getView().getModel("mdlAddPackage");
      var packageObj = mdl.getProperty("/");

      this.createPackage(packageObj);
    },

    _onSelectChangePackages: function (oEvent) {
      var lst = oEvent.getSource();
      if (lst.getSelectedItem()) {
        this.getView().byId("btnDeletePackage").setEnabled(true);
      }
    },

    _onDeletePackage: function (oEvent) {
      var list = this.getView().byId("lstPackages");
      var selItem = list.getSelectedItem();
      var ctx = selItem.getBindingContext();
      var p = ctx.getModel().getProperty(ctx.getPath());

      MessageBox.warning("Are you sure you want to delete the package " + p.name + "?", {
        actions: [MessageBox.Action.OK, MessageBox.Action.CANCEL],
        emphasizedAction: MessageBox.Action.OK,
        onClose: function (sAction) {
          if (sAction == MessageBox.Action.OK) {
            var sQuery = "/api/v1/package/" + p.id;
            var mParameters = {
              bShowBusyIndicator: true,
            };
            this.deleteWithJSONP(sQuery, mParameters)
              .then(
                function () {
                  this.messageBoxGenerator("Package was deleted successfully!", true);
                  this.loadPackages();
                }.bind(this)
              )
              .catch(
                function (oError) {
                  this.showError(oError);
                }.bind(this)
              );
          }
        }.bind(this),
      });
    },

    createPackage: function (obj) {
      var sQuery = "/api/v1/package";
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.createDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
        .then(
          function () {
            this.genericDialog.close();
            this.messageBoxGenerator("Package created!", true);
            this.loadPackages();
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.showError(oError);
          }.bind(this)
        );
    },

    loadPackages: function () {
      var sQuery = "/api/v1/package";
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.getView().byId("btnDeletePackage").setEnabled(false);

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
  });
});
