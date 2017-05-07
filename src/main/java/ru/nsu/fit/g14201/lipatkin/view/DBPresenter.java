package ru.nsu.fit.g14201.lipatkin.view;

import ru.nsu.fit.g14201.lipatkin.model.Entity;
import ru.nsu.fit.g14201.lipatkin.model.SQLCommander;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by SPN on 07.05.2017.
 */
public class DBPresenter {
    private JList tableList;
    private SQLCommander commander;
    private JTable tableView;
    private List<Entity> entities;

    public DBPresenter(SQLCommander sqlCommander, JList list, JTable table) {
        tableList = list;
        commander = sqlCommander;
        tableView = table;

        entities = commander.getAllEntities();

        tableList.setModel(new AbstractListModel() {
            @Override
            public int getSize() {
                return entities.size();
            }

            @Override
            public Object getElementAt(int index) {
                return entities.get(index).getName();
            }
        });

        tableList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selected = tableList.getSelectedIndex();
                Entity entity = entities.get(selected);
                //TODO: I think it is bad that after every selection it will created new AbstractTableModel
                //TODO: One of some solutions: for each table create one TableModel
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
