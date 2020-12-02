sap.ui.define(function() {
    "use strict";
  
    var Formatter = {
      countSystems: function(arrSystems) {
        if (!arrSystems) {
          return "0";
        }
        return arrSystems.length + "";
      }
    };
    return Formatter;
  }, true);
  