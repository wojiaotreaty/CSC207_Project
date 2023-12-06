package use_case.send_notification;

import static org.junit.jupiter.api.Assertions.*;

import entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SendNotificationInteractorMessageUnitTest {
    private final String output = "These tasks are due today: \n     For Project: project1\n          Task Name: task1\n          Task Description: task1 desc\n\n" +
            "These tasks are due tomorrow: \n     For Project: project1\n          Task Name: task2\n          Task Description: task2 desc\n\n" +
            "     For Project: project2\n          Task Name: task3\n          Task Description: task3 desc\n\n" +
            "These tasks are due the day after tomorrow: \n     For Project: project2\n          Task Name: task4\n          Task Description: task4 desc\n\n";

    @Test
    void MessageTest() {
        NotificationInputData notificationInputData = new NotificationInputData(LocalDate.now(), "foo");
        NotificationUsersDataAccessInterface dummyUsersDataAccess = new DummyUsersDataAccessObject();

        NotificationOutputBoundary successPresenter = new NotificationOutputBoundary() {
            @Override
            public void prepareNotificationView(NotificationOutputData notificationOutputData) {
                assertEquals(notificationOutputData.getMessage().substring(0,output.length()), output);
                assertNotNull(notificationOutputData.getImageUrl());
            }
        };
        NotificationInputBoundary notificationInteractor = new NotificationInteractor(dummyUsersDataAccess, successPresenter);
        notificationInteractor.execute(notificationInputData);
    }
    private static class DummyUsersDataAccessObject implements NotificationUsersDataAccessInterface {

        @Override
        public User getUser(String username) {
            TaskFactory taskFactory = new CommonTaskFactory();
            Task task1 = taskFactory.create("task1", LocalDate.now(), "task1 desc");
            Task task2 = taskFactory.create("task2", LocalDate.now().plusDays(1), "task2 desc");
            Task task3 = taskFactory.create("task3", LocalDate.now().plusDays(1), "task3 desc");
            Task task4 = taskFactory.create("task4", LocalDate.now().plusDays(2), "task4 desc");
            ProjectFactory projectFactory= new CommonProjectFactory();
            Project project1 = projectFactory.create("1", "project1", "project1 desc", new ArrayList<Task>(List.of(new Task[]{task1, task2})));
            Project project2 = projectFactory.create("2", "project2", "project2 desc", new ArrayList<Task>(List.of(new Task[]{task3, task4})));
            UserFactory userFactory = new CommonUserFactory();
            User user  = userFactory.create("foo", "baz", new ArrayList<Project>(List.of(new Project[]{project1, project2})));
            return user;
        }
    }

}
