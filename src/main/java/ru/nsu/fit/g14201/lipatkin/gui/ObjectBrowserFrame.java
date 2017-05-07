package ru.nsu.fit.g14201.lipatkin.gui;

import ru.nsu.fit.g14201.lipatkin.model.SQLCommander;
import ru.nsu.fit.g14201.lipatkin.view.DBPresenter;

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
    private JButton entryEditingButton;
    private JButton tableEditingButton;
    private JButton viewButton;

    private final SQLCommander commander;

    public ObjectBrowserFrame(final SQLCommander sqlCommander) {
        super("Object Browser");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        commander = sqlCommander;

        Dimension size = new Dimension(800, 500);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        setBounds((width - size.width) / 2, (height - size.height) / 2, size.width, size.height);

        /*---------------Panel components-------------------*/

        tableView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


        DBPresenter dbPresenter = new DBPresenter(sqlCommander, tableList, tableView);

        /*-----------------Menu bar-------------------------*/

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JFrame currentFrame = this;
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentFrame.dispose();
            }
        });
        fileMenu.add(quitItem);

        this.setJMenuBar(menuBar);


    }


}
