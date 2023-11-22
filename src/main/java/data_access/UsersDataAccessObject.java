package data_access;

import entity.Project;
import entity.User;
import entity.UserFactory;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This DAO interacts with information of users (email, list of projects).
 * It DOES NOT have project information.
 */

public class UsersDataAccessObject {

//    This csv file connects user emails to the project IDs that it is associated with.
    private final File usersCsvFile;

    private final Map<String, Integer> headers = new LinkedHashMap<>();

    private UserFactory userFactory;

    private ProjectsDataAccessObject projectsDAO;

    private User currentUser;


//    TODO: finish the UsersDAO constructor
    /**
     * Note that no Users are built at time of DAO construction.
     * Instead, only the currentUser is built on successful login.
     */
    public UsersDataAccessObject(String usersCsvPath, UserFactory userFactory, ProjectsDataAccessObject projectsDAO) throws IOException {
        this.userFactory = userFactory;
        this.projectsDAO = projectsDAO;

        usersCsvFile = new File(usersCsvPath);
        headers.put("email", 0);
        headers.put("projectIDs", 1);

        if (usersCsvFile.length() == 0) {
//            If no existing csv file exists, write a csv file that only contains the headers
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersCsvFile))) {
                writer.write(String.join(",", headers.keySet()));
                writer.newLine();
            }
        }
    }


//    Do this when we are just operating on the current user.
    private void saveUser(){
        saveUser(currentUser.getEmail());
    }

//    TODO: finish the saveUser method
    /**
     * Adds the user if it does not exist yet.
     * If the user does exist, save it.
     */
    private void saveUser(String email) {

    }


    /**
     * Uses the Google API to log in the user.
     * If the user does not exist in our local database, create a matching entry.
     * If successful, builds the matching User and sets it to currentUser.
     * @param email
     */
//    TODO: finish the login method
//    Calls Google API for authentication
//    Calls ProjectDAO for building the projects
    public void login(String email, String password){

//        TODO: authentication using Google


        try (BufferedReader reader = new BufferedReader(new FileReader(usersCsvFile))) {
            String header = reader.readLine();
            // For later: clean this up by creating a new Exception subclass and handling it in the UI.
            assert header.equals("email,projectIDs");

            String row;
            while ((row = reader.readLine()) != null) {
                String[] col = row.split(",");
                String currentEmail = String.valueOf(col[headers.get("email")]);
                if (currentEmail.equals(email)){
                    String projectIDs = String.valueOf(col[headers.get("projectIDs")]);
                    String[] projectIDsCol = projectIDs.split(",");
                    ArrayList<Project> projects = new ArrayList<>();
                    for (String id : projectIDsCol){
//                        projects.add(projectsDAO.get(id));
                    }
                    User user = userFactory.create(currentEmail, projects);
                    currentUser = user;
                    return;
                }
            }

//            TODO: handle case where user does not exist in local database


        } catch (IOException e){
//            TODO: what to do when it is caught?
        }
    }
}
