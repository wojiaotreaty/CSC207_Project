package Dummy;

import entity.Project;
import entity.Task;
import entity.User;
import use_case.send_notification.NotificationUsersDataAccessInterface;

import java.time.LocalDate;

public class DummyDAO implements NotificationUsersDataAccessInterface {
    public DummyDAO() {
    }
    @Override
    public User getCurrentUser() {
        User user = new User("dummy", "dummy");
        Task task1 = new Task("task1", "the first task", LocalDate.now());
        Task task2 = new Task("task2", "the second task, with a much much longer description to see what happens to the panel.", LocalDate.now().plusDays(1));
        Task task3 = new Task("task3", "the third task", LocalDate.now());
        Project project1 = new Project("1", "Project1", "The first project.");
        project1.addTask(task1);
        project1.addTask(task2);
        Project project2 = new Project("2", "Project2", "The second project.");
        project2.addTask(task3);
        user.addProject(project1);
        user.addProject(project2);
        return user;
    }
}
