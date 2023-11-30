package entity;

import java.time.LocalDate;

public class CommonTaskFactory implements TaskFactory {
    CommonTask create(String name, LocalDate deadline, String description) {
        return new CommonTask(name, deadline, description);
    }
    CommonTask create(String name, String deadline, String description) {
        return new CommonTask(name, LocalDate.parse(deadline), description);
    }
}
