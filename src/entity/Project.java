package entity;

import java.time.LocalDate;
import java.util.ArrayList;

public class Project {
    private final int id;
    private String name;
    private LocalDate deadline;
    private ArrayList<Task> tasks;

    public Project(int id, String name, LocalDate deadline) {
        this.id = id;
        this.name = name;
        this.deadline = deadline;
        tasks = new ArrayList<Task>();
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setName(String name) {
        this.name = name;
    }
    public LocalDate getDeadline() {
        return deadline;
    }
    public String getName() {
        return name;
    }
}
