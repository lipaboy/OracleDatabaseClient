package ru.nsu.fit.g14201.lipatkin.core;

/**
 * Created by SPN on 01.05.2017.
 */
public interface DatabaseEnterable {
    public void enter(String username, String password) throws WrongUsernamePasswordException;
}
