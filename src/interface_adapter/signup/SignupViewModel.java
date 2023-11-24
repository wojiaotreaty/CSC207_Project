package interface_adapter.signup;
import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SignupViewModel extends ViewModel {

    public static final String TITLE_LABEL = "Sign in View";
    public static final String USERNAME_LABEL = "Choose username";
    public static final String PASSWORD_LABEL = "Choose password";
    public static final String REPEAT_PASSWORD_LABEL = "Enter password again";
    public static final String SIGNUP_BUTTON_LABEL = "Sign Up";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public SignupViewModel() {
        super("Sign up");
    }

    @Override
    public void firePropertyChanged() {

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }
}
