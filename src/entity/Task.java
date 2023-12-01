package entity;

import java.time.LocalDate;

public interface Task {
    String toString();

    void setStatus(boolean status);

    LocalDate getDeadline();
}
