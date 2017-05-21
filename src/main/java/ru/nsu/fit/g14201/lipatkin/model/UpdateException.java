package ru.nsu.fit.g14201.lipatkin.model;

import ru.nsu.fit.g14201.lipatkin.core.OracleClientException;

/**
 * Created by RARETA on 09.05.2017.
 */
public class UpdateException extends OracleClientException {
    private String message;

    public UpdateException(String msg) { message = msg; }
    public String getMessage() { return "Update exception: " + message; }
}
