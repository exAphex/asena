package com.asena.scimgateway.connector;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;

public class CSVConnector {

    private RemoteSystem template;

    public CSVConnector() {
        template = new RemoteSystem("CSV-Connector", "Sample CSV-Connector");
        template.addProperty(new ConnectionProperty("filepath", "/home/user/data.csv", "Filepath to the CSV file", false, ConnectionPropertyType.STRING))
        .addProperty(new ConnectionProperty("delimiter", ";", "Delimiter used for csv", false, ConnectionPropertyType.STRING))
        .addProperty(new ConnectionProperty("quote", "true", "if set to true then quotes will be used for csv entries", false, ConnectionPropertyType.BOOLEAN))
        .addProperty(new ConnectionProperty("header", "true", "set to true if header line is set", false, ConnectionPropertyType.BOOLEAN));
       
        template.addWriteMapping(new Attribute("username", "username", "username field"));
        template.setType("CSVConnector");
    }

    public RemoteSystem getRemoteSystemTemplate() {
        return template;
    }

}