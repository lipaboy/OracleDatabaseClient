package ru.nsu.fit.g14201.lipatkin.gui;

import ru.nsu.fit.g14201.lipatkin.model.Entity;
import ru.nsu.fit.g14201.lipatkin.model.SQLCommander;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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

        Dimension size = new Dimension(800, 500);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        setBounds((width - size.width) / 2, (height - size.height) / 2, size.width, size.height);

        /*---------------Panel components-------------------*/

        tableView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //tableView.setBounds();

        List<String> tableNames = commander.getAllEntities();
        tableList.setListData(tableNames.toArray());
            //TODO: you need to load all entities for once because it is expensive to do query every once
        tableList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selected = tableList.getSelectedIndex();
                Entity entity = commander.getAllEntries(tableList.getModel().getElementAt(selected).toString());
                //DEMO_ORDERS
                tableView.setModel(new AbstractTableModel() {
                    @Override
                    public int getRowCount() {
                        return entity.getRowCount();
                    }

                    @Override
                    public int getColumnCount() {
                        return entity.getColumnCount();
                    }

                    @Override
                    public Object getValueAt(int rowIndex, int columnIndex) {
                        return entity.get(rowIndex, columnIndex);
                    }

                    @Override
                    public String getColumnName(int index) {
                        return entity.getColumnName(index);
                    }
                });
            }
        });




    }


}
