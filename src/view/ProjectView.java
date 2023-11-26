package view;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

public class ProjectView extends JFrame implements PropertyChangeListener {
    private final ProjectViewModel projectViewModel;
    private JPanel projectpanel;
    private ProjectInformation project;

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
        JLabel l = new JLabel();
        l.setText(project.getProjectTitle());
        JTextArea textArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(popupPanel);
        textArea.setEditable(false);
        textArea.append(project.getProjectDescritpion() + "\n");
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
            if (taskStatus[0] == "1") {
                status = new JCheckBox("Status", true);
            } else {
                status = new JCheckBox("Status");
            }

            JCheckBox finalStatus = status;
            ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if( finalStatus.getModel().isSelected() ) {
                    taskStatus[0] = "1";
                }
                else{taskStatus[0] = "0";}
            }
        };
        status.addActionListener(actionListener);
    }
       }

        JButton refactorButton = new JButton("Refactor Project");

    }
}