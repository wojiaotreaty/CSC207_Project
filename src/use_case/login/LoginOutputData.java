package use_case.login;

import java.util.ArrayList;

public class LoginOutputData {

    private final String username;
    private final ArrayList<ArrayList<String>> projects;
    private boolean useCaseFailed;

    public LoginOutputData(String username, ArrayList<ArrayList<String>> projects, boolean useCaseFailed) {
        this.username = username;
        this.useCaseFailed = useCaseFailed;
        this.projects = projects;
    }

    public String getUsername() {
        return username;
    }
    public ArrayList<ArrayList<String>> getProjectData() {return projects;}

}
