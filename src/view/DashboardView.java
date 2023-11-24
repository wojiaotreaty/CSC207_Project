package view;

import interface_adapter.add_project.AddProjectState;
import interface_adapter.add_project.AddProjectViewModel;
import interface_adapter.add_project.AddProjectController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.text.MaskFormatter;

public class DashboardView extends JFrame {
    private JPanel dashboardPanel;
    private ArrayList<String> projectsList;

    public DashboardView() {
        projectsList = new ArrayList<>();

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
        addProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddProjectPopup();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addProjectButton);
        add(buttonPanel, BorderLayout.NORTH);
    }

    private void updateEmptyDashboardLabel() {
        dashboardPanel.removeAll();
        if (projectsList.isEmpty()) {
            JLabel emptyLabel = new JLabel("Click Add Project to add a project");
            emptyLabel.setFont(new Font("Arial", Font.BOLD, 16));
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setForeground(Color.GRAY);

            dashboardPanel.setLayout(new GridBagLayout());
            dashboardPanel.add(emptyLabel);
        } else {
            dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));
            projectsList.forEach(project -> {
                ProjectPanel projectPanel = new ProjectPanel(project, "Description Placeholder");
                dashboardPanel.add(projectPanel);
                dashboardPanel.add(Box.createVerticalStrut(10));
            });
        }
        dashboardPanel.revalidate();
        dashboardPanel.repaint();
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
                        projectsList.add(projectName);

                        ProjectPanel projectPanel = new ProjectPanel(projectName, projectDescription);
                        dashboardPanel.add(projectPanel);
                        dashboardPanel.add(Box.createVerticalStrut(10));
                        dashboardPanel.revalidate();
                        updateEmptyDashboardLabel();

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
        private String projectName;
        private String projectDescription;
        private static final int PANEL_MIN_MAX_HEIGHT = 100;
        private static final int ARC_SIZE = 20;

        public ProjectPanel(String projectName, String projectDescription) {
            this.projectName = projectName;
            this.projectDescription = projectDescription;
            setMinimumSize(new Dimension((int) (DashboardView.this.getWidth() * 0.95), PANEL_MIN_MAX_HEIGHT));
            setMaximumSize(new Dimension((int) (DashboardView.this.getWidth() * 0.95), PANEL_MIN_MAX_HEIGHT));
            setPreferredSize(new Dimension((int) (DashboardView.this.getWidth() * 0.95), PANEL_MIN_MAX_HEIGHT));
            setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    JOptionPane.showMessageDialog(null, "Clicked on project: " + projectName);
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
            int textWidth = metrics.stringWidth(projectName);
            int x = (getWidth() - textWidth) / 2;
            int y = getHeight() / 2 + metrics.getHeight() / 4;
            g2d.drawString(projectName, x, y);
            g2d.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DashboardView dashboard = new DashboardView();
            dashboard.setVisible(true);
        });
    }
}
