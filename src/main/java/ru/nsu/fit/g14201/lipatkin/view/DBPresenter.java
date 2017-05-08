package ru.nsu.fit.g14201.lipatkin.view;

import javafx.scene.control.Tab;
import ru.nsu.fit.g14201.lipatkin.model.DBManager;
import ru.nsu.fit.g14201.lipatkin.model.Entity;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SPN on 07.05.2017.
 */
public class DBPresenter implements EditorStateChangedListener {
    private JList tableList;
    private DBManager dbManager;
    private JTable tableView;
    private List<EntityPresenter> entitiesPresenter;
    private int selected;
    private TableEditorState tableEditorState;

    public DBPresenter(DBManager manager, JList list, JTable table, TableEditorState editorState) {
        tableList = list;
        tableView = table;
        dbManager = manager;
        tableEditorState = editorState;

        entitiesPresenter = new ArrayList<>();
        for (Entity entity : dbManager.getEntities()) {
            entitiesPresenter.add(new EntityPresenter(entity));
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

    @Override
    public void stateChanged(TableEditorState.States newState) {
        tableView.setModel(entitiesPresenter.get(selected).getEntityModel(newState));
    }
}
