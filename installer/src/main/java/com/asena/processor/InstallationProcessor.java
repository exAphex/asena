package com.asena.processor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

public class InstallationProcessor {
    public static void processInstallation(HashMap<String, String> settings) throws IOException {
        String installationFolder = com.asena.utils.FileUtils
                .addTrailingBackslash(settings.get("com.asena.scimgateway.installationpath"));
        String jdbcPath = settings.get("com.asena.scimgateway.jdbc");
        String currentDirectory = com.asena.utils.FileUtils.addTrailingBackslash(System.getProperty("user.dir"));
        String appJarDirectory = currentDirectory + "asena.jar";
        String propertiesPath = installationFolder + "application.properties";

        FileUtils.copyFileToDirectory(new File(jdbcPath), new File(installationFolder));
        FileUtils.copyFileToDirectory(new File(appJarDirectory), new File(installationFolder));

        writePropertiesToFile(propertiesPath, settings);
    }

    public static void writePropertiesToFile(String propertiesPath, HashMap<String, String> settings)
            throws IOException {
        OutputStream output = new FileOutputStream(propertiesPath);
        Properties prop = new Properties();

        for (String key : settings.keySet()) {
            prop.setProperty(key, settings.get(key));
        }
        prop.store(output, null);
    }

    
}