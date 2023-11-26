package interface_adapter.send_notification;

import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import use_case.send_notification.NotificationOutputBoundary;
import use_case.send_notification.NotificationOutputData;

public class NotificationPresenter implements NotificationOutputBoundary {
    private final DashboardViewModel dashboardViewModel;
    public NotificationPresenter(DashboardViewModel dashboardViewModel) {
        this.dashboardViewModel = dashboardViewModel;
    }
    @Override
    public void prepareNotificationView(NotificationOutputData notificationOutputData) {
        DashboardState dashboardState = dashboardViewModel.getState();
        dashboardState.setNotificationMessage(notificationOutputData.getGptResponse());
        dashboardViewModel.setState(dashboardState);
        dashboardViewModel.firePropertyChanged();
    }
}
