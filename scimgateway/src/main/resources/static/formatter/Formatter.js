sap.ui.define(function() {
    "use strict";
  
    var Formatter = {

      convertLogIcon: function(state) {
        switch (state) {
          case "ERROR":
            return "sap-icon://message-error";
          case "WARNING":
            return "sap-icon://message-warning";
          case "INFO":
            return "sap-icon://message-information";
          case "DEBUG":
            return "sap-icon://feed";
        }
        return "";
      },

      convertLogState: function(state) {
        switch (state) {
          case "ERROR":
            return "Error";
          case "WARNING":
            return "Indication03";
          case "INFO":
            return "Information";
          case "DEBUG":
            return "Indication06";
        }
        return "None";
      },

      countSystems: function(arrSystems) {
        if (!arrSystems) {
          return "0";
        }
        return arrSystems.length + "";
      },

      convertSystemStatus: function(state) {
        if (state) {
          return "Active";
        } else {
          return "Disabled";
        }
      },

      convertSystemColor: function(state) {
        if (state) {
          return "Success";
        } else {
          return "Error";
        }
      },

      convertSystemStatusChangerIcon: function(state) {
        if (state) {
          return "sap-icon://pause";
        } else {
          return "sap-icon://activate";
        }
      }
    };
    return Formatter;
  }, true);
  