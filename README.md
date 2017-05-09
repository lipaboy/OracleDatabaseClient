# OracleDatabaseClient

//For Windows NT

	Customizations:

1) Download Oracle Database XE 11g
	ORACLEXE112_Win64

2) Install XE

3) Copy ojdbc14 to (find) jdk8***\jre\ext

4) Copy classes12.zip and ocijdbc8.dll (from jdbc817jdk12-nt.zip) 
	to {$ORACLE_XE_INSTALL_PLACE = "C:\oraclexe"}\app\oracle\product\11.2.0\server\jdbc\lib

5) Set environment variables: 
	ORACLE_HOME = {$ORACLE_XE_INSTALL_PLACE}\app\oracle\product\11.2.0\server
	CLASSPATH = %ORACLE_HOME%\jdbc\lib\classes12.zip;.
													 | - ";." - it is neccesary

6) Check in Task Manager that you have two processes:
	a) oracle.exe
	b) TNSLSNR.exe		//important

	6.2) If you don't see process "oracle.exe" than go to "All programs -> Oracle Database 11g Express Edition -> Start Database".

7) Open Oracle DB XE (maybe you need to rewrite link "Get Started" -> change port on 8080)
	- enter as "system" (like root)  (password you entry when setup the application)
	- create account in "Application Express" tab (for example: 
														database username: USER1
														Application Express username: USER1
														and password
														- advice: write usernames the same)
	- login
	- You have already had DEMO_STATES table. Lets check the work.
	
7.1) You can test connection: install plugin in IntellIj IDEA "Database Navigator" and try to connect to Oracle XE (press button Test Connection)
	
	Java Code: 

package ru.nsu.fit.g14201.lipatkin;
import java.sql.*;
import java.util.Locale;

//import oracle.jdbc.driver.*;

public class App
{
    public static void main(String[] args)
    {
        Locale.setDefault(Locale.ENGLISH);      //for output Oracle system information //VERY IMPORTANT
        String url = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");		//if you have exception here than you add ojdbc14 (3) in jdk wrong
        } catch (ClassNotFoundException e) {

            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
            return;
        }

        System.out.println("Oracle JDBC Driver Registered!");
        //If you use a / it is a net service name, if you use a colon it is a SID.
        url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";

        if (url == null) {
            System.out.println("usage: StaffByJDBC [thin/oci]");
            return;
        }

        try {
            Class theDriver = Class.forName("oracle.jdbc.driver.OracleDriver");

            //DriverManager.registerDriver (
              //      (Driver)theDriver.newInstance());
        }
        catch (Exception e) { return; }

        try {
            Connection cn =
                    DriverManager.getConnection (url,
                            //"hr",
                            "USER1",
                            "password");		//at this moment you can have different errors like: 
												//wrong NLS language (wrong Locale.setDefault)
												//no network connection (oracle.exe doesn't run)
												//no listeners (wrong file listener.ora or TNSLSNR.exe doesn't run)

            if (cn != null)
                System.out.println("Be Happy");
            else
                System.out.println("Bad connection");

            Statement st = cn.createStatement();
            ResultSet rs =
                    st.executeQuery ("SELECT * FROM DEMO_STATES");		//in your table (Oracle XE) can be russian entries

            while (rs.next()) {
                System.out.println("Number=" + rs.getString(1) + " " +
                        "Name=" + rs.getString(2));
            }

            st.close();

            cn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally { System.out.println("All that happened"); }
    }
}
