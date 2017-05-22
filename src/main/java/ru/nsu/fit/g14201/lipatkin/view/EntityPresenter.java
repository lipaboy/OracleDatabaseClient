package ru.nsu.fit.g14201.lipatkin.view;

import ru.nsu.fit.g14201.lipatkin.model.Column;
import ru.nsu.fit.g14201.lipatkin.model.DBManager;
import ru.nsu.fit.g14201.lipatkin.model.Entity;
import ru.nsu.fit.g14201.lipatkin.model.UpdateException;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by SPN on 08.05.2017.
 */
public class EntityPresenter {
    private Entity entity;
    private DBManager dbManager;
    private AbstractTableModel viewEntity;
    private AbstractTableModel dataEditor;
    private AbstractTableModel constructor;

    public EntityPresenter(Entity en, DBManager manager) {
        entity = en;
        dbManager = manager;

        constructor = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return entity.getColumnCount();
            }

            @Override
            public int getColumnCount() {
                return 5;
            }

            @Override
            public String getColumnName(int index) {
                //TODO: make function for get Enum of Column Type (like name, primary key or descripion)
                //TODO: by index
                switch (index) {
                    case 0: return "Column Name";
                    case 1: return "Type";
                    case 2: return "Is Primary Key";
                    case 3: return "Foreign Key";
                    case 4: return "Description";
                    default:
                }
                return "";
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Column column = entity.getColumn(rowIndex);

                switch (columnIndex) {
                    case 0: return column.getName();
                    case 1: return column.getType().toViewFormat();
                    case 2: return entity.isPrimaryKey(column);     //checkbox inside of AbstractModel or DefaultJTable
                    case 3: return "Reference";     //may be combobox
                    case 4: return "Description";
                    default:
                }
                return "";
            }

            @Override
            public Class getColumnClass(int column) {
                if (column == 2)
                    return Boolean.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) { return true; }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                try {
                    Column column = entity.getColumn(rowIndex);

                    switch (columnIndex) {
                        case 0:     dbManager.setColumnName(entity, column, aValue.toString());
                        case 1:     dbManager.setColumnType(entity, column, aValue.toString());
                        case 2:
                        case 3:
                        case 4:
                        default:
                    }
                } catch (UpdateException exp) {
                    System.out.println(exp.getMessage());
                }
            }
        };

        dataEditor = new AbstractTableModel() {
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

            @Override
            public boolean isCellEditable(int row, int column) { return true; }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                try {
                    dbManager.setValueAt(entity, rowIndex, columnIndex, aValue.toString());
                } catch (UpdateException exp) {
                    System.out.println(exp.getMessage());
                }
            }
        };

        viewEntity = new AbstractTableModel() {
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

            @Override
            public boolean isCellEditable(int row, int column) { return false; }

            //bad to switch between true and false (I don't know how do it and it is unnecessary)
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                //Only the third column
//                Editable editable = isEditable;
//                System.out.println(editable.value);
//                return editable.value;
//            }
        };
    }

    public AbstractTableModel getViewEntity() { return viewEntity; }

    public AbstractTableModel getDataEditor() { return dataEditor; }

    public AbstractTableModel getConstructor() { return constructor; }

    public AbstractTableModel getEntityModel(TableEditorState.States state) {
        switch (state) {
            case CONSTRUCTOR: return getConstructor();
            case DATA_EDITOR: return getDataEditor();
            default:
                case VIEW: return getViewEntity();
        }
    }

    public Entity getEntity() { return entity; }
}
