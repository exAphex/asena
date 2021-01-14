package com.asena.scimgateway.logger;

import com.asena.scimgateway.LoggerConfig;

public class LoggerFactory {
        private static final class InstanceHolder {
          // Die Initialisierung von Klassenvariablen geschieht nur einmal 
          // und wird vom ClassLoader implizit synchronisiert

          static final Logger INSTANCE = LoggerConfig.getBean(Logger.class);
        }
      
        // Verhindere die Erzeugung des Objektes über andere Methoden
        private LoggerFactory () {}
        // Eine nicht synchronisierte Zugriffsmethode auf Klassenebene.
        public static Logger getLogger () {
          return InstanceHolder.INSTANCE;
        }
}