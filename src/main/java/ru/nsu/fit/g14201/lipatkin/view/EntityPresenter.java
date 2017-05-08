package ru.nsu.fit.g14201.lipatkin.view;

import ru.nsu.fit.g14201.lipatkin.model.Entity;

import javax.swing.table.AbstractTableModel;

/**
 * Created by SPN on 08.05.2017.
 */
public class EntityPresenter {
    private Entity entity;
    private AbstractTableModel viewEntity;
    private AbstractTableModel dataEditor;

    public EntityPresenter(Entity en) {
        entity = en;

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

    public AbstractTableModel getEntityModel(TableEditorState.States state) {
        switch (state) {
            case CONSTRUCTOR:
            case DATA_EDITOR: return getDataEditor();
            default:
                case VIEW: return getViewEntity();
        }
    }

    public Entity getEntity() { return entity; }
}
