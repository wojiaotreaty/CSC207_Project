package view;

import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.dashboard.ProjectData;
import interface_adapter.add_project.AddProjectController;
import interface_adapter.delete_project.DeleteProjectController;
import interface_adapter.delete_project.DeleteProjectState;
import interface_adapter.delete_project.DeleteProjectViewModel;
import interface_adapter.refactor_project.RefactorProjectController;
import interface_adapter.send_notification.NotificationController;
import interface_adapter.set_status.SetStatusController;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.text.MaskFormatter;

public class DashboardView extends JPanel implements PropertyChangeListener {
    public final String viewName = "Project Dashboard";
    private final DashboardViewModel dashboardViewModel;
    private final DeleteProjectViewModel deleteProjectViewModel;
    private final RefactorProjectController refactorProjectController;
    private JPanel dashboardPanel;
    private ArrayList<ProjectData> projectsList;
    public boolean fromLogin = true;
    private boolean expectingNotification = false;
    private boolean fromRefactor = false;
    private final ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture = null;
    private final AddProjectController addProjectController;
    private final NotificationController notificationController;
    private final DeleteProjectController deleteProjectController;
    private final SetStatusController setStatusController;

    public DashboardView(DashboardViewModel dashboardViewModel, DeleteProjectViewModel deleteProjectViewModel, AddProjectController addProjectController
                         ,NotificationController notificationController, DeleteProjectController deleteProjectController, RefactorProjectController refactorProjectController,SetStatusController setStatusController) {
        this.refactorProjectController=refactorProjectController;
        this.dashboardViewModel = dashboardViewModel;
        this.deleteProjectViewModel = deleteProjectViewModel;
        this.addProjectController = addProjectController;
        this.notificationController = notificationController;
        this.deleteProjectController = deleteProjectController;
        this.setStatusController = setStatusController;

        deleteProjectViewModel.addPropertyChangeListener(this);

        DashboardState dashboardState = dashboardViewModel.getState();

        dashboardViewModel.addPropertyChangeListener(this);

        projectsList = dashboardState.getProjects();

        dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(dashboardPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JButton addProjectButton = new JButton("Add Project");
        JButton toggleNotifications = new JButton("Notifications Off");
        addProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addProjectButton) {
                    try {
                        showAddProjectPopup();
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        toggleNotifications.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == toggleNotifications) {
                    // If notifications are turned off, start them up again using scheduleAtFixedRate
                    if (scheduledFuture == null) {
                        expectingNotification = true;
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
        buttonPanel.setPreferredSize(new Dimension(600, 40));
        scrollPane.setPreferredSize(new Dimension(600, 360));
        buttonPanel.add(toggleNotifications);
        buttonPanel.add(addProjectButton);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        this.add(buttonPanel);
        this.add(scrollPane);

        displayAllProjects();

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
//            updateEmptyDashboardLabel();
        }
        updateEmptyDashboardLabel();
    }

    private void showAddProjectPopup() throws ParseException {
        JFrame popupFrame = new JFrame("Add Project");
        popupFrame.setSize(700, 400);

        JPanel popupPanel = new JPanel(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Project Title:", SwingConstants.CENTER);
        JTextField nameField = new JTextField();

        JLabel descriptionLabel = new JLabel("Project Description:", SwingConstants.CENTER);
        JTextArea descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);

        JLabel deadlineLabel = new JLabel("Deadline (YYYY/MM/DD):", SwingConstants.CENTER);
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

    public class ProjectPanel extends JPanel {
        private final ProjectData projectData;
        private static final int ARC_SIZE = 20;

        public ProjectPanel(ProjectData projectData) {
            this.projectData = projectData;

            setMinimumSize(new Dimension(570, 100));
            setMaximumSize(new Dimension(570, 100));
            setPreferredSize(new Dimension(570, 100));
            setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));


            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    // call to Vedant's popup
                    projectPopup(dashboardViewModel.getState().getUsername(),projectData);
                }
            });
        }

        public ProjectData getProjectData() { return projectData; }

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
    private void projectPopup(String userName, ProjectData projectData) {
        String projectID = projectData.getProjectID();
        String projectTitle = projectData.getProjectTitle();
        String projectDescription = projectData.getProjectDescription();
        ArrayList<ArrayList<String>> projectTasks = projectData.getProjectTasks();

        JFrame popupFrame = new JFrame("Project");
        popupFrame.setSize(1080, 500);
        popupFrame.setResizable(false);

        JPanel popupPanel = new JPanel();
        popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(popupPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        popupFrame.add(scrollPane, BorderLayout.CENTER);

        // The project title
        JLabel Title = new JLabel(projectTitle);
        popupFrame.add(Title, BorderLayout.NORTH);
        JTextArea description = new JTextArea(1,20);
        description.append("Project Description");
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setOpaque(false);
        description.setEditable(false);
        description.setFocusable(false);
        description.setBackground(UIManager.getColor("Label.background"));
        description.setFont(UIManager.getFont("Label.font"));
        description.setBorder(UIManager.getBorder("Label.border"));
        JTextArea textArea = new JTextArea(5, 20);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setBackground(UIManager.getColor("Label.background"));
        textArea.setBorder(UIManager.getBorder("Label.border"));
        textArea.append(projectDescription + "\n");

        // Creating spacing for the rest of the popup panel
        popupPanel.add(description);
        popupPanel.add(textArea);

        // The list of tasks along with their deadlines and descriptions and status
        int i = 0;
        for (ArrayList<String> task : projectTasks) {
            StringBuilder mutableTask = new StringBuilder();
            mutableTask.append(task);
            String taskName = task.get(0);
            String taskDescription = task.get(1);;
            String taskDeadline = task.get(2);

            final String[] taskStatus = {task.get(3)};
            JLabel tName = new JLabel("Task Name");
            JLabel name = new JLabel(taskName);
            JPanel panel1 = new JPanel(new GridLayout(2, 1));
            panel1.add(tName);
            panel1.add(name);
            JLabel tDeadline = new JLabel("Task Deadline");
            JLabel deadline = new JLabel(taskDeadline);

            // Adding the deadline label and the deadline text field into one panel
            JPanel panel2 = new JPanel(new GridLayout(2, 1));
            panel2.add(tDeadline);
            panel2.add(deadline);

            // Create a check-box which is always checked if the task Status is true
            JCheckBox status = new JCheckBox("status");
            if (taskStatus[0].equals("true")) {
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
            JTextArea tDescription = new JTextArea(3, 20);
            tDescription.setWrapStyleWord(true);
            tDescription.setLineWrap(true);
            tDescription.setOpaque(false);
            tDescription.setEditable(false);
            tDescription.setFocusable(false);
            tDescription.setBackground(UIManager.getColor("Label.background"));
            tDescription.setBorder(UIManager.getBorder("Label.border"));
            tDescription.append(taskDescription + "\n");
            popupPanel.add(tDescription);

            // Action Listener for the check-box status to notify the backend when the user marks
            // a task as completed.

            status.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == status) {
                        StringBuilder sbtask = new StringBuilder();
                        sbtask.append(taskName).append("`");
                        sbtask.append(taskDescription).append("`");
                        sbtask.append(taskDeadline).append("`");
                        sbtask.append(taskStatus[0]);
                        setStatusController.execute(dashboardViewModel.getState().getUsername(), projectID, String.valueOf(sbtask));
                        if (taskStatus[0].equals("true")) {
                            taskStatus[0] = "false";
                        }
                        else {
                            taskStatus[0] = "true";
                        }
                    }
                }
            });
            i = i + 1;
        }
        popupFrame.add(scrollPane, BorderLayout.CENTER);
        popupFrame.setVisible(true);

        JButton refactor = new JButton("Refactor Project Deadlines");
        JButton delete = new JButton("Delete Project");

        refactor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == refactor) {
                    fromRefactor = true;
                    refactorProjectController.execute(userName,projectID);
                    popupFrame.dispose();
                }
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == delete) {
                    deleteProjectConfirmation(popupFrame, projectTitle, projectID);
                }
            }
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(refactor);
        buttons.add(delete);
        popupFrame.add(buttons, BorderLayout.SOUTH);
    }
    private void deleteProjectConfirmation(JFrame popup, String projectName, String projectId) {
        JFrame confirm = new JFrame("Delete Project?");
        confirm.setSize(500, 100);
        confirm.setResizable(false);
        JLabel text = new JLabel("Are you sure you want to delete the project: " + projectName + "?", SwingConstants.CENTER);
        confirm.add(text, BorderLayout.CENTER);
        JButton yes = new JButton("yes");
        JButton no = new JButton("no");
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == yes) {
                    confirm.dispose();
                    popup.dispose();
                    deleteProjectController.execute(dashboardViewModel.getState().getUsername(), projectId);
                }
            }
        });

        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == no) {
                    confirm.dispose();
                }
            }
        });
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(no);
        buttons.add(yes);
        confirm.add(buttons, BorderLayout.SOUTH);
        confirm.setVisible(true);
    }
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getNewValue() instanceof DashboardState state){

            if (state.getAddProjectError() != null) {
                JOptionPane.showMessageDialog(this, state.getAddProjectError());
            }
            // ***Displays JOptionPane if there is a notification to be displayed.
            if (state.getNotificationMessage() != null) {
                if (state.getNotificationImage() != null) {
                    try {
                        URL url = new URL(state.getNotificationImage());
                        BufferedImage image = ImageIO.read(url);
                        JLabel label = new JLabel(new ImageIcon(image));
                        JFrame f = new JFrame();
                        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        f.getContentPane().add(label);
                        f.pack();
                        f.setLocation(200, 200);
                        f.toFront();
                        f.setVisible(true);
                    } catch (IOException ignored) {
                        // If there is an error, no image pops up so nothing is done and no errors thrown.
                        ;
                    }
                }
                JOptionPane.showMessageDialog(this, state.getNotificationMessage());
                state.setNotificationMessage(null);
            }
            if (fromLogin) {
                fromLogin = false;
                Runnable sendNotification = () -> notificationController.execute(LocalDate.now(), dashboardViewModel.getState().getUsername());
                scheduledFuture = schedule.scheduleAtFixedRate(sendNotification, 0, 24, TimeUnit.HOURS);
            }
            projectsList = state.getProjects();
            displayAllProjects();
            // ***Displays JOptionPane if there is a notification to be displayed.
            if (expectingNotification) {
                expectingNotification = false;
                if (state.getNotificationMessage() != null) {
                    JOptionPane.showMessageDialog(this, state.getNotificationMessage());
                }
            }
            if (fromLogin) {
                fromLogin = false;
                expectingNotification = true;
                Runnable sendNotification = () -> notificationController.execute(LocalDate.now(), dashboardViewModel.getState().getUsername());
                scheduledFuture = schedule.scheduleAtFixedRate(sendNotification, 0, 24, TimeUnit.HOURS);
            }
            if (fromRefactor){
                fromRefactor = false;
                int size = state.getProjects().size();
                projectPopup(state.getUsername(),state.getProjects().get(size-1));
            }
            projectsList = state.getProjects();
            displayAllProjects();
        }
        if (evt.getNewValue() instanceof DeleteProjectState state){
            JOptionPane.showMessageDialog(this, state.getDeletedProjectName()
                    + " successfully deleted.");
        }
    }
}