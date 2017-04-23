package ru.nsu.fit.g14201.lipatkin;

import java.sql.*;
import java.util.Locale;

public class OracleClientMain
{
    public static void main(String[] args)
    {
        try {
            Locale.setDefault(Locale.ENGLISH);      //for output Oracle system information //VERY IMPORTANT
                //If you use a / it is a net service name, if you use a colon it is a SID.
            String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";;
                //if you have exception here than you add ojdbc14 (3) in jdk wrong
            Class.forName("oracle.jdbc.driver.OracleDriver");
                //TODO: add logging
            //System.out.println("Oracle JDBC Driver Registered!");
            Connection cn = DriverManager.getConnection (url,
                            "USER1",
                            "r4kxlktt");		//at this moment you can have different errors like:
                                                //wrong NLS language (wrong Locale.setDefault)
                                                //no network connection (oracle.exe doesn't run)
                                                //no listeners (wrong file listener.ora or TNSLSNR.exe doesn't run)

                //TODO: logging
            /*if (cn != null)
                System.out.println("Be Happy");
            else
                System.out.println("Bad connection");*/

            Statement st = cn.createStatement();
            ResultSet rs =
                    st.executeQuery ("SELECT * FROM DEMO_STATES");		//in your table (Oracle XE) can be russian entries

            while (rs.next()) {
                System.out.println("Number=" + rs.getString(1) + " " +
                        "Name=" + rs.getString(2));
            }

            st.close();
            cn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}