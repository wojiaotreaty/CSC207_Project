package data_access;

import entity.Project;
import entity.User;
import entity.UserFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This DAO interacts with information of users (username, password, list of projects).
 * It DOES NOT have project information.
 */

public class UsersDataAccessObject {

//    This csv file connects user emails to the project IDs that it is associated with.
    private final File usersCsvFile;

    private final Map<String, Integer> headers = new LinkedHashMap<>();

    private UserFactory userFactory;

    private ProjectsDataAccessObject projectsDAO;

    private User currentUser;


    /**
     * Note that no Users are built at time of DAO construction.
     * Instead, the currentUser (only) is built on successful login.
     */
    public UsersDataAccessObject(String usersCsvPath, UserFactory userFactory, ProjectsDataAccessObject projectsDAO) throws IOException {
        this.userFactory = userFactory;
        this.projectsDAO = projectsDAO;

        usersCsvFile = new File(usersCsvPath);
        headers.put("username", 0);
        headers.put("password", 1);
        headers.put("projectIDs", 2);

        if (usersCsvFile.length() == 0) {
//            If no existing csv file exists, write a csv file that only contains the headers
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersCsvFile))) {
                writer.write(String.join(",", headers.keySet()));
                writer.newLine();
            }
        }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    /**
     * Use this method when you are only operating on the currentUser.
     */
    public void saveUser(){
        saveUser(currentUser);
    }

    /**
     * Adds a user with no projects if user does not exist yet.
     * If the user does exist, save it (saves all the projects it owns and updates user info).
     * Does not allow for changing passwords.
     * @return true if the user previously existed in the database, false if not.
     */
    public boolean saveUser(User user) {
        String username = user.getUsername();
        boolean userExists = false;

//        Save all the projects that user is associated with
        projectsDAO.saveProjects(user.getProjects());

        try {
            // input the (modified) file content to the StringBuffer "input"
            BufferedReader reader = new BufferedReader(new FileReader(usersCsvFile));
            StringBuffer inputBuffer = new StringBuffer();

            String row;
            while ((row = reader.readLine()) != null) {
                String[] col = row.split(",");
                String currentName = String.valueOf(col[headers.get("username")]);

//                if the user exists, overwrite it in the modified file content
                if (currentName.equals(username)){
                    String password = String.valueOf(col[headers.get("password")]);
                    StringBuilder projectIDs = new StringBuilder();

                    for (Project project : user.getProjects()){
                        projectIDs.append(project.getProjectID()).append(";");
                    }

                    row = username + "," + password + "," + projectIDs;
                    userExists = true;
                }

                inputBuffer.append(row);
                inputBuffer.append('\n');
            }

//            if user does not exist, append it in the modified file content
//            ProjectID is set to -1 to indicate there are no projects associated with the user
            if (!userExists){
                inputBuffer.append(username + "," + user.getPassword() + "," + "-1");
            }

            reader.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(usersCsvFile);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();

            return userExists;

        } catch (IOException e) {
            throw new RuntimeException("ERROR: problem reading file when saving the user");
        }
    }


    /**
     * Precondition: username exists in the database.
     * @return the User if successful. null if no user with this username exists in the database.
     */
    public User getUser(String username){
        try (BufferedReader reader = new BufferedReader(new FileReader(usersCsvFile))) {
            String header = reader.readLine();
            // For later: clean this up by creating a new Exception subclass and handling it in the UI.
            assert header.equals("username,password,projectIDs");

            String row;
            while ((row = reader.readLine()) != null) {
                String[] col = row.split(",");
                String currentName = String.valueOf(col[headers.get("username")]);
                String currentPassword = String.valueOf(col[headers.get("password")]);
                if (currentName.equals(username)){
                    String projectIDs = String.valueOf(col[headers.get("projectIDs")]);

//                    If there is no project associated with the user
                    if (projectIDs.equals("-1")){
                        return userFactory.create(currentName, currentPassword, new ArrayList<>());
                    }

                    String[] projectIDsCol = projectIDs.split(";");
                    ArrayList<Project> projects = projectsDAO.getProjects(projectIDsCol);
                    return userFactory.create(currentName, currentPassword, projects);
                }
            }
            return null;

        } catch (IOException e){
            throw new RuntimeException("ERROR: problem reading file when getting the user");
        }
    }


}
