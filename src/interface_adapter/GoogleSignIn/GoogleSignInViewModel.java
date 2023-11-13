package interface_adapter.GoogleSignIn;
import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GoogleSignInViewModel extends ViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public GoogleSignInViewModel(String viewName) {
        super("GoogleSignIn");
    }

    @Override
    public void firePropertyChanged() {

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }
}
