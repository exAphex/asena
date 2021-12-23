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

    _onSaveAddPass: function () {
      var obj = this.getView().getModel("mdlAddPass").getProperty("/");
      try {
        if (!obj.name || obj.name === "") {
          throw "Name has to be selected";
        }

        var writeObj = { id: 0, name: obj.name, description: obj.description, type: obj.selectedType, system: obj.selectedSystem == -1 ? null : { id: obj.selectedSystem } };
        this.createPass(writeObj);
      } catch (e) {
        this.messageBoxGenerator(e, false);
      }
    },

    createPass: function (obj) {
      var sQuery = "/api/v1/job/" + this.id + "/pass";
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.createDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
        .then(
          function () {
            this.genericDialog.close();
            this.messageBoxGenerator("Pass created!", true);
            this.loadJob(this.id);
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.showError(oError);
          }.bind(this)
        );
    },

    _onRefreshPass: function () {
      this.loadJob(this.id);
    },

    _onPassDelete: function (oEvent) {
      var rowItem = oEvent.getSource();
      var ctx = rowItem.getBindingContext();
      var p = ctx.getModel().getProperty(ctx.getPath());
      MessageBox.warning("Are you sure you want to delete the pass " + p.name + "?", {
        actions: [MessageBox.Action.OK, MessageBox.Action.CANCEL],
        emphasizedAction: MessageBox.Action.OK,
        onClose: function (sAction) {
          if (sAction == MessageBox.Action.OK) {
            var sQuery = "/api/v1/pass/" + p.id;
            var mParameters = {
              bShowBusyIndicator: true,
            };
            this.deleteWithJSONP(sQuery, mParameters)
              .then(
                function () {
                  this.messageBoxGenerator("Pass was deleted successfully!", true);
                  this.loadJob(this.id);
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

    _onMoveUpPass: function (oEvent) {
      var rowItem = oEvent.getSource();
      var ctx = rowItem.getBindingContext();
      var p = ctx.getModel().getProperty(ctx.getPath());
      var sQuery = "/api/v1/job/" + this.id + "/" + p.id + "/moveup";
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.createDataWithAjaxP(sQuery, JSON.stringify({}), mParameters)
        .then(
          function () {
            this.loadJob(this.id);
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.showError(oError);
          }.bind(this)
        );
    },

    _onMoveDownPass: function (oEvent) {
      var rowItem = oEvent.getSource();
      var ctx = rowItem.getBindingContext();
      var p = ctx.getModel().getProperty(ctx.getPath());
      var sQuery = "/api/v1/job/" + this.id + "/" + p.id + "/movedown";
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.createDataWithAjaxP(sQuery, JSON.stringify({}), mParameters)
        .then(
          function () {
            this.loadJob(this.id);
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.showError(oError);
          }.bind(this)
        );
    },
  });
});
