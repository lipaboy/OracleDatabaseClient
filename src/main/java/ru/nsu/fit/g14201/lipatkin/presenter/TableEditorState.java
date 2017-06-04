package ru.nsu.fit.g14201.lipatkin.presenter;

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

    public TableEditorState() {}

    public void addTableEditorListener(EditorStateChangedListener listener) { listeners.add(listener); }

    public void removeTableEditorListener(EditorStateChangedListener listener) { listeners.remove(listener); }

    public void set(States state) {
        if (state != currentState) {
            currentState = state;
            notifyListeners();
        }
    }

    void notifyListeners() {
        for (EditorStateChangedListener listener : listeners) {
            listener.stateChanged(currentState);
        }
    }

    public States get() { return currentState; }
}
