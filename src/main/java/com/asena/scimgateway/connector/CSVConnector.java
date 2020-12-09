package com.asena.scimgateway.connector;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CSVConnector implements IConnector {

    private RemoteSystem template;

    public CSVConnector() {
        template = new RemoteSystem("CSV-Connector", "Sample CSV-Connector");
        template.addProperty(new ConnectionProperty("filepath", "/home/user/data.csv", "Filepath to the CSV file",
                false, ConnectionPropertyType.STRING))
                .addProperty(new ConnectionProperty("delimiter", ";", "Delimiter used for csv", false,
                        ConnectionPropertyType.STRING))
                .addProperty(new ConnectionProperty("quote", "true",
                        "if set to true then quotes will be used for csv entries", false,
                        ConnectionPropertyType.BOOLEAN))
                .addProperty(new ConnectionProperty("header", "true", "set to true if header line is set", false,
                        ConnectionPropertyType.BOOLEAN));

        template.addWriteMapping(new Attribute("username", "username", "username field"));
        template.setType("CSVConnector");
    }

    public RemoteSystem getRemoteSystemTemplate() {
        return template;
    }

    @Override
    public void writeData(String type, RemoteSystem rs, HashMap<String, Object> data) {
        String[] headers = getHeaders(data);
        for (String s : data.keySet()) {
            System.out.println(s + " - " + data.get(s));
        }
    }

    public String[] getHeaders(HashMap<String, Object> data) {
        String[] headers = new String[data.size()];
        int i = 0;
        for (String s : data.keySet()) {
            headers[i] = s;
            i++;
        }
        return headers;
    }

    public void insertData(String filepath, String[] headers, HashMap<String, Object> data) throws IOException {
        List<CSVRecord> records = readData(filepath, headers);
        
    }

    public List<CSVRecord> readData(String filepath, String[] headers) throws IOException {
        Reader in = new FileReader(filepath);
        Iterable<CSVRecord> csvrecords = CSVFormat.DEFAULT
        .withHeader(headers)
        .withFirstRecordAsHeader()
        .parse(in);

        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : csvrecords) {
            records.add(record);
        }

        return records;
    }

}