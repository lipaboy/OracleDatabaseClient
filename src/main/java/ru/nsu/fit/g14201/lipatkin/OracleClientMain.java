package ru.nsu.fit.g14201.lipatkin;

import org.apache.log4j.Logger;
import ru.nsu.fit.g14201.lipatkin.core.Controller;
import ru.nsu.fit.g14201.lipatkin.network.NetworkController;

public class OracleClientMain
{
    private static final Logger log = Logger.getLogger(OracleClientMain.class);

    public static void main(String[] args)
    {
        Controller controller = new Controller();
        //controller.login();
        controller.startSession(new NetworkController().getConnection("user1",
                "r4kxlktt"
             //   "user"
        ));
    }
}