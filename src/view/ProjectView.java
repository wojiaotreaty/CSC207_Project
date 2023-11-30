package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

import static java.lang.StringUTF16.lastIndexOf;

public class ProjectView extends JFrame implements PropertyChangeListener {
    private final ProjectViewModel projectViewModel;
    private JPanel projectpanel;
    private ProjectData project;
    public ProjectView(ProjectViewModel projectViewModel, RefactorProjectController refactorProjectController) {
        this.projectViewModel = projectViewModel;

        ProjectState projectState = projectViewModel.getState();

        projectViewModel.addPropertyChangeListener(this);

        project = projectState.getProject();
    }
    private void ProjectPopup() throws ParseException {
        JFrame popupFrame = new JFrame("Project");
        popupFrame.setSize(400, 250);
        JPanel popupPanel = new JPanel(new BoxLayout(popupPanel, BoxLayout.Y_AXIS));
        // The project title
        JLabel Title=new JLabel("Project Name");
        JTextField title = new JTextField(project.getProjectName());
        title.setEditable(false);
        popupPanel.add(Title);
        popupPanel.add(title);
        // The project description
        JLabel description=new JLabel("Project Description");
        JTextArea textArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(popupPanel);
        textArea.setEditable(false);
        textArea.append(project.getProjectDescritpion() + "\n");
        // Creating spacing for the rest of the popup panel
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        popupPanel.add(textArea);
        popupPanel.add(description);
        // The list of tasks along with their deadlines and descriptions and status
        for (String task : project.getTasks()) {
            StringBuilder mutableTask=new StringBuilder();
            mutableTask = mutableTask.append(task);
            String[] arrOfStr = task.split("|uwu|");
            String taskName = arrOfStr[0];
            String taskDescription = arrOfStr[1];
            String taskDeadline = arrOfStr[2];
            String taskStatus = arrOfStr[3];
            JLabel tName = new JLabel("Task Name");
            JTextField name=new JTextField(taskName);
            name.setEditable(false);
            // Adding the taskName label and the taskName textField into one panel
            JPanel panel1= new JPanel(new GridLayout(2,1));
            panel1.add(tName);
            panel1.add(name);
            JLabel tDeadline= new JLabel("Task Deadline");
            JTextField deadline =new JTextField(taskDeadline);
            deadline.setEditable(false);
            // Adding the deadline label and the deadline text field into one panel
            JPanel panel2= new JPanel(new GridLayout(2,1));
            panel1.add(tDeadline);
            panel1.add(deadline);
            // Create a check-box which is always checked if the task Status is true
            JCheckBox status = new JCheckBox("status");
            if (taskStatus.equals("true")){
               status.setSelected(true);
               status.setEnabled(false);
            }
            else{
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
            StringBuilder finalMutableTask = mutableTask;
            status.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (status.isSelected()){
                        //TODO:Make a change to the DAO and a call to Dashboard View's interactor
                        finalMutableTask.setCharAt(finalMutableTask.length()-1,'1');
                    }
                }
            });
            status.addActionListener(ActionListener);
        }


        JButton refactorButton = new JButton("Refactor Project");
        refactorButton.addActionListener(e -> {
            String projectName = project.getProjectTitle();
            String projectDescription = project.getProjectDescription();
            String[] tasks = project.getTasks();

            refactorProjectController.execute(
                    projectName,
                    projectDescription,
                    tasks
            );

            popupFrame.dispose();
        };
        popupFrame.add(popupPanel);
        popupFrame.setVisible(true);
                )}


    public void propertyChange(PropertyChangeEvent evt) {
        RefactorProjectState state = (RefactorprojectState) evt.getNewValue();

        if (state.getrefactorProjectError() != null) {
            JOptionPane.showMessageDialog(this, state.getAddProjectError());
            state.setrefactorProjectError(null);
        }

    }
}
