package entity;

import java.time.LocalDate;
import java.util.ArrayList;

public class Project {
    private final int id;
    private String name;
    private LocalDate deadline;
    private ArrayList<Task> tasks;
    private boolean status;

    public Project(int id, String name, LocalDate deadline) {
        this.id = id;
        this.name = name;
        this.deadline = deadline;
        tasks = new ArrayList<Task>();
        status = false;
    }
    public Project(int id, String name, LocalDate deadline, ArrayList<Task> tasks) {
        this.id = id;
        this.name = name;
        this.deadline = deadline;
        this.tasks = new ArrayList<Task>(tasks);
        status = false;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDate getDeadline() {
        return deadline;
    }
    public String getName() {
        return name;
    }
    public boolean getStatus() {
        return status;
    }
    // adds task to tasks such that it is the first task with its deadline chronologically.
    public void addTask(Task task) {
        if (tasks.isEmpty()) {
            tasks.add(task);
        } else {
            LocalDate taskDeadline = task.getDeadline();
            int i = 0;
            while (i < tasks.size() && tasks.get(i).getDeadline().isBefore(taskDeadline)) {
                i++;
            }
            if (i == tasks.size()) {
                tasks.add(task);
            } else {
                tasks.add(i, task);
            }
        }
    }
    public void removeTask(Task task) {
        tasks.remove(task);
    }
}
