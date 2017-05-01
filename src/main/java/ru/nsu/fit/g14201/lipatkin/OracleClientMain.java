package ru.nsu.fit.g14201.lipatkin;

import org.apache.log4j.Logger;
import ru.nsu.fit.g14201.lipatkin.gui.LoginFrame;

public class OracleClientMain
{
    private static final Logger log = Logger.getLogger(OracleClientMain.class);

    public static void main(String[] args)
    {
        LoginFrame window = new LoginFrame();
        window.setVisible(true);
    }
}