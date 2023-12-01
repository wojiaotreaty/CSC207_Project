package use_case.send_notification;

import java.time.LocalDate;

public class NotificationInputData {
    private final LocalDate date;
    private final String currentUser;

    public NotificationInputData(LocalDate date, String currentUser) {
        this.date = date;
        this.currentUser = currentUser;
    }

    public LocalDate getDate() {
        return date;
    }
    public String getCurrentUser() {
        return currentUser;
    }
}
