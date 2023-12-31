package use_case.login;

import java.util.ArrayList;

public class LoginOutputData {

    private final String username;
    /*
    projects is an arraylist of arraylist where each inner arraylist represents a project
    and it contains a string representation of all tasks related to that project

    projects is unique to every user.
     */
    private final ArrayList<ArrayList<String>> projects;

    public LoginOutputData(String username, ArrayList<ArrayList<String>> projects) {
        this.username = username;
        this.projects = projects;
    }

    public String getUsername() {
        return username;
    }
    public ArrayList<ArrayList<String>> getProjectData() { return projects; }

}
