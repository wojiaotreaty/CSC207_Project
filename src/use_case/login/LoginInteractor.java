package use_case.login;

import entity.User;
import java.util.ArrayList;

public class LoginInteractor implements LoginInputBoundary {
    final LoginDataAccessInterface userDataAccessObject;
    final LoginOutputBoundary loginPresenter;
    public ArrayList<ArrayList<String>> projectData;

    public LoginInteractor(LoginDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        String username = loginInputData.getUsername();
        String password = loginInputData.getPassword();
        if (userDataAccessObject.getUser(username) == null) {
            loginPresenter.prepareFailView(username + ": Account does not exist.");
        } else {
            String pwd = userDataAccessObject.getUser(username).getPassword(); // User needs a method getPassword()
            if (!password.equals(pwd)) {
                loginPresenter.prepareFailView("Incorrect password for " + username + ".");
            } else {

                User user = userDataAccessObject.getUser(loginInputData.getUsername());

                ArrayList<Project> listofproject = user.getProjects(); // get arraylist of projects
                for (Project project: listofproject) {
                    ArrayList<Task> tasks = project.getTasks(); // return arraylist of tasks for the project
                    String stringTasks = "";
                    for (Task task : tasks) {
                        stringTasks.concat(task.toString());
                        stringTasks.concat("|uwu|");

                    }
                    ArrayList<String> projectList = new ArrayList<String>();
                    projectList.add(project.getID()); // project id
                    projectList.add(project.getName()); // project name
                    projectList.add(project.getDesc()); // project description
                    projectList.add(stringTasks); // project tasks
                    projectData.add(projectList);
                }


                LoginOutputData loginOutputData = new LoginOutputData(user.getUsername(), projectData, false);
                loginPresenter.prepareSuccessView(loginOutputData);
            }
        }
    }
}