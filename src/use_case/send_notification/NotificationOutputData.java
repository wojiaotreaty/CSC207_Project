package use_case.send_notification;
public class NotificationOutputData {
    private final String message;
    private final String imageUrl;
    public NotificationOutputData(String message, String imageUrl) {
        this.message = message;
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
