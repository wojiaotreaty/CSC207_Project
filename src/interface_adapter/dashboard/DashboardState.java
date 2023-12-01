package interface_adapter.dashboard;

import java.util.ArrayList;

public class DashboardState {
    private String username = "";
    private ArrayList<ProjectData> projects;
    private String addProjectError = null;
    private String projectDescription;
    private ArrayList<String> tasks;
    private String projectTitle;
    private String projectID;
    private String refactorProjectError = null;


    public DashboardState(DashboardState copy) {
        username = copy.username;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public DashboardState() {
    }

    public String getAddProjectError() { return addProjectError; }
    public void setAddProjectError(String error) { this.addProjectError = error; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<ProjectData> getProjects() { return projects; }

    public void setProjects(ArrayList<ArrayList<String>> listOfProjects) {
        ArrayList<ProjectData> projects = new ArrayList<>();

        for (ArrayList<String> project : listOfProjects) {
            //project is formatted as follows: {String projectID, String projectTitle, String projectDescription, String projectTasks}
            ProjectData projectData = new ProjectData(project.get(0), project.get(1), project.get(2), project.get(3));
            projects.add(projectData);
        }

        this.projects = projects;
    }

    public void addProjectData(ArrayList<String> project) {
        ProjectData projectData = new ProjectData(project.get(0), project.get(1), project.get(2), project.get(3));
        this.projects.add(projectData);
    }

    public void deleteProjectData(String projectID){
        for (ProjectData project : this.projects){
            if (project.getProjectID().equals(projectID)){
                projects.remove(project);
                break;
            }
        }
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


