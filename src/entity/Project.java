package entity;

import java.time.LocalDate;
import java.util.ArrayList;

public class Project implements ProjectInterface {
    private final String id;
    private final String name;
    private final String description;
    private ArrayList<Task> tasks;

    public Project(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        tasks = new ArrayList<Task>();
    }
    public Project(String id, String name, String description, ArrayList<Task> tasks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tasks = tasks;
    }

    public String getDesc() {
        return description;
    }

    public String getName() {
        return name;
    }
    public ArrayList<Task> getTasks() {
        return (ArrayList<Task>) tasks.clone();
    }
    public String getId() {
        return id;
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
