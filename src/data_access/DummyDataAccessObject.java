package data_access;

import use_case.add_project.AddProjectDataAccessInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the DAO that will be used in unit testing in the beninging.
 * Uses memory; a new list of valid new Users will have to be created each time any tests are run
 */
public class DummyDataAccessObject implements AddProjectDataAccessInterface{

//    NOTE: this part simulates Google Calendar. We would not need to store this normally.
    HashMap<String, String> credentialsInMemory;
    HashMap<String, UserCalendar> gmailCalendarMap;

//    This part is necessary
    String currentUserGmail;
    UserCalendar currentCalendar;

    public boolean existsByGmail(String gmail) {
        return credentialsInMemory.containsKey(gmail);
    }

    /**
     * NOTE: this method should exclusively be used for building tests with blank users. <br>
     * Creates a new user in memory with an empty calendar. <br>
     * If there already exists a user with the same gmail, the old one will be overwritten.
     */
    public void createUser(String gmail, String password){
        credentialsInMemory.put(gmail, password);
        gmailCalendarMap.put(gmail, new UserCalendar());
    }

    /**
     * NOTE: this method should exclusively be used for building tests with blank users. <br>
     * Creates a new user in memory with a calendar that includes the given tasks. <br>
     * If there already exists a user with the same gmail, the old one will be overwritten.
     */
    public void createUser(String gmail, String password, ArrayList<HashMap<String, String>> tasks){
        credentialsInMemory.put(gmail, password);
        gmailCalendarMap.put(gmail, new UserCalendar(tasks));
    }

    /**
     * PRECONDITION: the given gmail exists in the list of credentials in memory <br>
     * If the password is correct, switches/sets the current user to the one with the given gmail.
     * @return true if login successful, false if incorrect password
     */
    public boolean login(String gmail, String password){
        if (credentialsInMemory.get(gmail).equals(password)){
            currentUserGmail = gmail;
            currentCalendar = gmailCalendarMap.get(gmail);
            return true;
        } return false;
    }

    /**
     * @return whether the calendar associated with the current user has a given task.
     */
    public boolean hasTask(HashMap<String, String> task){
        return currentCalendar.hasTask(task);
    }

    /**
     * @return the tasks in the calendar associated with the current user.
     */
    public ArrayList<HashMap<String, String>> getTasks(){
        return currentCalendar.tasks;
    }

    @Override
    public void saveTasks(ArrayList<HashMap<String, String>> tasks){
        currentCalendar.tasks.addAll(tasks);
    }

    /**
     * Helper class to simulate Google Calendar.
     */
    private class UserCalendar {
        ArrayList<HashMap<String, String>> tasks;

        UserCalendar(){};

        UserCalendar(ArrayList<HashMap<String, String>> tasks) {
            this.tasks = tasks;
        };

        public boolean hasTask(HashMap<String, String> task) {
            for (HashMap<String, String> taskInCalendar : tasks){
                if (taskInCalendar.equals(task)) return true;
            }
            return false;
        }
    }
}
