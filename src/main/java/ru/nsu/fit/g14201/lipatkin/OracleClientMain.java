package ru.nsu.fit.g14201.lipatkin;

import org.apache.log4j.Logger;
import ru.nsu.fit.g14201.lipatkin.core.Presenter;
import ru.nsu.fit.g14201.lipatkin.network.NetworkController;

public class OracleClientMain
{
    private static final Logger log = Logger.getLogger(OracleClientMain.class);

    public static void main(String[] args)
    {
        Presenter presenter = new Presenter();
        //presenter.login();
        presenter.startSession(new NetworkController().getConnection("user1", "r4kxlktt"));
    }
}