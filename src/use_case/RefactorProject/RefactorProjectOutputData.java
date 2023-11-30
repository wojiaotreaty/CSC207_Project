package use_case.RefactorProject;

import java.util.ArrayList;

public class RefactorProjectOutputData {
    private ArrayList<String> tasks;


    public RefactorProjectOutputData(ArrayList<String> tasks) {
        this.tasks=tasks;
    }

    public ArrayList<String> getTask() {
        return tasks;
    }

    public void setTask(ArrayList<String> tasks) {
        this.tasks = tasks;
    }
}
