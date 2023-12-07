package interface_adapter.refactor_project;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class RefactorProjectViewModel extends ViewModel {

    private RefactorProjectState state = new RefactorProjectState();

    public RefactorProjectViewModel() {
        super("refactor project");
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    // This is what the Signup Presenter will call to let the ViewModel know
    // to alert the View
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public RefactorProjectState getState() {
        return state;
    }

    public void setState(RefactorProjectState state) {
        this.state = state;
    }
}

