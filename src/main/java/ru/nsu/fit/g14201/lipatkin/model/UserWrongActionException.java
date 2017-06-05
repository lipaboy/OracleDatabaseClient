package ru.nsu.fit.g14201.lipatkin.model;

import ru.nsu.fit.g14201.lipatkin.core.OracleClientException;

/**
 * Created by RARETA on 05.06.2017.
 */
public class UserWrongActionException extends OracleClientException {
    private String message;

    public UserWrongActionException(String msg) { message = msg; }
    public String getMessage() { return "Error: " + message; }
}
