package use_case.send_notification;

import static org.junit.jupiter.api.Assertions.*;

import data_access.ProjectsDataAccessObject;
import data_access.UsersDataAccessObject;
import entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SendNotificationIntegrationTest {
    private final String PROJECTS_PATH = "./projects_test.csv";
    private final String USERS_PATH = "./users_test.csv";
    private final CommonProjectFactory PROJECT_FACTORY = new CommonProjectFactory();
    private final CommonTaskFactory TASK_FACTORY = new CommonTaskFactory();
    private final CommonUserFactory USER_FACTORY = new CommonUserFactory();
    private UsersDataAccessObject usersDAO;
    private final String output = "These tasks are due today: \n     For Project: project1\n          Task Name: task1\n          Task Description: task1 desc\n\n" +
            "These tasks are due tomorrow: \n     For Project: project1\n          Task Name: task2\n          Task Description: task2 desc\n\n" +
            "     For Project: project2\n          Task Name: task3\n          Task Description: task3 desc\n\n" +
            "These tasks are due the day after tomorrow: \n     For Project: project2\n          Task Name: task4\n          Task Description: task4 desc\n\n";


    @BeforeEach
    public void init() {
        try {
            ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(
                    PROJECTS_PATH, PROJECT_FACTORY, TASK_FACTORY);
            this.usersDAO = new UsersDataAccessObject(USERS_PATH, USER_FACTORY, projectsDAO);

            User user = new CommonUser("foobar", "baz");
            usersDAO.saveUser(user);

        } catch (IOException error){
            System.out.println("ERROR: IOexception when creating UsersDAO");
        }
    }

    @Test
    void MessageTest() {
        TaskFactory taskFactory = new CommonTaskFactory();
        Task task1 = taskFactory.create("task1", LocalDate.now(), "task1 desc");
        Task task2 = taskFactory.create("task2", LocalDate.now().plusDays(1), "task2 desc");
        Task task3 = taskFactory.create("task3", LocalDate.now().plusDays(1), "task3 desc");
        Task task4 = taskFactory.create("task4", LocalDate.now().plusDays(2), "task4 desc");
        ProjectFactory projectFactory = new CommonProjectFactory();
        Project project1 = projectFactory.create("1", "project1", "project1 desc", new ArrayList<Task>(List.of(new Task[]{task1, task2})));
        Project project2 = projectFactory.create("2", "project2", "project2 desc", new ArrayList<Task>(List.of(new Task[]{task3, task4})));
        UserFactory userFactory = new CommonUserFactory();
        User user  = userFactory.create("foo", "baz", new ArrayList<Project>(List.of(new Project[]{project1, project2})));
        usersDAO.saveUser(user);
        usersDAO.saveUser(user);


        NotificationInputData notificationInputData = new NotificationInputData(LocalDate.now(), "foo");

        NotificationOutputBoundary successPresenter = notificationOutputData -> {
            assertEquals(notificationOutputData.getMessage().substring(0,output.length()), output);
            assertNotNull(notificationOutputData.getImageUrl());
        };
        NotificationInputBoundary notificationInteractor = new NotificationInteractor(usersDAO, successPresenter);
        notificationInteractor.execute(notificationInputData);
    }
    @Test
    void BlankTest() {
        TaskFactory taskFactory = new CommonTaskFactory();

        UserFactory userFactory = new CommonUserFactory();
        User user  = userFactory.create("foobar", "baz");
        usersDAO.saveUser(user);

        NotificationInputData notificationInputData = new NotificationInputData(LocalDate.now(), "foobar");

        NotificationOutputBoundary successPresenter = notificationOutputData -> {
            assertNull(notificationOutputData.getMessage());
            assertNull(notificationOutputData.getImageUrl());
        };
        NotificationInputBoundary notificationInteractor = new NotificationInteractor(usersDAO, successPresenter);
        notificationInteractor.execute(notificationInputData);
    }


    @AfterEach
    public void cleanUp(){
        File testProjectsDatabase = new File(PROJECTS_PATH);
        if (!testProjectsDatabase.delete()){
            System.out.println(PROJECTS_PATH + " did not delete properly after testing.");
        }
        File testUsersDatabase = new File(USERS_PATH);
        if (!testUsersDatabase.delete()){
            System.out.println(USERS_PATH + " did not delete properly after testing.");
        }
    }
}