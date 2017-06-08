package ru.nsu.fit.g14201.lipatkin.model;

import ru.nsu.fit.g14201.lipatkin.core.OracleClientException;

/**
 * Created by castiel on 08.06.2017.
 */
public class SelectException extends OracleClientException {
    private String message;

    public SelectException(String msg) { message = msg; }
    public String getMessage() { return "Select exception: " + message; }
}
