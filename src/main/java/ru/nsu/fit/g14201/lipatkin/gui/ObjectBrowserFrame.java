package ru.nsu.fit.g14201.lipatkin.gui;

import ru.nsu.fit.g14201.lipatkin.core.BeforeQuitOperation;
import ru.nsu.fit.g14201.lipatkin.model.DBManager;
import ru.nsu.fit.g14201.lipatkin.model.SQLCommander;
import ru.nsu.fit.g14201.lipatkin.view.DBPresenter;
import ru.nsu.fit.g14201.lipatkin.view.TableEditorState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by castiel on 02.05.2017.
 */
public class ObjectBrowserFrame extends JFrame{
    private JPanel panel1;
    private JList tableList;
    private JTable tableView;
    private JButton dataEditorButton;
    private JButton constructorButton;
    private JButton viewButton;
    private JButton addFieldButton;
    private TableEditorState tableEditorState;

    public ObjectBrowserFrame(final DBManager dbManager, BeforeQuitOperation beforeQuitOperation) {
        super("Object Browser");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension size = new Dimension(800, 500);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        setBounds((width - size.width) / 2, (height - size.height) / 2, size.width, size.height);

        /*---------------Presenter creation-------------------*/

        //NICE: if you move 3 bottom lines through away Panel Componets which depend on tableEditorState
        //then nothing will change.
        //TODO: I think that would be better to incapsulate the ability to change TableEditorState directrly
        //TODO: solution: may be use lambdas or like that
        tableEditorState = new TableEditorState(TableEditorState.States.CONSTRUCTOR);
        DBPresenter dbPresenter = new DBPresenter(dbManager, tableList, tableView, tableEditorState);
        tableEditorState.add(dbPresenter);

        /*---------------Panel components-------------------*/

        tableView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableEditorState.set(TableEditorState.States.VIEW);
            }
        });
        constructorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableEditorState.set(TableEditorState.States.CONSTRUCTOR);
            }
        });
        dataEditorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableEditorState.set(TableEditorState.States.DATA_EDITOR);
            }
        });



        /*-----------------Menu bar-------------------------*/

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JFrame currentFrame = this;
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                beforeQuitOperation.quit();
                currentFrame.dispose(); //TODO: close this window and got to LoginFrame
            }
        });
        fileMenu.add(quitItem);

        this.setJMenuBar(menuBar);


    }

    //TODO: close connection in windowClosing event
    // extends JFrame implements WindowListener,
//    WindowFocusListener,
//    WindowStateListener

//    public void windowClosing(WindowEvent e) {
//        displayMessage("WindowListener method called: windowClosing.");
//        //A pause so user can see the message before
//        //the window actually closes.
//        ActionListener task = new ActionListener() {
//            boolean alreadyDisposed = false;
//            public void actionPerformed(ActionEvent e) {
//                if (frame.isDisplayable()) {
//                    alreadyDisposed = true;
//                    frame.dispose();
//                }
//            }
//        };
//        Timer timer = new Timer(500, task); //fire every half second
//        timer.setInitialDelay(2000);        //first delay 2 seconds
//        timer.setRepeats(false);
//        timer.start();
//    }


}
