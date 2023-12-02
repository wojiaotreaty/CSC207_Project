package use_case.add_project;

import java.util.ArrayList;

public class AddProjectOutputData {
    final private ArrayList<String> project;

    public AddProjectOutputData(ArrayList<String> project) {
        this.project = project;
    }

    public ArrayList<String> getProject () { return this.project; }
}
