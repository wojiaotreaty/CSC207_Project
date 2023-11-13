package data_access;

import use_case.add_project.AddProjectDataAccessInterface

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the DAO that will be used in unit testing in the beninging.
 * Uses memory; a new list of valid new Users will have to be created each time any tests are run
 */
public class DummyDataAccessObject implements AddProjectDataAccessInterface{

    HashMap<String, String> credentialsInMemory;



    public void saveTasks(ArrayList<HashMap<String, String>> tasks){

        for (int i = 0; i < tasks.size(); i++){

        }

    }
}
