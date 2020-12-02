sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/ui/core/routing/History",
    "com/asena/ui5/formatter/Formatter",
    "sap/m/MessageBox",
    "sap/ui/model/Filter",
	"sap/ui/model/FilterOperator"
], function (Controller, History, Formatter, MessageBox, Filter, FilterOperator) {
    "use strict";

    return Controller.extend("core.controller.BaseController", {

        createDataWithAjaxP: function(url, sJsonData, mParameters) {
            return this.saveDataWithAjaxP(url, sJsonData, "POST", mParameters);
        },

        updateDataWithAjaxP: function(url, sJsonData, mParameters) {
            return this.saveDataWithAjaxP(url, sJsonData, "PUT", mParameters);
        },

        saveDataWithAjaxP: function (url, sJsonData, method, mParameters) {
            return new Promise(function (resolve, reject) {
                var bSuppressBusyIndicator = false;
                if (mParameters && mParameters.bSuppressBusyIndicator) {
                    bSuppressBusyIndicator = true;
                }
                if (!bSuppressBusyIndicator) {
                    sap.ui.core.BusyIndicator.show(0);
                }
                jQuery.ajax(url, {
                    type: method,
                    beforeSend: function (request) {
                        request.setRequestHeader("Accept", "application/json");
                        request.setRequestHeader("Content-Type", "application/json");
                    },
                    data: sJsonData,
                    async: true,
                    success: function (responseJSON, statustext, httpresponse) {
                        if (mParameters && mParameters.mSpecialParameters) {
                            resolve(responseJSON, statustext, httpresponse, mParameters.mSpecialParameters);
                        } else {
                            resolve(responseJSON, statustext, httpresponse);
                        }
                        if (!bSuppressBusyIndicator) {
                            jQuery.sap.delayedCall(500, this, function () {
                                sap.ui.core.BusyIndicator.hide();
                            });
                        }
                    },
                    error: function (httpresponse, statustext, error) {
                        reject(httpresponse, statustext, error);
                        if (!bSuppressBusyIndicator) {
                            jQuery.sap.delayedCall(500, this, function () {
                                sap.ui.core.BusyIndicator.hide();
                            });
                        }
                    }
                });
            }.bind(this));
        },

        

        messageBoxGenerator: function (sText, isSuccess) {
            if (!isSuccess) {
                MessageBox.error(sText);
            } else {
                MessageBox.success(sText);
            }
        },

        showError: function(oError) {
            var json = oError.responseJSON;
            var message =JSON.stringify(json);
            if (json) {
                message = json.message;
            }
            this.messageBoxGenerator("Status Code: "+oError.status+ " \n Error Message: "+ message, false);
        },

        // Added while developing #FCL:
        loadJsonWithAjaxP: function (url, mParameters) {
            return new Promise(function (resolve, reject) {

                var bSuppressBusyIndicator = false;
                if (mParameters && mParameters.bSuppressBusyIndicator) {
                    bSuppressBusyIndicator = true;
                }
                if (!bSuppressBusyIndicator) {
                    sap.ui.core.BusyIndicator.show(0);
                }
                jQuery.ajax(url, {
                    type: "GET",
                    beforeSend: function (request) {
                        request.setRequestHeader("Accept", "application/json");
                    },
                    success: function (responseJSON, statustext, httpresponse) {
                        if (!responseJSON) {
                            responseJSON = {};
                        }

                        if ((mParameters) && (mParameters.mSpecialParameters)) {
                            responseJSON.mSpecialParameters = mParameters.mSpecialParameters;
                        }

                        resolve(responseJSON);

                        if (!bSuppressBusyIndicator) {
                            jQuery.sap.delayedCall(500, this, function () {
                                sap.ui.core.BusyIndicator.hide();
                            });
                        }
                    },
                    error: function (httpresponse, statustext, error) {
                        reject(httpresponse, statustext, error);
                        if (!bSuppressBusyIndicator) {
                            jQuery.sap.delayedCall(500, this, function () {
                                sap.ui.core.BusyIndicator.hide();
                            });
                        }
                    }
                });
            }.bind(this));
        },

        deleteWithJSONP: function (url, mParameters) {
            return new Promise(function (resolve, reject) {

                var bSuppressBusyIndicator = false;
                if (mParameters && mParameters.bSuppressBusyIndicator) {
                    bSuppressBusyIndicator = true;
                }
                if (!bSuppressBusyIndicator) {
                    sap.ui.core.BusyIndicator.show(0);
                }
                jQuery.ajax(url, {
                    type: "DELETE",
                    beforeSend: function (request) {
                        request.setRequestHeader("Accept", "application/json");
                    },
                    success: function (responseJSON, statustext, httpresponse) {
                        if (!responseJSON) {
                            responseJSON = {};
                        }

                        if ((mParameters) && (mParameters.mSpecialParameters)) {
                            responseJSON.mSpecialParameters = mParameters.mSpecialParameters;
                        }

                        resolve(responseJSON);

                        if (!bSuppressBusyIndicator) {
                            jQuery.sap.delayedCall(500, this, function () {
                                sap.ui.core.BusyIndicator.hide();
                            });
                        }
                    },
                    error: function (httpresponse, statustext, error) {
                        reject(httpresponse, statustext, error);
                        if (!bSuppressBusyIndicator) {
                            jQuery.sap.delayedCall(500, this, function () {
                                sap.ui.core.BusyIndicator.hide();
                            });
                        }
                    }
                });
            }.bind(this));
        },

        goBack: function (site, mskey) {
            if (History.getInstance().getPreviousHash() !== undefined) {
                window.history.go(-1);
            } else { // nav to home screen if history is lost
                if (site !== undefined) {
                    sap.ui.core.UIComponent.getRouterFor(this).navTo(site, {
                        app: site,
                    }, false);
                } else {
                    sap.ui.core.UIComponent.getRouterFor(this).navTo("applications", {
                        app: "applications"
                    }, false);
                }
            }
        },

        loadFragment: function (sFragmentName) {
            if (this.genericDialog) {
                this.getView().removeDependent(this.genericDialog);
                this.genericDialog.destroy();
            }
            this.genericDialog = sap.ui.xmlfragment(
                "com.asena.ui5.view.fragment." + sFragmentName,
                this
            );
            this.getView().addDependent(this.genericDialog);
            this.genericDialog.open();
        }
    });
});
