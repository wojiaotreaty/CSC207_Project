
import Dummy.DummyDAO;
import Dummy.DummyInteractor;
import Dummy.DummyView;

import interface_adapter.dashboard.DashboardViewModel;

import interface_adapter.send_notification.NotificationController;
import interface_adapter.send_notification.NotificationPresenter;

import use_case.send_notification.NotificationOutputBoundary;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class DummyMain {
    public static void main(String[] args) {
        // Build the main program window, the main panel containing the
        // various cards, and the layout, and stitch them together.

        // The main application window.
        JFrame application = new JFrame("Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        CardLayout cardLayout = new CardLayout();

        // The various View objects. Only one view is visible at a time.
        JPanel views = new JPanel(cardLayout);
        application.add(views);

        DashboardViewModel dashboardViewModel = new DashboardViewModel();

        DummyDAO dummyDAO = new DummyDAO();

        NotificationOutputBoundary notificationPresenter = new NotificationPresenter(dashboardViewModel);
        DummyInteractor dummyInteractor = new DummyInteractor(dummyDAO, notificationPresenter);
        NotificationController notificationController = new NotificationController(dummyInteractor);
        DummyView dummyView = new DummyView(dashboardViewModel, notificationController);


        application.pack();
        application.setVisible(true);
    }
}