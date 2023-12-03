package entity;

import java.time.LocalDate;

public interface Task {
    String toString();

    void setStatus(boolean status);

    LocalDate getDeadline();
    void setDeadline(LocalDate date);
    String getName();
    String getDescription();
    boolean getStatus();
}
