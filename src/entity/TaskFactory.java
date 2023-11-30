package entity;

import java.time.LocalDate;

public class TaskFactory implements TaskFactoryInterface {
    Task create(String name, LocalDate deadline, String description) {
        return new Task(name, deadline, description);
    }
    Task create(String name, String deadline, String description) {
        return new Task(name, LocalDate.parse(deadline), description);
    }
}
