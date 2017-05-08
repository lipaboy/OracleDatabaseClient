package ru.nsu.fit.g14201.lipatkin.core;

import ru.nsu.fit.g14201.lipatkin.model.SQLCommander;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by SPN on 08.05.2017.
 */
public interface BeforeQuitOperation {
    public void quit();
}
