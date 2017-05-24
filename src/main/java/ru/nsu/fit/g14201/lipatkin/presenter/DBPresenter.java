package ru.nsu.fit.g14201.lipatkin.presenter;

import ru.nsu.fit.g14201.lipatkin.model.DBManager;
import ru.nsu.fit.g14201.lipatkin.model.Entity;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

import static ru.nsu.fit.g14201.lipatkin.presenter.TableEditorState.States.DATA_EDITOR;

/**
 * Created by SPN on 07.05.2017.
 */
public class DBPresenter implements EditorStateChangedListener {
    private JList tableList;
    private DBManager dbManager;
    private JTable tableView;
    private List<EntityPresenter> entitiesPresenter;
    private int selected;   //current table
    private TableEditorState tableEditorState;

    public DBPresenter(DBManager manager, JList list, JTable table, TableEditorState editorState) {
        tableList = list;
        tableView = table;
        dbManager = manager;
        tableEditorState = editorState;

        entitiesPresenter = new ArrayList<>();
        for (Entity entity : dbManager.getEntities()) {
            entitiesPresenter.add(new EntityPresenter(entity, manager));
        }

        tableList.setModel(new AbstractListModel() {
            @Override
            public int getSize() {
                return entitiesPresenter.size();
            }

            @Override
            public Object getElementAt(int index) {
                return entitiesPresenter.get(index).getEntity().getName();
            }
        });

        tableList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selected = tableList.getSelectedIndex();
                EntityPresenter entity = entitiesPresenter.get(selected);
                //TODO: I think it is bad that after every selection it will created new AbstractTableModel
                //TODO: One of some solutions: for each table create one TableModel
                tableView.setModel(entity.getEntityModel(tableEditorState.get()));
            }
        });
    }

    //Action
    public void addEntry() {
        if (tableEditorState.get() == DATA_EDITOR) {
//            int rows = tableView.getRowCount();
//            DefaultTableModel table = (DefaultTableModel)tableView.getModel();
//            table.addRow(new Object[rows]);
        }
    }

    @Override
    public void stateChanged(TableEditorState.States newState) {
        tableView.setModel(entitiesPresenter.get(selected).getEntityModel(newState));
    }
}
