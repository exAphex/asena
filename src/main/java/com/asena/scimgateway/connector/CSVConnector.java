package com.asena.scimgateway.connector;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class CSVConnector implements IConnector {

    private RemoteSystem template;
    private RemoteSystem remoteSystem;
    
    private String filepath;
    private String delimiter;
    private String hasQuotes;
    private String hasHeader;
    private String uniqueattribute;
    

    public CSVConnector() {
        template = new RemoteSystem("CSV-Connector", "Sample CSV-Connector");
        template.addProperty(new ConnectionProperty("filepath", "/home/user/data.csv", "Filepath to the CSV file",
                false, ConnectionPropertyType.STRING))
                .addProperty(new ConnectionProperty("delimiter", ";", "Delimiter used for csv", false,
                        ConnectionPropertyType.STRING))
                .addProperty(new ConnectionProperty("uniqueattribute", "username", "name of the unique attribute", false,
                        ConnectionPropertyType.STRING));

        template.addWriteMapping(new Attribute("username", "username", "username field"));
        template.setType("CSVConnector");
    }

    public RemoteSystem getRemoteSystemTemplate() {
        return template;
    }

    @Override
    public void setupConnector(RemoteSystem rs) {
        this.remoteSystem = rs;
        setConnectionProperties(rs);
    }

    @Override
    public void writeData(String type, RemoteSystem rs, HashMap<String, Object> data) throws IOException {
        String[] headers = getHeaders(data);
        insertData(this.filepath, headers, data);
        
        for (String s : data.keySet()) {
            System.out.println(s + " - " + data.get(s));
        }
    }

    public void setConnectionProperties(RemoteSystem rs) {
        Set<ConnectionProperty> cp = rs.getProperties();
        for (ConnectionProperty c : cp) {
            switch(c.getKey()) {
                case "filepath":
                    this.filepath = c.getValue();
                    break;
                case "delimiter":
                    this.delimiter = c.getValue();
                    break;
                case "uniqueattribute":
                    this.uniqueattribute = c.getValue();
                    break;
            }
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

        CSVPrinter csvPrinter = CSVFormat.RFC4180.withHeader(headers).print(out);

        if (!isAlreadyInRecords(records, data)) {
            csvPrinter.printRecords(records);
            csvPrinter.printRecord(data);
        } else {
            for (CSVRecord c : records) {
            }
        }
        csvPrinter.flush();

    }

    public boolean isAlreadyInRecords(List<CSVRecord> records, HashMap<String, Object> data) {
        Object inptData = data.get(this.uniqueattribute);
        if (inptData == null) {
            return false;
        }

        String searchValue = (String) inptData;
        for (CSVRecord c : records) {
            String tempData = c.get(this.uniqueattribute);
            if (tempData.equals(searchValue)) {
                return true;
            }
        }
        
        return false;
    }

    public List<CSVRecord> readData(String filepath, String[] headers) throws IOException {
        Reader in = new FileReader(filepath);

        List<CSVRecord> records = CSVFormat.DEFAULT
        .withHeader(headers)
        .withFirstRecordAsHeader().parse(in).getRecords();
        return records;
    }

    

}