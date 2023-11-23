package entity;

import java.util.ArrayList;
import java.util.HashMap;

public interface ProjectFactory {

    Project create(String projectId, String projectName, String projectDesc, ArrayList<HashMap<String, String>> tasks);
}
