package ru.nsu.fit.g14201.lipatkin.view;

import ru.nsu.fit.g14201.lipatkin.model.Entity;

import javax.swing.table.AbstractTableModel;

/**
 * Created by SPN on 08.05.2017.
 */
public class EntityPresenter {
    private Entity entity;
    private AbstractTableModel viewEntity;

    EntityPresenter(Entity en) {
        entity = en;
    }
}
