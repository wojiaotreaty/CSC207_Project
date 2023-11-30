package entity;

import java.time.LocalDate;

public interface TaskFactory {
    Task create(String name, LocalDate deadline, String description);
}
