package interface_adapter.dashboard;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class DashboardViewModel extends ViewModel {
  
    private final String viewName = "Project Dashboard";

    private DashboardState state = new DashboardState();

    public DashboardViewModel() {
        super("Project Dashboard");
    }

    public void setState(DashboardState state) {
        this.state = state;
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public DashboardState getState() {
        return state;
    }
    public String getViewName() {
        return this.viewName;
    }
}
