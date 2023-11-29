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

        JPanel popupPanel = new JPanel(new GridLayout(4, 2));
        // The project title
        JTextField Title = new JTextField(project.getProjectName());
        popupFrame.add(Title);
        // The project description
        JTextArea textArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(popupPanel);
        textArea.setEditable(false);
        textArea.append(project.getProjectDescritpion() + "\n");
        // The list of tasks along with their deadlines and descriptions and status
        JCheckBox status = null;
        for (String task : project.getTasks()) {
            String[] arrOfStr = task.split("|uwu|");

            String taskName = arrOfStr[0];
            String taskDescription = arrOfStr[1];
            String taskDeadline = arrOfStr[2];
            String[] taskStatus = {arrOfStr[3]};
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
