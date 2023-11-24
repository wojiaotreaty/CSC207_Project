package use_case.send_notification;

import java.time.LocalDate;

public class NotificationInputData {
    private final LocalDate date;

    public NotificationInputData(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}
