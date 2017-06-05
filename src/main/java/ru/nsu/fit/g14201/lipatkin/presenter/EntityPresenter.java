package ru.nsu.fit.g14201.lipatkin.presenter;

import ru.nsu.fit.g14201.lipatkin.model.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SPN on 08.05.2017.
 */
class EntityPresenter implements EntityListener {
    private Entity entity;
    private DBManager dbManager;
    private DBPresenter dbPresenter;
    private AbstractTableModel viewEntity;
    private AbstractTableModel dataEditor;
    private AbstractTableModel constructor;

    private List<String> newRow;

    public EntityPresenter(Entity en, DBManager manager, DBPresenter presenter) {
        entity = en;
        dbManager = manager;
        dbPresenter = presenter;        //TODO: remove dependency
        newRow = new ArrayList<>();
        en.addEntityListener(this);

        for (int i = 0; i < en.getColumnCount(); i++)
            newRow.add("");

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
                    case 3: return "Reference To";
                    case 4: return "Not Null";
                    default:
                }
                return "";
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Column column = entity.getColumn(rowIndex);

                switch (columnIndex) {
                    case 0: return column.getName();
                    case 1: return column.getType().toSQLFormat();
                    case 2: return entity.isPrimaryKey(column);     //checkbox inside of AbstractModel or DefaultJTable
                    case 3: return "Reference To";     //may be combobox
                    case 4: return false;
                    default:
                }
                return "";
            }

            @Override
            public Class getColumnClass(int column) {
                if (column == 2 || column == 4)
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
                } catch (UserWrongActionException exp) {
                    dbPresenter.showErrorMessage(exp.getMessage());
                }
            }
        };

        dataEditor = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return entity.getRowCount() + 1;
            }

            @Override
            public int getColumnCount() {
                return entity.getColumnCount();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return ((rowIndex == entity.getRowCount()) ? newRow.get(columnIndex) :
                        entity.get(rowIndex, columnIndex));
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
                    if (rowIndex < entity.getRowCount())
                        dbManager.setValueAt(entity, rowIndex, columnIndex, aValue.toString());
                    else
                        newRow.set(columnIndex, aValue.toString());
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
        };

    }

    void clearNewRows() {
        for (int i = 0; i < newRow.size(); i++)
            newRow.set(i, "");
    }

    /*-------------------Getters--------------------------*/

    public final List<String> getNewRow() { return newRow; }

    //public int getNewEntryFirstPosition() { return dataEditor.getRowCount() - 1; }

    public final AbstractTableModel getViewEntity() { return viewEntity; }

    public final AbstractTableModel getDataEditor() { return dataEditor; }

    public final AbstractTableModel getConstructor() { return constructor; }

    public final AbstractTableModel getEntityModel(TableEditorState.States state) {
        switch (state) {
            case CONSTRUCTOR: return getConstructor();
            case DATA_EDITOR: return getDataEditor();
            default:
                case VIEW: return getViewEntity();
        }
    }

    public final Entity getEntity() { return entity; }

    @Override
    public void dataChanged() {
        viewEntity.fireTableDataChanged();
        dataEditor.fireTableDataChanged();
        constructor.fireTableDataChanged();
    }
}
