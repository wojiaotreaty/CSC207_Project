package use_case.send_notification;
public class NotificationOutputData {
    private final String gptResponse;
    public NotificationOutputData(String gptResponse) {
        this.gptResponse = gptResponse;
    }

    public String getGptResponse() {
        return gptResponse;
    }
}
