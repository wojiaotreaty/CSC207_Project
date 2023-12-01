package data_access;

import entity.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class DataAccessObjectTestHelper {

    static ArrayList<Project> getDummyProjectsTen(ProjectFactory projectFactory, TaskFactory taskFactory){
        //        This creates 10 Projects with ids 1 - 10 in dummyProjects to use for tests.
        ArrayList<Project> dummyProjects = new ArrayList<>();

        for (int i = 0; i < 10; i++){
            String id = String.valueOf(i + 1);
            LocalDate dummyDate = LocalDate.parse("2023-04-20");
            Task dummyTask = taskFactory.create("task "  + id, dummyDate, "dummy task desc");
            ArrayList<Task> dummyTaskList = new ArrayList<>();
            dummyTaskList.add(dummyTask);
            Project project = projectFactory.create(id, "Project " + id,
                    "dummy project desc", dummyTaskList);
            dummyProjects.add(project);
        }

        return dummyProjects;
    }

    static String[] getDummyIdTen(){
        return new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    }

    static ArrayList<Project> getDummyProjectsTenMore(ProjectFactory projectFactory, TaskFactory taskFactory){
        //        This creates 10 Projects with ids 1 - 10 in dummyProjects to use for tests.
        ArrayList<Project> dummyProjects = new ArrayList<>();

        for (int i = 10; i < 20; i++){
            String id = String.valueOf(i + 1);
            LocalDate dummyDate = LocalDate.parse("2023-04-20");
            Task dummyTask = taskFactory.create("task "  + id, dummyDate, "dummy task desc");
            ArrayList<Task> dummyTaskList = new ArrayList<>();
            dummyTaskList.add(dummyTask);
            Project project = projectFactory.create(id, "Project " + id,
                    "dummy project desc", dummyTaskList);
            dummyProjects.add(project);
        }

        return dummyProjects;
    }

    static String[] getDummyIdTenMore(){
        return new String[]{"11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
    }

}
