package data_access;

import entity.User;
import entity.UserFactory;
import entity.Project;
import use_case.add_project.AddProjectDataAccessInterface;
import use_case.delete_project.DeleteProjectDataAccessInterface;
import use_case.login.LoginDataAccessInterface;
import use_case.send_notification.NotificationUsersDataAccessInterface;
import use_case.signup.SignupDataAccessInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This DAO interacts with information of users (username, password, list of projects).
 * It DOES NOT have project information.
 */

public class UsersDataAccessObject implements SignupDataAccessInterface, LoginDataAccessInterface, DeleteProjectDataAccessInterface, NotificationUsersDataAccessInterface, AddProjectDataAccessInterface {

    //    This csv file connects user emails to the project IDs that it is associated with.
    private final File usersCsvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private UserFactory userFactory;
    private ProjectsDataAccessObject projectsDAO;
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
                writer.write(String.join("%%", headers.keySet()));
                writer.newLine();
            }
        }
    }

    /**
     * WARNING: always generates the same ID unless the number of projects in the database
     * changes AND IS SAVED.
     * To add projects in bulk (entity side) without saving, you can increment the return value from this method by one
     * for each additional project.
     * @return a unique projectId that is not in use for any other project THAT IS SAVED.
     */
    public String generateNewProjectId(){
        return projectsDAO.generateNewProjectIdHelper();
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
                userExists = true;
                String[] col = row.split("%%");
                String currentName = String.valueOf(col[headers.get("username")]);

//                if the user exists, overwrite it in the modified file content
                if (currentName.equals(username)){
                    String password = String.valueOf(col[headers.get("password")]);

                    if (user.getProjects().isEmpty()){
                        row = username + "%%" + password + "%%" + "-1";
                    } else {
                        ArrayList<String> newProjectIds = new ArrayList<>();
                        for (Project project : user.getProjects()) {
                            newProjectIds.add(project.getProjectId());
                        }

//                    Check for deleted projects
                        String[] oldProjectIds = String.valueOf(col[headers.get("projectIDs")]).split(";");
                        for (int i = 0; i < oldProjectIds.length; i++) {
                            if (!newProjectIds.contains(oldProjectIds[i])) {
                                projectsDAO.deleteProject(oldProjectIds[i]);
                            }
                        }
//                    Create the new projectIDs string
                        StringBuilder projectIDs = new StringBuilder();
                        for (String id : newProjectIds) {
                            projectIDs.append(id).append(";");
                        }

                        row = username + "%%" + password + "%%" + projectIDs;
                    }
                }

                inputBuffer.append(row);
                inputBuffer.append('\n');
            }

//            if user does not exist, append it in the modified file content
//            ProjectID is set to -1 to indicate there are no projects associated with the user
            if (!userExists){
                inputBuffer.append(username + "%%" + user.getPassword() + "%%" + "-1").append('\n');
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
            assert header.equals("username%%password%%projectIDs");

            String row;
            while ((row = reader.readLine()) != null) {
                String[] col = row.split("%%");
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
