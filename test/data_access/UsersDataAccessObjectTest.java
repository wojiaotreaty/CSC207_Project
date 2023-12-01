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
    private ArrayList<Project> dummyProjects;
    private String[] dummyIds;


    @Before
    public void init(){
        try {
            ProjectsDataAccessObject projectsDAO = new ProjectsDataAccessObject(
                    PROJECTS_PATH, PROJECT_FACTORY, TASK_FACTORY);
            this.usersDAO = new UsersDataAccessObject(USERS_PATH, USER_FACTORY, projectsDAO);
        } catch (IOException error){
            System.out.println("ERROR: IOexception when creating ProjectDAO");
        }

        this.dummyProjects = DataAccessObjectTestHelper.getDummyProjectsTen(PROJECT_FACTORY, TASK_FACTORY);

        this.dummyIds = DataAccessObjectTestHelper.getDummyIdTen();

        this.dummyUser1 = USER_FACTORY.create(username1, password1);
        this.dummyUser2 = USER_FACTORY.create(username2, password2);
    }

    @Test
    public void testSaveUserNew(){
        assert !usersDAO.saveUser(dummyUser1);
    }

    @Test
    public void testSaveUserExisted(){
        usersDAO.saveUser(dummyUser2);
        for (Project dummyProject : dummyProjects) {
            dummyUser2.addProject(dummyProject);
        }
        assert usersDAO.saveUser(dummyUser2);
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
    public void testGenerateNewProjectId(){


        for (int i = 0; i < 10; i++){
            String id = projectsDAO.generateNewProjectIdHelper();
            assertEquals(String.valueOf(i + 1), id);

            ArrayList<Project> projectsToAdd = new ArrayList<>();
            projectsToAdd.add(
                    PROJECT_FACTORY.create(id, "Project " + i, "dummy", new ArrayList<>()));
            projectsDAO.saveProjects(projectsToAdd);
        }

        ArrayList<Project> returnedProjects = projectsDAO.getProjects(dummyIds);
        for (int j = 0; j < 10; j++){
            assertEquals(dummyProjects.get(j), returnedProjects.get(j));
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
