package com.asena.scimgateway.exception;

public class InternalErrorException extends RuntimeException {

    private Object obj;

    private static final long serialVersionUID = 5409117063462161396L;

    public InternalErrorException(String id) {
        super(id);
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public InternalErrorException(String id, Throwable e, Object obj) {
        super(id, e);
        this.obj = obj;
    }
}