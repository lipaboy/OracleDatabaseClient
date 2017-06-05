package ru.nsu.fit.g14201.lipatkin.model;

/**
 * Created by RARETA on 05.06.2017.
 */
public class Reference {
    private Entity entity;
    private Column column;

    public Reference(Entity entity1, Column column1) {
        entity = entity1;
        column = column1;
    }

    public final Entity getEntity() { return entity; }
    public final Column getColumn() { return column; }
}
