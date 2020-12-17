package com.asena.scimgateway.logger;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.Log;
import com.asena.scimgateway.model.Log.LogType;
import com.asena.scimgateway.service.LogService;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class Logger {

    @Autowired
    private LogService logService;

    private LogType logLevel = LogType.ERROR;

    public void info(String info) {
        if ((logLevel == LogType.INFO)) {
            Log l = new Log(info, LogType.INFO);
            logService.create(l);
        }
        
    }

    public LogType getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogType logLevel) {
        this.logLevel = logLevel;
    }

    public void debug(String debug) {
        if ((logLevel == LogType.INFO) || (logLevel == LogType.DEBUG)) {
            Log l = new Log(debug, LogType.DEBUG);
            logService.create(l);
        }

    }

    public void warning(String warning) {
        if ((logLevel == LogType.INFO) || (logLevel == LogType.DEBUG) || (logLevel == LogType.WARNING)) {
            Log l = new Log(warning, LogType.WARNING);
            logService.create(l);
        }
    }

    public void error(String error) {
        if ((logLevel == LogType.INFO) || (logLevel == LogType.DEBUG) || (logLevel == LogType.WARNING) || (logLevel == LogType.ERROR)) {
            Log l = new Log(error, LogType.ERROR);
            logService.create(l);
        }
    }

    public void error(Throwable e) {
        if ((logLevel == LogType.INFO) || (logLevel == LogType.DEBUG) || (logLevel == LogType.WARNING) || (logLevel == LogType.ERROR)) {
            if (e == null) {
                return;
            }

            if (e instanceof InternalErrorException) {
                InternalErrorException tmpE = (InternalErrorException)e;
                if (tmpE.getObj() != null) {
                    String sObj = ((InternalErrorException)e).getObj().toString();
                    sObj = "Object: " + sObj;
                    Log objectLog = new Log(sObj, LogType.ERROR);
                    logService.create(objectLog);
                }
            }
            String message = e.getMessage();
            Log messageLog = new Log(message, LogType.ERROR);
            logService.create(messageLog);

            String s = ExceptionUtils.getStackTrace(e);
            Log stackTrace = new Log(s, LogType.ERROR);
            logService.create(stackTrace);

            
        }
    }
}