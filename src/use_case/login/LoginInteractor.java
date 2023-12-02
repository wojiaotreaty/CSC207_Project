package use_case.login;

import entity.Project;
import entity.Task;
import entity.User;

import java.util.ArrayList;

public class LoginInteractor implements LoginInputBoundary {
    private final LoginDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;
    private ArrayList<ArrayList<String>> projectData = new ArrayList<ArrayList<String>>();

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

                ArrayList<Project> listOfProject = user.getProjects(); // get arraylist of projects
                for (Project project: listOfProject) {
                    ArrayList<Task> tasks = project.getTasks(); // return arraylist of tasks for the project
                    StringBuilder stringTasks = new StringBuilder();
                    for (Task task : tasks) {
                        stringTasks = stringTasks.append(task.toString());
                        stringTasks = stringTasks.append("|uwu|");

                    }
                    ArrayList<String> projectList = new ArrayList<String>();
                    projectList.add(project.getProjectId()); // project id
                    projectList.add(project.getProjectName()); // project name
                    projectList.add(project.getProjectDescription()); // project description
                    projectList.add(stringTasks.toString()); // project tasks
                    projectData.add(projectList);
                }


                LoginOutputData loginOutputData = new LoginOutputData(user.getUsername(), projectData, false);
                loginPresenter.prepareSuccessView(loginOutputData);
            }
        }
    }
}