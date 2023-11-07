package view;

import interface_adapter.add_project.AddProjectState;
import interface_adapter.add_project.AddProjectViewModel;
import interface_adapter.add_project.AddProjectController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AddProjectView extends JPanel implements ActionListener, PropertyChangeListener {
    public final String viewName = "calendar";

    private final JTextField projectTitleInputField = new JTextField(15);
    private final JTextArea projectDetailsInputArea = new JTextArea();
    private final JTextField projectDeadlineInputField = new JTextField();
    private final AddProjectController addProjectController;
    private final AddProjectViewModel addProjectViewModel;

    //private final JButton projectPopup; //This is the button to create the popup to the add project menu
    private final JButton addProject; //This is the button to submit the fields regarding a project and start the use case


    public AddProjectView(AddProjectController controller, AddProjectViewModel signupViewModel) {

        this.addProjectController = controller;
        this.addProjectViewModel = signupViewModel;
        addProjectViewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel(AddProjectViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        LabelTextPanel projectTitleInfo = new LabelTextPanel(
                new JLabel(AddProjectViewModel.PROJECT_TITLE_LABEL), projectTitleInputField);
        LabelTextPanel projectDetailsInfo = new LabelTextPanel(
                new JLabel(AddProjectViewModel.PROJECT_DETAILS_LABEL), projectDetailsInputArea);
        LabelTextPanel projectDeadlineInfo = new LabelTextPanel(
                new JLabel(AddProjectViewModel.PROJECT_DEADLINE_LABEL), projectDeadlineInputField);

        JPanel buttons = new JPanel();
        addProject = new JButton(AddProjectViewModel.ADD_PROJECT_BUTTON_LABEL);
        buttons.add(addProject);

        addProject.addActionListener(
                // This creates an anonymous subclass of ActionListener and instantiates it.
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(addProject)) {
                            AddProjectState currentState = addProjectViewModel.getState();

                            addProjectController.execute(
                                    currentState.getProjectTitle(),
                                    currentState.getProjectDetails(),
                                    currentState.getProjectDeadline()
                            );
                        }
                    }
                }
        );
        projectTitleInputField.addKeyListener(
                new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        AddProjectState currentState = addProjectViewModel.getState();
                        String text = projectTitleInputField.getText() + e.getKeyChar();
                        currentState.setProjectTitle(text);
                        addProjectViewModel.setState(currentState);
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                    }
                });

        projectDetailsInputArea.addKeyListener(
                new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        AddProjectState currentState = addProjectViewModel.getState();
                        currentState.setProjectDetails(projectDetailsInputArea.getText() + e.getKeyChar());
                        addProjectViewModel.setState(currentState);
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {

                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                }
        );

        projectDeadlineInputField.addKeyListener(
                new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        AddProjectState currentState = addProjectViewModel.getState();
                        currentState.setProjectDeadline(projectDeadlineInputField.getText() + e.getKeyChar());
                        addProjectViewModel.setState(currentState);
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {

                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                }
        );


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(title);
        this.add(projectTitleInfo);
        this.add(projectDetailsInfo);
        this.add(projectDeadlineInfo);
        this.add(buttons);
    }

    /**
     * React to a button click that results in evt.
     */
    public void actionPerformed(ActionEvent evt) {
        JOptionPane.showConfirmDialog(this, "Cancel not implemented yet.");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        AddProjectState state = (AddProjectState) evt.getNewValue();
    }
}