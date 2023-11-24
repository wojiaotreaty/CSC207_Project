package view;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SignupView extends JPanel implements ActionListener, PropertyChangeListener{

    public final String viewName = "Login Page";

    private final SignupViewModel signInLogInViewModel;
    private final JTextField usernameInputField = new JTextField(15);
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(15);
    private final SignupController signInLogInController;
    private final JButton login;

    public SignupView(SignupController controller, SignupViewModel viewModel) {

        this.signInLogInController = controller;
        this.signInLogInViewModel = viewModel;
        viewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel(SignupViewModel.TITLE_LABEL);

        JPanel buttons = new JPanel();
        login = new JButton(SignupViewModel.SIGNUP_BUTTON_LABEL);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
