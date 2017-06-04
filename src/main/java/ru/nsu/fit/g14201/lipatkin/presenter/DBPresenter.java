package ru.nsu.fit.g14201.lipatkin.presenter;

import ru.nsu.fit.g14201.lipatkin.model.DBManager;
import ru.nsu.fit.g14201.lipatkin.model.Entity;
import ru.nsu.fit.g14201.lipatkin.model.UpdateException;

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
        tableEditorState.add(this);

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
                tableView.setModel(entity.getEntityModel(tableEditorState.get()));
            }
        });
    }

    /*---------------Actions-----------------*/

    public void addEntry() {
        try {
            if (tableEditorState.get() == DATA_EDITOR) {
                EntityPresenter entityPresenter = entitiesPresenter.get(selected);
                List<String> row = entityPresenter.getNewRow();
                dbManager.insert(entityPresenter.getEntity(), row);
                entityPresenter.clearNewRows();
                //TODO: need to update table but how??? sol: maybe exec a setter
                //tableView.getRowCount();
            }
        } catch (UpdateException exp) {
            System.out.println(exp.getMessage());
        }
    }

    public void removeEntry() {
        try {
            if (tableEditorState.get() == DATA_EDITOR) {
                EntityPresenter entityPresenter = entitiesPresenter.get(selected);
                int[] rowIndices = tableView.getSelectedRows();
                //TODO: need to update table but how??? sol: maybe exec a setter
                //tableView.getRowCount();
            }
        } catch (UpdateException exp) {
            System.out.println(exp.getMessage());
        }
    }

    @Override
    public void stateChanged(TableEditorState.States newState) {
        tableView.setModel(entitiesPresenter.get(selected).getEntityModel(newState));
    }
}
