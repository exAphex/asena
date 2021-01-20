sap.ui.define([
    "controller/core/BaseController",
    "sap/ui/model/json/JSONModel",
    "sap/m/MessageBox",
    "com/asena/ui5/formatter/Formatter"
], function (Controller, JSONModel, MessageBox, Formatter) {
    "use strict"; 
    return Controller.extend("com.asena.ui5.controller.views.Settings", {
        formatter: Formatter,

        onInit: function () {
            var target = sap.ui.core.UIComponent.getRouterFor(this).getTarget("Settings");
            target.attachDisplay(this._onDisplay, this);
            sap.ui.core.UIComponent.getRouterFor(this).getRoute("settings").attachPatternMatched(this._onObjectMatched, this);
        },

        _onObjectMatched: function() {
            var mdl = new JSONModel({});
            this.getView().setModel(mdl);
    	},

        _onDisplay: function () {
            var mainModel = sap.ui.getCore().getModel("mainModel");
            mainModel.setProperty("/showNavButton", true);
        },

        _onSaveSettings: function() {
            var mdl = this.getView().getModel();
            var obj = mdl.getProperty("/");
            try {
                this.validatePassword(obj);
                this.updateUser(obj);
            } catch (e) {
                this.messageBoxGenerator(e, false);
            }
        },

        updateUser: function(obj) {
            var sQuery = "/api/v1/user";
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.updateDataWithAjaxP(sQuery, JSON.stringify(obj), mParameters)
                .then(function () {
                    this.messageBoxGenerator("Settings saved!", true);
                }.bind(this))
                .catch(function (oError) {
                    this.showError(oError); 
                }.bind(this));
        },

        validatePassword: function(obj) {
            if (!obj) {
                throw "Cant resolve object!";
            }

            if ((!obj.password) || (obj.password.length < 8)) {
                throw "Please use at least 8 characters!";
            }

            if (obj.password !== obj.repeatpassword) {
                throw "Selected password and repeated password do not match!"
            }
        }
    });       
});
