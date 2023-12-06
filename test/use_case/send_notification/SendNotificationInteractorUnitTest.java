package use_case.send_notification;

import static org.junit.jupiter.api.Assertions.*;

import entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SendNotificationInteractorUnitTest {

    @Test
    void successTest() {
        NotificationInputData notificationInputData = new NotificationInputData(LocalDate.now(), "foo");

    }
    private static class DummyUsersDataAccessInterface implements NotificationUsersDataAccessInterface {

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
