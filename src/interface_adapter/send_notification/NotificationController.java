package interface_adapter.send_notification;

import use_case.send_notification.NotificationInputBoundary;
import use_case.send_notification.NotificationInputData;

import java.time.LocalDate;

public class NotificationController {
    final NotificationInputBoundary notificationInteractor;
    public NotificationController(NotificationInputBoundary notificationInteractor) {
        this.notificationInteractor = notificationInteractor;
    }
    public void execute(LocalDate date, String currentUser) {
        NotificationInputData notificationInputData = new NotificationInputData(date, currentUser);
        notificationInteractor.execute(notificationInputData);
    }
}
