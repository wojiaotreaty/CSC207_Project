package Dummy;

import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.dashboard.ProjectData;
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

public class DummyView extends JFrame implements PropertyChangeListener {
    private final DashboardViewModel dashboardViewModel;
    private JPanel dashboardPanel;
    private ArrayList<ProjectData> projectsList = new ArrayList<ProjectData>();
    private final ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture = null;

    public DummyView(DashboardViewModel dashboardViewModel,
                         NotificationController notificationController) {
        this.dashboardViewModel = dashboardViewModel;

        DashboardState dashboardState = dashboardViewModel.getState();

        dashboardViewModel.addPropertyChangeListener(this);

        projectsList = dashboardState.getProjects();


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
                    if (scheduledFuture == null) {
                        Runnable sendNotification = () -> notificationController.execute(LocalDate.now());
                        scheduledFuture = schedule.scheduleAtFixedRate(sendNotification, 0, 24, TimeUnit.HOURS);
                        toggleNotifications.setText("Notifications Off");
                    }
                    else {
                        scheduledFuture.cancel(true);
                        scheduledFuture = null;
                        toggleNotifications.setText("Notifications On");
                    }
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addProjectButton);
        buttonPanel.add(toggleNotifications);
        add(buttonPanel, BorderLayout.NORTH);

        displayAllProjects();

        Runnable sendNotification = () -> notificationController.execute(LocalDate.now());
        scheduledFuture = schedule.scheduleAtFixedRate(sendNotification, 0, 24, TimeUnit.HOURS);
    }

    private void updateEmptyDashboardLabel() {
        projectsList = new ArrayList<>();
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

                        ;

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
            setMinimumSize(new Dimension((int) (DummyView.this.getWidth() * 0.95), PANEL_MIN_MAX_HEIGHT));
            setMaximumSize(new Dimension((int) (DummyView.this.getWidth() * 0.95), PANEL_MIN_MAX_HEIGHT));
            setPreferredSize(new Dimension((int) (DummyView.this.getWidth() * 0.95), PANEL_MIN_MAX_HEIGHT));
            setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    //TODO: PLACEHOLDER FOR VEDANTS PROJECT VIEW POPUP
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

    public void propertyChange(PropertyChangeEvent evt) {
        DashboardState state = (DashboardState) evt.getNewValue();

        if (state.getAddProjectError() != null) {
            JOptionPane.showMessageDialog(this, state.getAddProjectError());
            state.setAddProjectError(null);
        }
        if (state.getNotificationMessage() != null) {
            JOptionPane.showMessageDialog(this, state.getNotificationMessage());
            state.setNotificationMessage(null);
        }
        displayAllProjects();
    }
}
