package data_access;

import entity.CommonProjectFactory;
import entity.CommonTaskFactory;
import entity.Project;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ProjectsDataAccessObjectTest {

    private final String PROJECTS_PATH = "./projects_test.csv";
    private final CommonProjectFactory PROJECT_FACTORY = new CommonProjectFactory();
    private final CommonTaskFactory TASK_FACTORY = new CommonTaskFactory();

    private ProjectsDataAccessObject projectsDAO;

    private ArrayList<Project> dummyProjects;
    private String[] dummyIds;

    /**
     * Create the projectDAO used for testing.
     * Currently, the database is empty.
     */
    @Before
    public void init(){
        try {
            this.projectsDAO = new ProjectsDataAccessObject(PROJECTS_PATH, PROJECT_FACTORY, TASK_FACTORY);
        } catch (IOException error){
            System.out.println("ERROR: IOexception when creating ProjectDAO");
        }

//        This creates 10 Projects with ids 1 - 10 in dummyProjects to use for tests.
        for (int i = 0; i < 10; i++){
            String id = String.valueOf(i + 1);
            Project project = PROJECT_FACTORY.create(id, "Project " + i, "dummy", new ArrayList<>());
            dummyProjects.add(project);
        }
        dummyIds = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

//        adds 10 projects into the database
        projectsDAO.saveProjects(dummyProjects);
    }

    @Test
    public void testGetProjectsSuccess() {
        assertEquals(projectsDAO.getProjects(dummyIds), dummyProjects);
    }

    @Test
    public void testGetProjectsFail(){
        try {
            projectsDAO.getProjects(new String[]{"11"});
            fail("NoSuchElementException expected but did not trigger.");
        } catch (NoSuchElementException ignored){
        }
    }

    // Deletes the 10 dummyProjects in the database right now, then adds them back
    @Test
    public void testDeleteProjects(){
        ArrayList<String> leftoverIds = new ArrayList<>(List.of(dummyIds));

        while (!leftoverIds.isEmpty()){
            int i = (int) (Math.random() * 10) + 1;
            if (leftoverIds.contains(i)){
                leftoverIds.remove(String.valueOf(i));
                dummyProjects.remove(i);

                projectsDAO.deleteProject(String.valueOf(i));

                String[] ids = leftoverIds.toArray(new String[0]);
                assertEquals(projectsDAO.getProjects(ids), dummyProjects);
            }
        }

//        Remake dummyProjects
        for (int i = 0; i < 10; i++){
            String id = String.valueOf(i + 1);
            Project project = PROJECT_FACTORY.create(id, "Project " + i, "dummy", new ArrayList<>());
            dummyProjects.add(project);
        }

        projectsDAO.saveProjects(dummyProjects);
    }

    @Test
    public void testGenerateNewProjectIdHelper(){
        for (int i = 0; i < 10; i++){
            projectsDAO.deleteProject(String.valueOf(i + 1));
        }

        deleteAll();
        for (int i = 0; i < 10; i++){
            String id = projectsDAO.generateNewProjectIdHelper();
            assertEquals(id, String.valueOf(i + 1));

            ArrayList<Project> projectsToAdd = new ArrayList<>();
            projectsToAdd.add(
                    PROJECT_FACTORY.create(id, "Project " + i, "dummy", new ArrayList<>()));
            projectsDAO.saveProjects(projectsToAdd);
        }

        assertEquals(projectsDAO.getProjects(dummyIds), dummyProjects);
    }

    @Test
    public void testSaveProjectsExisted(){
        projectsDAO.saveProjects(dummyProjects);
        assertEquals(projectsDAO.generateNewProjectIdHelper(), "11");
    }

    @After
    public void cleanUp(){
        File testProjectsDatabase = new File(PROJECTS_PATH);
        if (!testProjectsDatabase.delete()){
            System.out.println(PROJECTS_PATH + " did not delete properly after testing.");
        };
    }

}
