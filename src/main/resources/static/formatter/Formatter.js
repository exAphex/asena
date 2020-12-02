sap.ui.define(function() {
    "use strict";
  
    var Formatter = {
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
          return 8;
        } else {
          return 3;
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
  