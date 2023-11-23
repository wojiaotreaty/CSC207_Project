package view;

import interface_adapter.SignInLogIn.SignInLogInController;
import interface_adapter.SignInLogIn.SignInLogInViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
public class SignInLogInView extends JPanel implements ActionListener, PropertyChangeListener{

    public final String viewName = "Login Page";

    private final SignInLogInViewModel signInLogInViewModel;
    private final JTextField usernameInputField = new JTextField(15);
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(15);
    private final SignInLogInController signInLogInController;
    private final JButton login;

    public SignInLogInView(SignInLogInController controller, SignInLogInViewModel viewModel) {

        this.signInLogInController = controller;
        this.signInLogInViewModel = viewModel;
        viewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel(SignInLogInViewModel.TITLE_LABEL);

        JPanel buttons = new JPanel();
        login = new JButton(SignInLogInViewModel.SIGNUP_BUTTON_LABEL);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
