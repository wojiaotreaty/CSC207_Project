package entity;

import java.util.ArrayList;
import java.util.HashMap;

public interface Project {

    String getProjectTitle();

    ArrayList<HashMap<String, String>> getTasks();

}
