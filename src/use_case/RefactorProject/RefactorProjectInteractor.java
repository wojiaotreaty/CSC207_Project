package use_case.RefactorProject;
import entity.Project;
import entity.ProjectFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

    public class RefactorProjectInteractor implements RefactorProjectInputBoundary {
        final RefactorProjectDataAccessInterface userDataAccessObject;
        final RefactorProjectOutputBoundary userPresenter;
        final ProjectFactory projectFactory;

        public RefactorProjectInteractor(RefactorProjectDataAccessInterface refactorProjectDataAccessInterface,
                                         RefactorProjectOutputBoundary refactorProjectOutputBoundary, ProjectFactory projectFactory) {
            this.userDataAccessObject = refactorProjectDataAccessInterface;
            this.userPresenter = refactorProjectOutputBoundary;
            this.projectFactory = projectFactory;
        }

        @Override
        public void execute(RefactorProjectInputData refactorProjectInputData) {
            // Getting the time right now
            LocalDateTime now = LocalDateTime.now();
            String  projectID = refactorProjectInputData.id;
            // getting the project from the DAO
            String [] l= {projectID};
            Project project;
            project = userDataAccessObject.getProjects(l).get(0);
            ArrayList<Task> tasks = project.getTasks();
            int taskIndex = 0;

            while (tasks.get(taskIndex).getStatus == true) {
                taskIndex=taskIndex+1;
            }
            //getting the task which was completed most recently just before the deadline
           Task completedTask = tasks.get(taskIndex - 1);
            // getting the deadline
            LocalDate deadline = completedTask.getDeadline();
            // assuming the TasksDeadline is in "YYYY-MM-DD" format
            // claculating the time difference between the current date and the deadline date
            long timeDifference = deadline.until(now, ChronoUnit.MILLIS);
            double deciDays = timeDifference / 86400000;
            long days = Math.round(deciDays);
            ArrayList<String> list_task = new ArrayList<String>();
            for (int i = taskIndex; i < tasks.size(); i++) {
                LocalDate taskDeadline = tasks.get(i).getDeadline();
                LocalDate shiftedTaskDeadline = taskDeadline.minusDays(days);
                Task task=tasks.get(i);
                task.setDeadline(shiftedTaskDeadline);

                String t=task.toStringUwu();
                list_task.add(t);
                userDataAccessObject.setTaskDeadline(projectID,tasks.get(i).getId(),shiftedTaskDeadline);
            }
            // TODO:Update the project entities
            RefactorProjectOutputData refactorProjectOutputData = new RefactorProjectOutputData(list_task);
            userPresenter.prepareSuccessView(refactorProjectOutputData);

        }
    }
