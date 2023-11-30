package entity;

import java.time.LocalDate;

public class UwuTaskFactory implements TaskFactory {
    UwuTask create(String name, LocalDate deadline, String description) {
        return new UwuTask(name, deadline, description);
    }
}
