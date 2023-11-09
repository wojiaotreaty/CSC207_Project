package entity;

import java.time.LocalDate;

public class Task {
    private String name;
    private LocalDate deadline;
    private String description;
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
}
