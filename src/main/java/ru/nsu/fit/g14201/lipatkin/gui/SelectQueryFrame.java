package ru.nsu.fit.g14201.lipatkin.gui;

import ru.nsu.fit.g14201.lipatkin.model.DBManager;
import ru.nsu.fit.g14201.lipatkin.model.Entity;
import ru.nsu.fit.g14201.lipatkin.presenter.EntityPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by castiel on 08.06.2017.
 */
public class SelectQueryFrame extends JFrame {
    private JTextArea queryTextArea;
    private JTable resultTable;
    private JButton runButton;
    private JPanel panel1;

    private DBManager dbManager;
    private EntityPresenter entityPresenter;

    public SelectQueryFrame(DBManager dbManager1) {
        super("Query Runner");
        dbManager = dbManager1;
        setContentPane(panel1);

        Dimension size = new Dimension(600, 500);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        setBounds((width - size.width) / 2, (height - size.height) / 2, size.width, size.height);

        entityPresenter = new EntityPresenter(new Entity("Empty"), dbManager1, resultTable);
        resultTable.setModel(entityPresenter.getViewEntity());
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Entity entity = dbManager.executeSelectQuery(queryTextArea.getText());
                entityPresenter.setEntity(entity);
                resultTable.updateUI();
                entityPresenter.dataChanged();
            }
        });
    }
}
