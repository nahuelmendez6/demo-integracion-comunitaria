package com.integracioncomunitaria.database;



public class ResultDataBase {
    private boolean success;
    private String message;
    private Object object;

    // Constructor
    public ResultDataBase() {
        this.success = false;
        this.message = "";
        this.object = null;
    }

    public ResultDataBase(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.object = null;
    }

    // Getters y Setters
    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}