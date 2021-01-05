package com.asena.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.asena.utils.FileUtils;

public class ScriptProcessor {
    public static void installScripts(HashMap<String, String> settings) throws IOException {
        String installationPath = settings.get("com.asena.scimgateway.installationpath");
        String scriptPath = FileUtils.addTrailingBackslash(installationPath) + "scripts/";
        Files.createDirectories(Paths.get(scriptPath));
        copySystemdScript(installationPath, scriptPath);
        printScriptMessage(installationPath);
    } 

    private static void copySystemdScript(String installationPath, String scriptPath) throws IOException {
        String script = getResourceFileAsString("scripts/asena.service");
        String fullPath = FileUtils.addTrailingBackslash(installationPath) + "asena.jar";
        String formattedScript = String.format(script, fullPath);
        scriptPath = FileUtils.addTrailingBackslash(scriptPath) + "asena.service";
        org.apache.commons.io.FileUtils.writeStringToFile(new File(scriptPath), formattedScript, StandardCharsets.UTF_8);
    }

    private static String getResourceFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    public static void printScriptMessage(String installationPath) {
        System.out.println("=================");
        System.out.println("Service scripts created!");
        System.out.println("To install asena as a service run the following command:");
        System.out.println("     sudo cp " + FileUtils.addTrailingBackslash(installationPath) + "scripts/asena.service /etc/systemd/system/asena.service"); 
        System.out.println("     sudo systemctl start asena");
        System.out.println("For automatic startup of the service:");
        System.out.println("     sudo systemctl enable asena"); 
        System.out.println("=================");
    }
}