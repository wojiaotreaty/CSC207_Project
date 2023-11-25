package use_case.send_notification;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class NotificationInteractor implements NotificationInputBoundary {
    final NotificationUsersDataAccessInterface notificationUsersDataAccessInterface;
    final NotificationOutputBoundary notificationPresenter;
    public NotificationInteractor(NotificationUsersDataAccessInterface notificationUsersDataAccessInterface,
                                  NotificationOutputBoundary notificationOutputBoundary) {
        this.notificationUsersDataAccessInterface = notificationUsersDataAccessInterface;
        this.notificationPresenter = notificationOutputBoundary;
    }
    @Override
    public void execute(NotificationInputData notificationInputData) {
        User user = notificationUsersDataAccessInterface.getCurrentUser();
        ArrayList<Project> projects = user.getProjects();
        LocalDate date = notificationInputData.getDate();
        LocalDate plusOne = date.plusDays(1);
        LocalDate plusTwo = date.plusDays(2);
        HashMap<Project, ArrayList<Task>> duePlusOne = new HashMap<Project, ArrayList<Task>>();
        HashMap<Project, ArrayList<Task>> duePlusTwo = new HashMap<Project, ArrayList<Task>>();
        for (Project project : projects) {
            for (Task task : project.getTasks()) {
                // checks if task is due tomorrow
                if (task.getDeadline().equals(plusOne)) {
                    if (!duePlusOne.containsKey(project)) {
                        duePlusOne.put(project, new ArrayList<Task>());
                    }
                    duePlusOne.get(project).add(task);
                // checks if task is due in two days
                } else if (task.getDeadline().equals(plusTwo)) {
                    if (!duePlusTwo.containsKey(project)) {
                        duePlusTwo.put(project, new ArrayList<Task>());
                    }
                    duePlusTwo.get(project).add(task);
                }
            }
        }
        String gptOutput = apiCall(duePlusOne, duePlusTwo);


    }
    private static String apiCall(HashMap<Project, ArrayList<Task>> One, HashMap<Project, ArrayList<Task>> Two) {
        return "";
    }
}
