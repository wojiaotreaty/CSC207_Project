package use_case.RefactorProject;

import entity.Project;
import entity.ProjectFactory;
import entity.TaskFactory;
import entity.User;

import java.time.LocalDate;
import java.util.ArrayList;

public class RefactorProjectInteractor implements RefactorProjectInputBoundary {
    final private RefactorProjectDataAccessInterface userDataAccessObject;
    final private RefactorProjectOutputBoundary userPresenter;
    final private ProjectFactory projectFactory;
    final private TaskFactory taskFactory;

    public RefactorProjectInteractor(RefactorProjectDataAccessInterface refactorProjectDataAccessInterface,
                                     RefactorProjectOutputBoundary refactorProjectOutputBoundary, ProjectFactory projectFactory, TaskFactory taskFactory) {
        this.userDataAccessObject = refactorProjectDataAccessInterface;
        this.userPresenter = refactorProjectOutputBoundary;
        this.projectFactory = projectFactory;
        this.taskFactory = taskFactory;
    }

    @Override
    public void execute(RefactorProjectInputData refactorProjectInputData) {
        // Getting the time right now
        LocalDate now = LocalDate.now();
        // getting the user and the project I'd from the dao
        User user = userDataAccessObject.getUser(refactorProjectInputData.getUserName());
        String projectID = refactorProjectInputData.getId();
        ArrayList<Project> projects = new ArrayList<>();
        projects = user.getProjects();
        // Have set it to null in the beginning so that it can throw an error if the
        // project ID is not in the array(which should ideally never happen)
        Project project = null;
        for (Project project1 : projects) {
            if (project1.getProjectId().equals(projectID)) {
                project = project1;
            }
        }

    }
}
