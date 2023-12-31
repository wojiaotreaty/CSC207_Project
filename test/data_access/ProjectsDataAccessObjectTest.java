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
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ProjectsDataAccessObjectTest {

    private final String PROJECTS_PATH = "./projects_test.csv";
    private final CommonProjectFactory PROJECT_FACTORY = new CommonProjectFactory();
    private final CommonTaskFactory TASK_FACTORY = new CommonTaskFactory();

    private ProjectsDataAccessObject projectsDAO;

    private ArrayList<Project> dummyProjects = new ArrayList<>();
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

        this.dummyProjects = DataAccessObjectTestHelper.getDummyProjectsTen(PROJECT_FACTORY, TASK_FACTORY);

        this.dummyIds = DataAccessObjectTestHelper.getDummyIdTen();
    }

    @Test
    public void testSaveProjects(){
        //        adds 10 projects into the database
        projectsDAO.saveProjects(dummyProjects);
        assert(!dummyProjects.isEmpty());
    }

    @Test
    public void testGetProjectsSuccess() {
        projectsDAO.saveProjects(dummyProjects);
        ArrayList<Project> returnedProjects = projectsDAO.getProjects(dummyIds);
        for (int i = 0; i < 10; i++){
            assertEquals(dummyProjects.get(i), returnedProjects.get(i));
        }
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
        projectsDAO.saveProjects(dummyProjects);
        ArrayList<String> leftoverIds = new ArrayList<>(List.of(dummyIds));

        while (!leftoverIds.isEmpty()){
            int i = (int) (Math.random() * 10) + 1;
            if (leftoverIds.contains(String.valueOf(i))){
                leftoverIds.remove(String.valueOf(i));
                dummyProjects.removeIf(project -> project.getProjectId().equals(String.valueOf(i)));

                projectsDAO.deleteProject(String.valueOf(i));

                String[] ids = leftoverIds.toArray(new String[0]);

                ArrayList<Project> returnedProjects = projectsDAO.getProjects(ids);
                for (int j = 0; j < dummyProjects.size(); j++){
                    assertEquals(dummyProjects.get(j), returnedProjects.get(j));
                }
            }
        }

//        Remake dummyProjects
        this.dummyProjects = DataAccessObjectTestHelper.getDummyProjectsTen(PROJECT_FACTORY, TASK_FACTORY);
    }

    @Test
    public void testGenerateNewProjectIdHelperAdding(){
        for (int i = 0; i < 10; i++){
            String id = projectsDAO.generateNewProjectIdHelper();
            assertEquals(String.valueOf(i + 1), id);

            ArrayList<Project> projectsToAdd = new ArrayList<>();
            Project matchingDummy = dummyProjects.get(i);
            projectsToAdd.add(PROJECT_FACTORY.create(id, matchingDummy.getProjectName(),
                    matchingDummy.getProjectDescription(), matchingDummy.getTasks()));
            projectsDAO.saveProjects(projectsToAdd);
        }

        ArrayList<Project> returnedProjects = projectsDAO.getProjects(dummyIds);
        for (int j = 0; j < 10; j++){
            assertEquals(dummyProjects.get(j).getProjectId(), returnedProjects.get(j).getProjectId());
        }
    }

    @Test
    public void testGenerateNewProjectIdHelperDeleting(){
        projectsDAO.saveProjects(dummyProjects);
        ArrayList<Integer> leftoverIdNums = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            leftoverIdNums.add(i + 1);
        }

        while (!leftoverIdNums.isEmpty()){
            Integer i = (int) (Math.random() * 10) + 1;
            if (leftoverIdNums.contains(i)){
                leftoverIdNums.remove(i);
                int maxPlusOne = 1;
                if (!leftoverIdNums.isEmpty()){
                    maxPlusOne = leftoverIdNums.get(leftoverIdNums.size() - 1) + 1;
                }

                projectsDAO.deleteProject(String.valueOf(i));

                assertEquals(String.valueOf(maxPlusOne), projectsDAO.generateNewProjectIdHelper());
            }
        }
    }

    @Test
    public void testSaveProjectsExisted(){
        projectsDAO.saveProjects(dummyProjects);
        assertEquals("11", projectsDAO.generateNewProjectIdHelper());
    }

    @After
    public void cleanUp(){
        File testProjectsDatabase = new File(PROJECTS_PATH);
        if (!testProjectsDatabase.delete()){
            System.out.println(PROJECTS_PATH + " did not delete properly after testing.");
        };
    }

}
