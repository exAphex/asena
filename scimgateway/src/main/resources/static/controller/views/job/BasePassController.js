sap.ui.define(["controller/core/BaseController", "sap/ui/model/json/JSONModel", "sap/m/MessageBox", "com/asena/ui5/formatter/Formatter"], function (Controller, JSONModel, MessageBox, Formatter) {
  "use strict";
  return Controller.extend("com.asena.ui5.controller.views.job.BasePassController", {
    isEdit: false,
    passPropertyId: 0,
    passMappingId: 0,

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

    loadPassProperty: function (id) {
      var sQuery = "/api/v1/passproperty/" + id;
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.loadJsonWithAjaxP(sQuery, mParameters)
        .then(
          function (oData) {
            var oMainModel = new JSONModel(oData);
            this.getView().setModel(oMainModel, "mdlAddProperty");
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.messageBoxGenerator("Status Code: " + oError.status + " \n Error Message: " + JSON.stringify(oError.responseJSON), false);
          }.bind(this)
        );
    },

    loadPassMapping: function (id) {
      var sQuery = "/api/v1/passmapping/" + id;
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.loadJsonWithAjaxP(sQuery, mParameters)
        .then(
          function (oData) {
            var oMainModel = new JSONModel(oData);
            this.getView().setModel(oMainModel, "mdlAddMapping");
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.messageBoxGenerator("Status Code: " + oError.status + " \n Error Message: " + JSON.stringify(oError.responseJSON), false);
          }.bind(this)
        );
    },

    openAddProperty: function () {
      this.isEdit = false;
      var mdl = new JSONModel({});
      this.getView().setModel(mdl, "mdlAddProperty");
      this.loadFragment("AddPassProperty");
    },

    openAddMapping: function () {
      this.isEdit = false;
      var mdl = new JSONModel({});
      this.getView().setModel(mdl, "mdlAddMapping");
      this.loadFragment("AddPassMapping");
    },

    addPassProperty: function () {
      var obj = this.getView().getModel("mdlAddProperty").getProperty("/");
      try {
        if (!obj.key || obj.key === "") {
          throw "Name has to be selected";
        }

        if (!this.isEdit) {
          var writeObj = { id: 0, key: obj.key, description: obj.description, value: obj.value };
          this.createPassProperty(writeObj);
        } else {
          var writeObj = { id: 0, key: obj.key, description: obj.description, value: obj.value };
          this.modifyPassProperty(this.passPropertyId, writeObj);
        }
      } catch (e) {
        this.messageBoxGenerator(e, false);
      }
    },

    addPassMapping: function () {
      var obj = this.getView().getModel("mdlAddMapping").getProperty("/");
      try {
        if (!obj.source || obj.source === "") {
          throw "Source has to be selected";
        }

        if (!obj.destination || obj.destination === "") {
          throw "Destination has to be selected";
        }

        var writeObj = { id: 0, source: obj.source, destination: obj.destination };
        if (!this.isEdit) {
          this.createPassMapping(writeObj);
        } else {
          this.modifyPassMapping(this.passMappingId, writeObj);
        }
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

    createPassMapping: function (obj) {
      var sQuery = "/api/v1/pass/" + this.id + "/mapping";
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.createDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
        .then(
          function () {
            this.genericDialog.close();
            this.messageBoxGenerator("Mapping added!", true);
            this.loadPass(this.id);
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.showError(oError);
          }.bind(this)
        );
    },

    modifyPassProperty: function (id, obj) {
      var sQuery = "/api/v1/passproperty/" + id;
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.updateDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
        .then(
          function () {
            this.genericDialog.close();
            this.messageBoxGenerator("Property saved!", true);
            this.loadPass(this.id);
          }.bind(this)
        )
        .catch(
          function (oError) {
            this.showError(oError);
          }.bind(this)
        );
    },

    modifyPassMapping: function (id, obj) {
      var sQuery = "/api/v1/passmapping/" + id;
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.updateDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
        .then(
          function () {
            this.genericDialog.close();
            this.messageBoxGenerator("Mapping saved!", true);
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

    deletePassMapping: function (oEvent) {
      var rowItem = oEvent.getSource();
      var ctx = rowItem.getBindingContext();
      var p = ctx.getModel().getProperty(ctx.getPath());
      MessageBox.warning("Are you sure you want to delete the mapping?", {
        actions: [MessageBox.Action.OK, MessageBox.Action.CANCEL],
        emphasizedAction: MessageBox.Action.OK,
        onClose: function (sAction) {
          if (sAction == MessageBox.Action.OK) {
            var sQuery = "/api/v1/passmapping/" + p.id;
            var mParameters = {
              bShowBusyIndicator: true,
            };
            this.deleteWithJSONP(sQuery, mParameters)
              .then(
                function () {
                  this.messageBoxGenerator("Mapping was deleted successfully!", true);
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

    editPassProperty: function (oEvent) {
      var rowItem = oEvent.getSource();
      var ctx = rowItem.getBindingContext();
      var p = ctx.getModel().getProperty(ctx.getPath());
      this.isEdit = true;
      this.passPropertyId = p.id;
      this.loadPassProperty(p.id);
      this.loadFragment("AddPassProperty");
    },

    editPassMapping: function (oEvent) {
      var rowItem = oEvent.getSource();
      var ctx = rowItem.getBindingContext();
      var p = ctx.getModel().getProperty(ctx.getPath());
      this.isEdit = true;
      this.passMappingId = p.id;
      this.loadPassMapping(p.id);
      this.loadFragment("AddPassMapping");
    },

    savePass: function () {
      var mdl = this.getView().getModel();
      var obj = mdl.getProperty("/");
      this.updatePass(obj);
    },

    updatePass: function (obj) {
      var sQuery = "/api/v1/pass/" + obj.id;
      var mParameters = {
        bShowBusyIndicator: true,
      };
      this.updateDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
        .then(
          function () {
            this.messageBoxGenerator("Pass saved!", true);
            this.loadPass(this.id);
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
