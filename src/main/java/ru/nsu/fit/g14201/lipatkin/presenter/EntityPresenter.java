package ru.nsu.fit.g14201.lipatkin.presenter;

import ru.nsu.fit.g14201.lipatkin.model.*;

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
    private final int constructorCols = 4;
    private AbstractTableModel constructor;

    private List<String> newRowData;
    private List<String> newColumnRow;

    public EntityPresenter(Entity en, DBManager manager, DBPresenter presenter) {
        entity = en;
        dbManager = manager;
        dbPresenter = presenter;        //TODO: remove dependency
        newRowData = new ArrayList<>();
        newColumnRow = new ArrayList<>();
        en.addEntityListener(this);

        for (int i = 0; i < en.getColumnCount(); i++) {
            newRowData.add("");
        }
        for (int i = 0; i < constructorCols; i++) {
            newColumnRow.add("");
        }

        constructor = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return entity.getColumnCount()
                                + 1;        // new row for add column
            }

            @Override
            public int getColumnCount() {
                return constructorCols;
            }

            @Override
            public String getColumnName(int index) {
                //TODO: make function for get Enum of Column Type (like name, primary key or descripion)
                //TODO: by index (to exclude dependency of order)
                switch (index) {
                    case 0: return "Column Name";
                    case 1: return "Type";
                    case 2: return "Is Primary Key";
                    case 3: return "Reference To";
                    //TODO: add not null
                    case 4: return "Not Null";
                    default:
                }
                return "";
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (rowIndex < entity.getColumnCount()) {
                    Column column = entity.getColumn(rowIndex);

                    switch (columnIndex) {
                        case 0:
                            return column.getName();
                        case 1:
                            return column.getType().getSQLFormat();
                        case 2:
                            return entity.isPrimaryKey(column);     //checkbox inside of AbstractModel or DefaultJTable
                        case 3:
                            final Constraint con = column.getConstraint(Constraint.Type.FOREIGN_KEY);
                            if (null == con)
                                return "";
                            final Reference ref = con.getReference();
                            return ((ref == null) ? "" : ref.getViewFormat());     //may be combobox
                        case 4:
                            return false;
                        default:
                    }
                } else {
                    switch (columnIndex) {
                        case 2:
                            return (newColumnRow.get(2).equals("true"));
                        default:
                            return newColumnRow.get(columnIndex);
                    }
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
                    if (rowIndex < entity.getColumnCount()) {
                        Column column = entity.getColumn(rowIndex);

                        switch (columnIndex) {
                            case 0:
                                dbManager.setColumnName(entity, column, aValue.toString());
                                break;
                            case 1:
                                dbManager.setColumnType(entity, column, aValue.toString());
                                break;
                            case 2:
                                boolean isPrim = (boolean) aValue;
                                if (isPrim)
                                    dbManager.addConstraint(entity, column,
                                            new Constraint(Constraint.Type.PRIMARY_KEY));
                                else
                                    dbManager.removeConstraint(entity, column,
                                            new Constraint(Constraint.Type.PRIMARY_KEY));
                                break;
                            case 3:
                                String reff = aValue.toString();
                                String[] strs = reff.split("\\.");
                                if (strs.length >= 2) {
                                    Entity entityPK = dbManager.getEntity(strs[0]);
                                    Column columnPK = entityPK.getColumn(strs[1]);

                                    Constraint constraint = new Constraint(Constraint.Type.FOREIGN_KEY);
                                    constraint.setReference(new Reference(entityPK, columnPK));
                                    dbManager.addConstraint(entity, column, constraint);
                                } else {
                                    dbManager.removeConstraint(entity, column,
                                            column.getConstraint(Constraint.Type.FOREIGN_KEY));
                                }
                            case 4:
                            default:
                        }
                    }
                    else {
                        switch (columnIndex) {
                            case 2: if ((boolean)aValue)
                                newColumnRow.set(columnIndex, "true");
                            else
                                newColumnRow.set(columnIndex, "");
                            default: newColumnRow.set(columnIndex, aValue.toString());
                        }
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
                return ((rowIndex >= entity.getRowCount()) ? newRowData.get(columnIndex) :
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
                        newRowData.set(columnIndex, aValue.toString());
                } catch (UserWrongActionException exp) {
                    dbPresenter.showErrorMessage(exp.getMessage());
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

    void clearNewRowData() {
        for (int i = 0; i < newRowData.size(); i++)
            newRowData.set(i, "");
    }

    void clearNewColumnRow() {
        for (int i = 0; i < newColumnRow.size(); i++)
            newColumnRow.set(i, "");
    }

    /*-------------------Getters--------------------------*/

    public final List<String> getNewRowData() { return newRowData; }

    public final List<String> getNewColumnRow() { return newColumnRow; }

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
        if (newRowData.size() < entity.getColumnCount())
            for (int i = newRowData.size(); i < entity.getColumnCount(); i++)
                newRowData.add("");
    }
}
