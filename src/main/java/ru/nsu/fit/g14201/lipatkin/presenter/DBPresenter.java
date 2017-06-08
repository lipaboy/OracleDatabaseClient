package ru.nsu.fit.g14201.lipatkin.presenter;

import ru.nsu.fit.g14201.lipatkin.model.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;

import static ru.nsu.fit.g14201.lipatkin.presenter.TableEditorState.States.CONSTRUCTOR;
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
        tableEditorState.addTableEditorListener(this);

        //TODO: add listener to DBManager for entities
        entitiesPresenter = new ArrayList<>();
        for (Entity entity : dbManager.getEntities()) {
            entitiesPresenter.add(new EntityPresenter(entity, manager, tableView));
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

    /*---------------Entity Actions-----------------*/

    public void createEntity() {
        try {
            final String tableName = JOptionPane.showInputDialog("Enter the table name:");
            final Entity entity = dbManager.createEntity(tableName, "first", "number");

            entitiesPresenter.add(new EntityPresenter(entity, dbManager, tableView));
            tableList.updateUI();
            tableList.setSelectedIndex(entitiesPresenter.size() - 1);

            tableEditorState.set(TableEditorState.States.CONSTRUCTOR);
        } catch (UserWrongActionException exp) {
            JOptionPane.showMessageDialog(tableView, exp.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void removeEntity() {
        try {
            final String entityName = tableList.getSelectedValue().toString();
            if (entityName != null) {
                dbManager.removeEntity(entityName);
                entitiesPresenter.remove(tableList.getSelectedIndex());
                tableList.setSelectedIndex(0);
                tableList.updateUI();
            }
        } catch (UserWrongActionException exp) {
            JOptionPane.showMessageDialog(tableView, exp.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*---------------Column Actions-----------------*/

    public void addColumn() {
        try {
            if (tableEditorState.get() == CONSTRUCTOR) {
                EntityPresenter entityPresenter = entitiesPresenter.get(selected);
                final List<String> row = entityPresenter.getNewColumnRow();
                final Entity entity = entityPresenter.getEntity();
                final Column column = dbManager.addColumn(entity, row.get(0),
                        new SQLType(row.get(1)));

                if (row.get(2).equals("true")) {
                    dbManager.addConstraint(entity, column, new Constraint(Constraint.Type.PRIMARY_KEY));
                }
                //TODO: bad copy paste (from entity presenter)
                String reff = row.get(3);
                String[] strs = reff.split("\\.");
                if (strs.length >= 2) {
                    Entity entityPK = dbManager.getEntity(strs[0]);
                    Column columnPK = entityPK.getColumn(strs[1]);

                    Constraint constraint = new Constraint(Constraint.Type.FOREIGN_KEY);
                    constraint.setReference(new Reference(entityPK, columnPK));
                    dbManager.addConstraint(entity, column, constraint);
                }

                entityPresenter.clearNewColumnRow();
            }
        } catch (UserWrongActionException exp) {
            showErrorMessage(exp.getMessage());
        }
    }

    public void removeColumn() {
        try{
            if (tableEditorState.get() == CONSTRUCTOR) {
                EntityPresenter entityPresenter = entitiesPresenter.get(selected);
                int[] rowIndices = tableView.getSelectedRows();
                int ind = tableView.getSelectedRow();
                final Entity entity = entityPresenter.getEntity();
                dbManager.removeColumn(entity, entity.getColumn(ind));
//                for (int i = 0; i < rowIndices.length; i++) {
//                    dbManager.removeColumn(entity, entity.getColumn(rowIndices[i]));
//                }
            }
        } catch (UserWrongActionException exp) {
            showErrorMessage(exp.getMessage());
        }
    }

    /*---------------Entry Actions-----------------*/

    public void addEntry() {
        try {
            if (tableEditorState.get() == DATA_EDITOR) {
                EntityPresenter entityPresenter = entitiesPresenter.get(selected);
                List<String> row = entityPresenter.getNewRowData();
                dbManager.insert(entityPresenter.getEntity(), row);
                entityPresenter.clearNewRowData();
                //TODO: need to update table but how??? sol: maybe exec a setter
                //tableView.getRowCount();
            }
        } catch (UserWrongActionException exp) {
            JOptionPane.showMessageDialog(tableView, exp.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void removeEntry() {
        try {
            if (tableEditorState.get() == DATA_EDITOR) {
                EntityPresenter entityPresenter = entitiesPresenter.get(selected);
                int[] rowIndices = tableView.getSelectedRows();
                for (int i = 0; i < rowIndices.length; i++) {
                    dbManager.removeRow(entityPresenter.getEntity(), rowIndices[i]);
                }
                //TODO: need to update table but how??? sol: maybe exec a setter
                //tableView.getRowCount();
            }
        } catch (UserWrongActionException exp) {
            JOptionPane.showMessageDialog(tableView, exp.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void stateChanged(TableEditorState.States newState) {
        tableView.setModel(entitiesPresenter.get(selected).getEntityModel(newState));
    }

    void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(tableView, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
