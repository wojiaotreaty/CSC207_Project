package use_case.send_notification;
public class NotificationOutputData {
    private final String message;
    public NotificationOutputData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
