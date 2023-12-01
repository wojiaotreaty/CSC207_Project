package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class CommonProject implements Project {
    private final String id;
    private final String name;
    private final String description;
    private ArrayList<Task> tasks;

    public CommonProject(String id, String name, String description, ArrayList<Task> tasks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tasks = tasks;
    }

    public String getProjectName() {
        return this.name;
    }

    @Override
    public String getProjectId() {
        return this.id;
    }

    @Override
    public String getProjectDescription() {
        return this.description;
    }

    public ArrayList<Task> getTasks() {
        return (ArrayList<Task>) tasks.clone();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonProject that = (CommonProject) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(description, that.description) && Objects.equals(tasks, that.tasks);
    }

    @Override
    public String toString(){
        String output = "";
        output += "Project id: " + this.id + "\n";
        output += "Project name: " + this.name + "\n";
        output += "Project descryption: " + this.description + "\n";

        StringBuilder taskListString = new StringBuilder();
        for (Task task : this.tasks){
            taskListString.append("\t").append(task.toString()).append("\n");
        }
        output += "Project tasks: " + "\n" + taskListString;
        return output;
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }
}
