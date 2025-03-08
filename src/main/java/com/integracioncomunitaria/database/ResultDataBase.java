package com.integracioncomunitaria.database;


// Clase que representa el resultado de una operacion en la base de datos
public class ResultDataBase {
    private boolean success;    // operacion exitosa
    private String message;     // mensaje asociado al resultado de la operacion
    private Object object;      // objecto que puede contener informacion adicional relacionada con el resultado

    // Constructor por defecto, establece valores predeterminados
    public ResultDataBase() {
        this.success = false;   // Por defecto, la operación no fue exitosa
        this.message = "";       // Sin mensaje por defecto
        this.object = null;     // Sin objeto asociado por defecto
    }

    // Constructor que permite establecer el éxito y el mensaje al momento de la creación
    public ResultDataBase(boolean success, String message) {
        this.success = success; // Constructor que permite establecer el éxito y el mensaje al momento de la creación
        this.message = message; // Establece el mensaje asociado al resultado
        this.object = null;     // No se asocia un objeto adicional en este constructor
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