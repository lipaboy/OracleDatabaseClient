package ru.nsu.fit.g14201.lipatkin.network;

import org.apache.log4j.Logger;
import ru.nsu.fit.g14201.lipatkin.core.WrongUsernamePasswordException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Created by SPN on 01.05.2017.
 */
public class NetworkController {
    private static final Logger log = Logger.getLogger(NetworkController.class);
     //If you use a / it is a net service name, if you use a colon it is a SID.
    private String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";

    public NetworkController() {
        try {
            Locale.setDefault(Locale.ENGLISH);      //for output Oracle system information //VERY IMPORTANT

            //if you have exception here than you addTableEditorListener ojdbc14 (3) in jdk wrong
            Class.forName("oracle.jdbc.driver.OracleDriver");
            log.info("Oracle JDBC Driver Registered!");

        } catch (ClassNotFoundException exp) {
            log.error(exp.getMessage());
        }
    }

    public Connection getConnection(String username, String password) throws WrongUsernamePasswordException {
        try {
            Connection cn = DriverManager.getConnection(url, username, password);
            return cn;
        } catch (SQLException exp) {
            //TODO: you need to check non-wrongusernamepasswordexception like wrong url
            log.error(exp.getMessage());
            throw new WrongUsernamePasswordException();
        }
        //at this moment you can have different errors like:
        //wrong NLS language (wrong Locale.setDefault)
        //no network connection (oracle.exe doesn't run)
        //no listeners (wrong file listener.ora or TNSLSNR.exe doesn't run)

    }
}
