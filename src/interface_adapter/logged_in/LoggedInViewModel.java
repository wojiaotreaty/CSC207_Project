package interface_adapter.logged_in;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/*
dashboard view model
 */

public class LoggedInViewModel extends ViewModel {
    public final String TITLE_LABEL = "dashboard";

    private LoggedInState state = new LoggedInState();

    public static final String LOGOUT_BUTTON_LABEL = "Log out";
    private String loggedInUser;

    public LoggedInViewModel() {
        super("logged in");
    }

    public void setState(LoggedInState state) {
        this.state = state;
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    // This is what the Login Presenter will call to let the ViewModel know
    // to alert the View
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public LoggedInState getState() {
        return state;
    }

}
