package ru.nsu.fit.g14201.lipatkin.network;

import ru.nsu.fit.g14201.lipatkin.OracleClientException;

/**
 * Created by SPN on 01.05.2017.
 */
public class WrongUsernamePasswordException extends OracleClientException {
    public String getMessage() { return "Database enter exception: wrong username or password"; }
}
