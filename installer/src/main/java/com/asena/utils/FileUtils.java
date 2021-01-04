package com.asena.utils;


public class FileUtils {
    public static String addTrailingBackslash(String path) {
        return path.endsWith("/") ? path : path + "/";
    }
}