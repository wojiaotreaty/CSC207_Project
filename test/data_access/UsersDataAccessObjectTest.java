package data_access;

import entity.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class UsersDataAccessObjectTest {

    private final String PROJECTS_PATH = "./projects_test.csv";
    private final String USERS_PATH = "./users_test.csv";
    private final CommonProjectFactory PROJECT_FACTORY = new CommonProjectFactory();
    private final CommonTaskFactory TASK_FACTORY = new CommonTaskFactory();
    private final CommonUserFactory USER_FACTORY = new CommonUserFactory();

    private final String username1 = "username1";
    private final String username2 = "username2";
    private final String password1 = "password1";
    private final String password2 = "password2";

    private User dummyUser1;
    private User dummyUser2;
    private UsersDataAccessObject usersDAO;
    private ArrayList<Project> dummyProjectsTen;
    private ArrayList<Project> dummyProjectsTenMore;


    @Before
    public void init(){
        try {
            ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(
                    PROJECTS_PATH, PROJECT_FACTORY, TASK_FACTORY);
            this.usersDAO = new UsersDataAccessObject(USERS_PATH, USER_FACTORY, projectsDAO);
        } catch (IOException error){
            System.out.println("ERROR: IOexception when creating ProjectDAO");
        }

        this.dummyProjectsTen = DataAccessObjectTestHelper.getDummyProjectsTen(PROJECT_FACTORY, TASK_FACTORY);
        this.dummyProjectsTenMore = DataAccessObjectTestHelper.getDummyProjectsTenMore(PROJECT_FACTORY, TASK_FACTORY);

        this.dummyUser1 = USER_FACTORY.create(username1, password1);
        this.dummyUser2 = USER_FACTORY.create(username2, password2);
    }

    @Test
    public void testSaveUserNew(){
        assert !usersDAO.saveUser(dummyUser2);
    }

    @Test
    public void testSaveUserExisted(){
        usersDAO.saveUser(dummyUser1);
        for (Project dummyProject : dummyProjectsTen) {
            dummyUser1.addProject(dummyProject);
        }
        assert usersDAO.saveUser(dummyUser1);
    }

    @Test
    public void testGetUserDoesNotExist(){
        assertNull(usersDAO.getUser("username3"));
    }

    @Test
    public void testGetUserExists(){
        assertEquals(usersDAO.getUser("username1"), dummyUser1);
        assertEquals(usersDAO.getUser("username2"), dummyUser2);
    }

    @Test
    public void testGenerateNewProjectIdAdding(){
        for (int i = 10; i < 20; i++){
            Project matchingProject = dummyProjectsTenMore.get(i - 10);

            String generatedId = usersDAO.generateNewProjectId();

            Project projectToAdd = PROJECT_FACTORY.create(generatedId, matchingProject.getProjectName(),
                    matchingProject.getProjectDescription(), matchingProject.getTasks());

            dummyUser2.addProject(projectToAdd);
        }

        ArrayList<Project> userProjects = dummyUser2.getProjects();

        for (int i = 0; i < 10; i++){
            assertEquals(dummyProjectsTenMore.get(i).getProjectId(), userProjects.get(i).getProjectId());
        }
    }

    @Test
    public void testGenerateNewProjectIdDeleting(){
        ArrayList<Integer> leftoverIdNums = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            leftoverIdNums.add(i + 1);
        }

        while (!leftoverIdNums.isEmpty()){
            Integer i = (int) (Math.random() * 20) + 1;
            if (leftoverIdNums.contains(i)){
                leftoverIdNums.remove(i);
                int maxPlusOne = leftoverIdNums.get(leftoverIdNums.size() - 1) + 1;

                if (i <= 10){
                    dummyUser1.deleteProject(i.toString());
                } else {
                    dummyUser2.deleteProject(i.toString());
                }

                assertEquals(String.valueOf(maxPlusOne), usersDAO.generateNewProjectId());
            }
        }
    }

    @After
    public void cleanUp(){
        File testProjectsDatabase = new File(PROJECTS_PATH);
        if (!testProjectsDatabase.delete()){
            System.out.println(PROJECTS_PATH + " did not delete properly after testing.");
        };
        File testUsersDatabase = new File(USERS_PATH);
        if (!testUsersDatabase.delete()){
            System.out.println(USERS_PATH + " did not delete properly after testing.");
        };
    }

    /**
     * helper method for wiping the slate clean.
     * Works by deleting both databases and reconstructing the UsersDAO that is tested.
     */
    private void deleteAll(){
        this.cleanUp();

        try {
            ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(
                    PROJECTS_PATH, PROJECT_FACTORY, TASK_FACTORY);
            this.usersDAO = new UsersDataAccessObject(USERS_PATH, USER_FACTORY, projectsDAO);
        } catch (IOException error){
            System.out.println("ERROR: IOexception when creating ProjectDAO");
        }
    }

}
