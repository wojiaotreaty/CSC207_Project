package interface_adapter.dashboard;


import java.util.ArrayList;

/*
 * mock dashboard state
 */
public class DashboardState {
    private String username = "";
    private ArrayList<ArrayList<String>> projectData;

    public DashboardState(DashboardState copy) {
        username = copy.username;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public DashboardState() {}

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setProjects(ArrayList<ArrayList<String>> projectData){
        this.projectData=projectData;
    }
}
