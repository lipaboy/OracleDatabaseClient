package ru.nsu.fit.g14201.lipatkin.core;

import org.apache.log4j.Logger;
import ru.nsu.fit.g14201.lipatkin.gui.LoginFrame;
import ru.nsu.fit.g14201.lipatkin.gui.ObjectBrowserFrame;
import ru.nsu.fit.g14201.lipatkin.model.SQLCommander;
import ru.nsu.fit.g14201.lipatkin.model.SQLCommandExecuter;
import ru.nsu.fit.g14201.lipatkin.network.NetworkController;

import java.sql.Connection;

/**
 * Created by SPN on 01.05.2017.
 */
public class Presenter {
    private static final Logger log = Logger.getLogger(Presenter.class);


    public Presenter() {}

    public void login() {
        LoginFrame window = new LoginFrame(new DatabaseEnterable() {
            @Override
            public void enter(String username, String password) throws WrongUsernamePasswordException {
                NetworkController networkController = new NetworkController();
                Connection connection = networkController.getConnection(username, password);
                    //if no exception then
                //I don't want to use reference on Presenter (this) because
                // I don't know which dependency it will cause to
                new Presenter().startSession(connection);
            }
        });
        window.setVisible(true);
    }

    public void startSession(Connection connection) {
        log.info("Start session");

        SQLCommander commander = new SQLCommandExecuter(connection);

        //TODO: when close the app you need to close the connection
        ObjectBrowserFrame window = new ObjectBrowserFrame(commander);
        window.setVisible(true);


    }

}
