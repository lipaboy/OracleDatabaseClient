package ru.nsu.fit.g14201.lipatkin.gui;

import ru.nsu.fit.g14201.lipatkin.model.SQLCommander;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Created by castiel on 02.05.2017.
 */
public class ObjectBrowserFrame extends JFrame{
    private JPanel panel1;
    private JList tableList;
    private JTable tableView;

    private final SQLCommander commander;

    public ObjectBrowserFrame(final SQLCommander commander1) {
        super("Object Browser");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        commander = commander1;

        Dimension size = new Dimension(500, 300);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        setBounds((width - size.width) / 2, (height - size.height) / 2, size.width, size.height);

        List<String> tableNames = commander.getAllEntities();
        tableList.setListData(tableNames.toArray());

        //DEMO_ORDERS
        Map<String, String[]> entries = commander.getAllEntries("DEMO_ORDERS");
        tableView.setModel(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return entries.values().iterator().next().length;
            }

            @Override
            public int getColumnCount() {
                return entries.keySet().size();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return entries.get(entries.keySet().toArray()[columnIndex])[rowIndex];
            }
        });

    }


}
