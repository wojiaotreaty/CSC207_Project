package entity;

import java.time.LocalDate;

public class CommonTaskFactory implements TaskFactory {
    public CommonTask create(String name, LocalDate deadline, String description) {
        return new CommonTask(name, deadline, description);
    }
    public CommonTask create(String name, String deadline, String description) {
        return new CommonTask(name, LocalDate.parse(deadline), description);
    }
}
