package ru.nsu.fit.g14201.lipatkin.view;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SPN on 08.05.2017.
 */
public class TableEditorState {
    public enum States {
        VIEW,
        DATA_EDITOR,
        CONSTRUCTOR
    }
    private States currentState;
    private List<EditorStateChangedListener> listeners;

    {
        listeners = new ArrayList<>();
    }

    public TableEditorState(States curr) {
        currentState = curr;
    }

    public void add(EditorStateChangedListener listener) { listeners.add(listener); }

    public void remove(EditorStateChangedListener listener) { listeners.remove(listener); }

    public void set(States state) {
        if (state != currentState) {
            currentState = state;
            for (EditorStateChangedListener listener : listeners) {
                listener.stateChanged(state);
            }
        }
    }

    public States get() { return currentState; }
}
