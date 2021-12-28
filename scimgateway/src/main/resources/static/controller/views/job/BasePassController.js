sap.ui.define(["controller/core/BaseController", "sap/ui/model/json/JSONModel", "sap/m/MessageBox", "com/asena/ui5/formatter/Formatter"], function (Controller, JSONModel, MessageBox, Formatter) {
  "use strict";
  return Controller.extend("com.asena.ui5.controller.views.job.BasePassController", {
    loadPass: function (id) {
      var sQuery = "/api/v1/pass/" + id;
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

    openAddProperty: function () {
      var mdl = new JSONModel({});
      this.getView().setModel(mdl, "mdlAddProperty");
      this.loadFragment("AddPassProperty");
    },

    addPassProperty: function () {
      var obj = this.getView().getModel("mdlAddProperty").getProperty("/");
      try {
        if (!obj.key || obj.key === "") {
          throw "Name has to be selected";
        }

        var writeObj = { id: 0, key: obj.key, description: obj.description, value: obj.value };
        this.createPassProperty(writeObj);
      } catch (e) {
        this.messageBoxGenerator(e, false);
      }
    },

    createPassProperty: function (obj) {
      var sQuery = "/api/v1/pass/" + this.id + "/property";
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.createDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
        .then(
          function () {
            this.genericDialog.close();
            this.messageBoxGenerator("Property added!", true);
            this.loadPass(this.id);
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.showError(oError);
          }.bind(this)
        );
    },

    deletePassProperty: function (oEvent) {
      var rowItem = oEvent.getSource();
      var ctx = rowItem.getBindingContext();
      var p = ctx.getModel().getProperty(ctx.getPath());
      MessageBox.warning("Are you sure you want to delete the property " + p.key + "?", {
        actions: [MessageBox.Action.OK, MessageBox.Action.CANCEL],
        emphasizedAction: MessageBox.Action.OK,
        onClose: function (sAction) {
          if (sAction == MessageBox.Action.OK) {
            var sQuery = "/api/v1/passproperty/" + p.id;
            var mParameters = {
              bShowBusyIndicator: true,
            };
            this.deleteWithJSONP(sQuery, mParameters)
              .then(
                function () {
                  this.messageBoxGenerator("Property was deleted successfully!", true);
                  this.loadPass(this.id);
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
  });
});
