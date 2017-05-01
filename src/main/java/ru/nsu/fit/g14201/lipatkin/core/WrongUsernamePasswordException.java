package ru.nsu.fit.g14201.lipatkin.core;

import ru.nsu.fit.g14201.lipatkin.core.OracleClientException;

/**
 * Created by SPN on 01.05.2017.
 */
public class WrongUsernamePasswordException extends OracleClientException {
    public String getMessage() { return "Database enter exception: wrong username or password"; }
}
