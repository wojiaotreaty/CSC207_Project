package interface_adapter.delete_project;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class DeleteProjectViewModel extends ViewModel {

    private DeleteProjectState state = new DeleteProjectState();

    public DeleteProjectViewModel() {
        super("delete project");
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

    public DeleteProjectState getState() {
        return state;
    }

    public void setState(DeleteProjectState state) {
        this.state = state;
    }
}