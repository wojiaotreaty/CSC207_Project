package interface_adapter;

import java.util.ArrayList;

public class ProjectState {
    private String username = "";
    private String projectDescription;
    private ArrayList<String> tasks;
    private String projectTitle;
    private String projectID;
    private String refactorProjectError = null;

    public ProjectState(ProjectState copy) {
        username = copy.username;

    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public ProjectState() {
    }

    public String getRefactorProjectError() { return refactorProjectError; }
    public void setRefactorProjectError(String error) { this.refactorProjectError = error; }


    public boolean getrefactorProjectError() {
        return false;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public ArrayList<String> getTasks() {
        return tasks;
    }

    public static void setTasks(ArrayList<String> tasks) {
        tasks = tasks;
    }
    public void setTaskStatus(int index, int status )
    {
    String task =tasks.get(index);
    String[]t =task.split("|uwu|");
    StringBuilder new_task =new StringBuilder();
    for(int i=0;i<3;i++) {
        new_task = new_task.append(t[i]);
    }
    if(status ==1){
    new_task.append("true");}
    else{
        new_task.append("false");
    }
    tasks.set(index,new_task.toString());
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectID() {
        return projectID;
    }
    public void setProjectID(String ID){
       this.projectID=ID;
    }
}