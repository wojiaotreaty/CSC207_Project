package interface_adapter.send_notification;

import use_case.send_notification.NotificationOutputBoundary;
import use_case.send_notification.NotificationOutputData;

public class NotificationPresenter implements NotificationOutputBoundary {
    @Override
    public void prepareNotificationView(NotificationOutputData notificationOutputData) {

    }
}
