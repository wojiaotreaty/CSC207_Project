package entity;

import java.time.LocalDate;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.HashMap;

public class Task implements TaskInterface {
    private String name;
    private String description;
    private LocalDate deadline;
    private boolean status;

    public Task(String name, LocalDate deadline, String description) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        status = false;
    }
    public Task(ArrayList<String> info) {
        name = info.get(0);
        description = info.get(1);
        deadline = LocalDate.parse(info.get(2));
        status = false;
    }
    public Task(HashMap<String, String> info) {
        name = info.get("title");
        description = info.get("description");
        deadline = LocalDate.parse(info.get("deadline"));
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
    public void setId(String id) {
        this.id = id;
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
    public String getId() {
        return id;
    }

    @Override
    public String toStringUwu() {
        StringBuilder s = new StringBuilder();
        s.append(name).append("|uwu|");
        s.append(description).append("|uwu|");
        s.append(deadline.toString()).append("|uwu|");
        s.append(status);
        return String.valueOf(s);
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
