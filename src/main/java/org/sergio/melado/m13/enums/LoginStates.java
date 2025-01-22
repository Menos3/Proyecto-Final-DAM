package org.sergio.melado.m13.enums;

public enum LoginStates {

    GRANTED(0,"Acces garantit "),
    FAILED(-1,"Acces erroni"),
    LOCKED(1,"Compte bloquejat"),
    NOEXIST(2,"Usuari no existent");

    private int errorCode;
    private String message;

    LoginStates (int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode () {
        return errorCode;
    }

    public void setErrorCode (int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }
}
