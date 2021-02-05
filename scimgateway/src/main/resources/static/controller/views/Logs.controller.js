sap.ui.define([
    "controller/core/BaseController",
    "sap/ui/model/json/JSONModel",
    "sap/m/MessageBox",
    "com/asena/ui5/formatter/Formatter"
], function (Controller, JSONModel, MessageBox, Formatter) {
    "use strict"; 
    return Controller.extend("com.asena.ui5.controller.views.Logs", {
        formatter: Formatter,

        onInit: function () {
            var target = sap.ui.core.UIComponent.getRouterFor(this).getTarget("Scripts");
            target.attachDisplay(this._onDisplay, this);
            sap.ui.core.UIComponent.getRouterFor(this).getRoute("logs").attachPatternMatched(this._onObjectMatched, this);
        },

        _onObjectMatched: function() {
            this.loadLogs();
    	},

        _onDisplay: function () {
            var mainModel = sap.ui.getCore().getModel("mainModel");
            mainModel.setProperty("/showNavButton", true);
        },

        loadLogs: function() {
            var sQuery = "/api/v1/log";
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.loadJsonWithAjaxP(sQuery, mParameters)
                .then(function (oData) {
                    var oMainModel = new JSONModel(oData);
                    this.getView().setModel(oMainModel);
                }.bind(this))
                .catch(function (oError) {
                    this.messageBoxGenerator("Status Code: "+oError.status+ " \n Error Message: "+ JSON.stringify(oError.responseJSON), false);
                }.bind(this));
        },

        _onRefreshLogs: function() {
            this.loadLogs();
        },

        _onDeleteLogs: function() {
            MessageBox.warning("Are you sure you want to delete all logs?", {
				actions: [MessageBox.Action.OK, MessageBox.Action.CANCEL],
				emphasizedAction: MessageBox.Action.OK,
				onClose: function (sAction) {
                    if (sAction == MessageBox.Action.OK) {
                        var sQuery = "/api/v1/log";
                        var mParameters = {
                            bShowBusyIndicator: true
                        };
                        this.deleteWithJSONP(sQuery, mParameters)
                            .then(function () {
                                this.messageBoxGenerator("Logs were cleaned!", true);
                                this.loadLogs();
                            }.bind(this))
                            .catch(function (oError) {
                                this.showError(oError); 
                            }.bind(this));
                    }
				}.bind(this)
			});
        },

        _onSearchLogs: function(oEvent) {
            var sQuery = oEvent.getParameter("query");

            var oFilter = this.getLogFilters(sQuery);
            var oTable = this.getView().byId("tblLogs");
            oTable.getBinding("rows").filter(oFilter, "Application");
        },

        _onLogDetail: function(oEvent) {
            var rowItem = oEvent.getSource();
            var ctx = rowItem.getBindingContext();
            var objLog = ctx.getModel().getProperty(ctx.getPath());

            objLog.eventmessage = this.parseEvents(objLog.event);
            var mdl = new JSONModel(objLog);
            this.getView().setModel(mdl, "mdlLogDialog");

            this.loadFragment("LogDialog");
        },

        parseEvents : function(obj) {
            function compare( a, b ) {
                if ( a.i < b.i ){
                    return -1;
                }
                if ( a.i > b.i ){
                    return 1;
                }
                return 0;
            }

            if ((obj) && (obj.length > 0)) {
                obj.sort(compare);
                var eventMessage = "";
                for (var i = 0; i < obj.length; i++) {
                    eventMessage += obj[i].message + "\n";
                }
                return eventMessage;
            } else {
                return "";
            }
        }
    });
});
