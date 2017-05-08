package ru.nsu.fit.g14201.lipatkin.view;

import ru.nsu.fit.g14201.lipatkin.model.Entity;

import javax.swing.table.AbstractTableModel;

/**
 * Created by SPN on 08.05.2017.
 */
public class EntityPresenter {
    private Entity entity;
    private AbstractTableModel viewEntity;

    public EntityPresenter(Entity en) {
        entity = en;
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
        };
    }

    public AbstractTableModel getViewEntity() { return viewEntity; }

    public Entity getEntity() { return entity; }
}
