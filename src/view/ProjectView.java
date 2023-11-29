package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
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
            // Create a check box which is always checked if the task Status is 1
            if (taskStatus.equals("true")){
                JCheckBox status = new JCheckBox("status",true);
            }
            else{
                JCheckBox status = new JCheckBox("status",true);
            }

            JPanel labelsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            labelsPanel.add(label1);
            labelsPanel.add(label2);

            mainPanel.add(labelsPanel, BorderLayout.SOUTH);
            JTextArea taskdescription = new JTextArea(5, 20);
            textArea.setEditable(false);
            textArea.append(taskName + "       " + "deadline:" + taskDeadline + "\n" + taskDescription + "\n");
            status = new JCheckBox();
            popupPanel.add(status);
            popupPanel.add(taskdescription);
            popupPanel.add(textArea);
            popupPanel.add(l);
            if (taskStatus[0] == "1") {
                status = new JCheckBox("Status", true);
            } else {
                status = new JCheckBox("Status");
            }

            JCheckBox finalStatus = status;
            ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    if (finalStatus.getModel().isSelected()) {
                        taskStatus[0] = "1";
                    } else {
                        taskStatus[0] = "0";
                    }
                }
            };
            status.addActionListener(actionListener);
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

    ;

    public void propertyChange(PropertyChangeEvent evt) {
        RefactorProjectState state = (RefactorprojectState) evt.getNewValue();

        if (state.getrefactorProjectError() != null) {
            JOptionPane.showMessageDialog(this, state.getAddProjectError());
            state.setrefactorProjectError(null);
        }

    }
}
