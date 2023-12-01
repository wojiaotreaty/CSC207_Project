package view;

import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.dashboard.ProjectData;
import interface_adapter.add_project.AddProjectController;
import interface_adapter.send_notification.NotificationController;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.text.MaskFormatter;

public class DashboardView extends JFrame implements PropertyChangeListener {
    private final DashboardViewModel dashboardViewModel;
    private JPanel dashboardPanel;
    private ArrayList<ProjectData> projectsList;
    private final ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture = null;

    // ***Added notificationController to the constructor.
    public DashboardView(DashboardViewModel dashboardViewModel, AddProjectController addProjectController,
                         NotificationController notificationController) {

        this.dashboardViewModel = dashboardViewModel;

        DashboardState dashboardState = dashboardViewModel.getState();

        dashboardViewModel.addPropertyChangeListener(this);

        projectsList = dashboardState.getProjects();

        displayAllProjects();

        setTitle("Project Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(dashboardPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);

        updateEmptyDashboardLabel();

        JButton addProjectButton = new JButton("Add Project");
        // ***Created toggleNotifications button
        JButton toggleNotifications = new JButton("Notifications Off");
        addProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ***Added check for addProjectButton
                if (e.getSource() == addProjectButton) {
                    try {
                        showAddProjectPopup();
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        // ***Added actionListener to toggleNotification button
        toggleNotifications.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == toggleNotifications) {
                    // If notifications are turned off, start them up again using scheduleAtFixedRate
                    if (scheduledFuture == null) {
                        Runnable sendNotification = () -> notificationController.execute(LocalDate.now(), dashboardViewModel.getState().getUsername());
                        scheduledFuture = schedule.scheduleAtFixedRate(sendNotification, 0, 24, TimeUnit.HOURS);
                        toggleNotifications.setText("Notifications Off");
                    }
                    // Else, stop the scheduled notifications.
                    else {
                        scheduledFuture.cancel(true);
                        scheduledFuture = null;
                        toggleNotifications.setText("Notifications On");
                    }
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // ***Added toggleNotification button to buttonPanel
        buttonPanel.add(toggleNotifications);
        buttonPanel.add(addProjectButton);
        add(buttonPanel, BorderLayout.NORTH);
        // ***Automatically sets notifications to be sent periodically
        Runnable sendNotification = () -> notificationController.execute(LocalDate.now(), dashboardViewModel.getState().getUsername());
        scheduledFuture = schedule.scheduleAtFixedRate(sendNotification, 0, 24, TimeUnit.HOURS);
    }

    private void updateEmptyDashboardLabel() {
        if (projectsList.isEmpty()) {
            dashboardPanel.removeAll();
            JLabel emptyLabel = new JLabel("Click Add Project to add a project");
            emptyLabel.setFont(new Font("Arial", Font.BOLD, 16));
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setForeground(Color.GRAY);

            dashboardPanel.setLayout(new GridBagLayout());
            dashboardPanel.add(emptyLabel);
        } else {
            dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));
        }
        dashboardPanel.revalidate();
        dashboardPanel.repaint();
    }

    private void displayAllProjects() {
        dashboardPanel.removeAll();

        for (ProjectData project : projectsList) {
            ProjectPanel projectPanel = new ProjectPanel(project);
            dashboardPanel.add(projectPanel);
            dashboardPanel.add(Box.createVerticalStrut(10));
            dashboardPanel.revalidate();
            updateEmptyDashboardLabel();
        }
    }

    private void showAddProjectPopup() throws ParseException {
        JFrame popupFrame = new JFrame("Add Project");
        popupFrame.setSize(400, 250);

        JPanel popupPanel = new JPanel(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Project Title:");
        JTextField nameField = new JTextField();

        JLabel descriptionLabel = new JLabel("Project Description:");
        JTextArea descriptionArea = new JTextArea(5, 20);

        JLabel deadlineLabel = new JLabel("Deadline (YYYY/MM/DD):");
        MaskFormatter dateFormatter = new MaskFormatter("####/##/##");
        dateFormatter.setPlaceholderCharacter('_');

        JFormattedTextField deadlineField = new JFormattedTextField(dateFormatter);

        JButton submitButton = new JButton("Add Project");
        submitButton.addActionListener(e -> {
            String projectName = nameField.getText();
            String projectDescription = descriptionArea.getText();
            String deadline = deadlineField.getText();

            if (!projectName.isEmpty()) {
                if (!projectDescription.isEmpty()) {
                    if (deadline.matches("\\d{4}/\\d{2}/\\d{2}")) {

                        addProjectController.execute(
                                projectName,
                                projectDescription,
                                deadline,
                                dashboardViewModel.getState().getUsername()
                        );

                        popupFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Please enter a date in the format YYYY/MM/DD");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a description for your project");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a title for your project");
            }
        });

        popupPanel.add(nameLabel);
        popupPanel.add(nameField);
        popupPanel.add(descriptionLabel);
        popupPanel.add(new JScrollPane(descriptionArea));
        popupPanel.add(deadlineLabel);
        popupPanel.add(deadlineField);
        popupPanel.add(new JLabel());
        popupPanel.add(submitButton);

        popupFrame.add(popupPanel);
        popupFrame.setVisible(true);
    }

    private class ProjectPanel extends JPanel {
        private final ProjectData projectData;
        private static final int PANEL_MIN_MAX_HEIGHT = 100;
        private static final int ARC_SIZE = 20;

        public ProjectPanel(ProjectData projectData) {
            this.projectData = projectData;
            setMinimumSize(new Dimension((int) (DashboardView.this.getWidth() * 0.95), PANEL_MIN_MAX_HEIGHT));
            setMaximumSize(new Dimension((int) (DashboardView.this.getWidth() * 0.95), PANEL_MIN_MAX_HEIGHT));
            setPreferredSize(new Dimension((int) (DashboardView.this.getWidth() * 0.95), PANEL_MIN_MAX_HEIGHT));
            setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    // call to Vedant's popup
                    ProjectPopup(dashboardViewModel.getState().getUsername(),projectData);
                    JOptionPane.showMessageDialog(null, "Clicked on project: " + projectData.getProjectTitle());
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), ARC_SIZE, ARC_SIZE);
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics metrics = g2d.getFontMetrics();
            int textWidth = metrics.stringWidth(projectData.getProjectTitle());
            int x = (getWidth() - textWidth) / 2;
            int y = getHeight() / 2 + metrics.getHeight() / 4;
            g2d.drawString(projectData.getProjectTitle(), x, y);
            g2d.dispose();
        }
    }
    private void ProjectPopup(String userName, ProjectData projectData) {
        String projectID = projectData.getProjectID();
        String projectTitle = projectData.getProjectTitle();
        String projectDescription = projectData.getProjectDescription();
        String projectTasks = projectData.getProjectTasks();
        String[] tasks = projectTasks.split("`");
        JFrame popupFrame = new JFrame("Project");
        popupFrame.setSize(400, 250);
        JPanel popupPanel = new JPanel();
        popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(popupPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
        // The project title
        JLabel Title = new JLabel("Project Name");
        JTextField title = new JTextField(projectTitle);
        title.setEditable(false);
        popupPanel.add(Title);
        popupPanel.add(title);
        // The project description
        JLabel description = new JLabel("Project Description");
        JTextArea textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        textArea.append(projectDescription + "\n");
        // Creating spacing for the rest of the popup panel
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        popupPanel.add(textArea);
        popupPanel.add(description);
        // The list of tasks along with their deadlines and descriptions and status
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
                        // make the change to the status of the i task in the tasks of the given project id
                        //  need to make this function in the dashboard state
                        dashboardViewModel.getState().setTaskStatus(projectID, finalI, 1);
                    }
                }
            });
            i = i + 1;
        }
    }
    public void propertyChange(PropertyChangeEvent evt) {
        DashboardState state = (DashboardState) evt.getNewValue();

        if (state.getAddProjectError() != null) {
            JOptionPane.showMessageDialog(this, state.getAddProjectError());
        }
        // ***Displays JOptionPane if there is a notification to be displayed.
        if (state.getNotificationMessage() != null) {
            JOptionPane.showMessageDialog(this, state.getNotificationMessage());
            state.setNotificationMessage(null);
        }

        displayAllProjects();
    }
}