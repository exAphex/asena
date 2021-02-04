sap.ui.define(function() {
    "use strict";
  
    var Formatter = {

      convertLogIcon: function(state) {
        switch (state) {
          case "ERROR":
            return "sap-icon://message-error";
          case "WARN":
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
          case "WARN":
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
      },

      convertTimestamp: function(timestamp) {
        var date = new Date(timestamp);
        var month = date.getMonth() + 1;
        var day = date.getDate();
        var hour = date.getHours();
        var min = date.getMinutes();
        var sec = date.getSeconds();

        month = (month < 10 ? "0" : "") + month;
        day = (day < 10 ? "0" : "") + day;
        hour = (hour < 10 ? "0" : "") + hour;
        min = (min < 10 ? "0" : "") + min;
        sec = (sec < 10 ? "0" : "") + sec;
        return hour + ":" + min + ":" + sec + ' ' + day + "-" + month + "-" + date.getFullYear();
      }
    };
    return Formatter;
  }, true);
  