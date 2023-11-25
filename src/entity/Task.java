package entity;

import java.time.LocalDate;
import java.lang.StringBuilder;

public class Task {
    private String name;
    private String description;
    private LocalDate deadline;
    private boolean status;

    public Task(String name, LocalDate deadline, String description) {
        this.name = name;
        this.deadline = deadline;
        this.description = description;
        status = false;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getName() {
        return name;
    }

    public LocalDate getDeadline() {
        return deadline;
    }
    public String getDescription() {
        return description;
    }

    public boolean getStatus() {
        return status;
    }
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(name).append("`");
        s.append(description).append("`");
        s.append(deadline.toString()).append("`");
        s.append(status);
        return String.valueOf(s);
    }
}
