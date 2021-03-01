sap.ui.define(function() {
    "use strict";
  
    var SAPIdMExporter = {

        exportToSAPIdM: function (name, description, host, path, port, user, password) {
            var header = "";
            var data = "";
            var csvData = {};
            csvData.Name = name;
            csvData.Description = description;
            csvData.Type = null;
            csvData.Disabled = "0";
            csvData.TypeQN = "com.sap.idm.connector.scim:reptype.SCIM";
            csvData.AUTH_PASSWORD = password;
            csvData.AUTH_TYPE = "basic";
            csvData.AUTH_USER = user; 
            csvData.CONNECT_TIMEOUT = "60000";
            csvData.CONNECTION_KEEPALIVE = "60000";
            csvData.MX_PRIV_GROUPING_ATTRIBUTE = null;
            csvData.OAUTH_URL = null;
            csvData.PROXY_HOST = null;
            csvData.PROXY_PASSWORD = null;
            csvData.PROXY_PORT = null;
            csvData.PROXY_USER = null;
            csvData.READ_TIMEOUT = "60000";
            csvData.SCIM_ASSIGNMENT_METHOD = "PATCH";
            csvData.SCIM_HOST = host;
            csvData.SCIM_PATH = path;
            csvData.SCIM_PORT = port;
            csvData.SCIM_PROTOCOL = "http";
            csvData.SYSTEM_PRIVILEGE = "PRIV:SYSTEM:" + name;
            csvData.TRUSTSTORE = null;
            csvData.TRUSTSTORE_PASSWORD = null;

            for (var key in csvData) {
                header += key + ",";
                data = SAPIdMExporter.addField(data, csvData[key]);
            }
            return header + "\n" + data;
        },

        addField: function(data, field) {
            if (!field) {
                return data + ",";
            } else {
                return data + "\"" + field + "\",";
            }
        }
    };
    return SAPIdMExporter;
  }, true);
  