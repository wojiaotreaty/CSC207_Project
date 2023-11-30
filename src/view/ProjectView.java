package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.ArrayList;

import interface_adapter.ProjectState;
import interface_adapter.ProjectViewModel;
import interface_adapter.RefactorProjectController;
import use_case.RefactorProject.RefactorProjectInputData;


public class ProjectView extends JFrame implements PropertyChangeListener {
    private final ProjectViewModel projectViewModel;
    private String projectDescription;
    private ArrayList<String> tasks;
    private String projectTitle;
    private String projectID;
    private RefactorProjectController refactorProjectController;
    public ProjectView(ProjectViewModel projectViewModel, RefactorProjectController refactorProjectController) {
        this.projectViewModel = projectViewModel;

       ProjectState projectState = projectViewModel.getState();

        projectViewModel.addPropertyChangeListener(this);

        this.tasks = projectState.getTasks();
        this.projectDescription=projectState.getProjectDescription();
        this.projectTitle=projectState.getProjectTitle();
        this.projectID=projectState.getProjectID();
        this.refactorProjectController=refactorProjectController;
    }
    private void ProjectPopup() {
        JFrame popupFrame = new JFrame("Project");
        popupFrame.setSize(400, 250);
        JPanel popupPanel =new JPanel();
        popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(popupPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
        // The project title
        JLabel Title = new JLabel("Project Name");
        JTextField title = new JTextField(this.projectTitle);
        title.setEditable(false);
        popupPanel.add(Title);
        popupPanel.add(title);
        // The project description
        JLabel description = new JLabel("Project Description");
        JTextArea textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        textArea.append(this.projectDescription + "\n");
        // Creating spacing for the rest of the popup panel
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        popupPanel.add(textArea);
        popupPanel.add(description);
        // The list of tasks along with their deadlines and descriptions and status
        ArrayList<String> tasks = this.tasks;
        int i = 0;
        for (String task : tasks) {
            StringBuilder mutableTask = new StringBuilder();
            mutableTask = mutableTask.append(task);
            String[] arrOfStr = task.split("|uwu|");
            String taskName = arrOfStr[0];
            String taskDescription = arrOfStr[1];
            String taskDeadline = arrOfStr[2];
            String taskStatus = arrOfStr[3];
            JLabel tName = new JLabel("Task Name");
            JTextField name = new JTextField(taskName);
            name.setEditable(false);
            // Adding the taskName label and the taskName textField into one panel
            JPanel panel1 = new JPanel(new GridLayout(2, 1));
            panel1.add(tName);
            panel1.add(name);
            JLabel tDeadline = new JLabel("Task Deadline");
            JTextField deadline = new JTextField(taskDeadline);
            deadline.setEditable(false);
            // Adding the deadline label and the deadline text field into one panel
            JPanel panel2 = new JPanel(new GridLayout(2, 1));
            panel1.add(tDeadline);
            panel1.add(deadline);
            // Create a check-box which is always checked if the task Status is true
            JCheckBox status = new JCheckBox("status");
            if (taskStatus.equals("true")) {
                status.setSelected(true);
                status.setEnabled(false);
            } else {
                status.setSelected(false);
            }
            // Adding the name,deadline,status of the task in a flowLayout from left to right
            JPanel labelsPanel = new JPanel(new FlowLayout());
            labelsPanel.add(panel1);
            labelsPanel.add(panel2);
            labelsPanel.add(status);
            popupPanel.add(labelsPanel);
            // The Task Description area
            JTextArea tDescription = new JTextArea(5, 20);
            tDescription.append(taskDescription + "\n");
            tDescription.setEditable(false);
            popupPanel.add(tDescription);
            // Action Listener for the check-box status to notify the backend when the user marks
            // a task as completed.
            int finalI = i;
            status.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (status.isSelected()) {
                        //TODO: Call to Braedon's constructor
                        projectViewModel.getState().setTaskStatus(finalI, 1);
                    }
                }
            });
            i = i + 1;
        }


        JButton refactorButton = new JButton("Refactor Project");
        refactorButton.addActionListener(e -> {
            refactorProjectController.execute(projectID);
            popupFrame.dispose();
        });
        popupFrame.add(popupPanel);
        popupFrame.setVisible(true);
    }
    public void propertyChange(PropertyChangeEvent evt) {
        ProjectState state = (ProjectState) evt.getNewValue();

        if (state.getRefactorProjectError() != null) {
            JOptionPane.showMessageDialog(this, state.getRefactorProjectError());
            state.setRefactorProjectError(null);
        }
        ProjectPopup();
    }

}
