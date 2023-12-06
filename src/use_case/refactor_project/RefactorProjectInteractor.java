package use_case.refactor_project;
import entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;

public class RefactorProjectInteractor implements RefactorProjectInputBoundary {
        final private RefactorProjectDataAccessInterface userDataAccessObject;
        final private RefactorProjectOutputBoundary userPresenter;
        final private ProjectFactory projectFactory;
        final private TaskFactory taskFactory;

        public RefactorProjectInteractor(RefactorProjectDataAccessInterface refactorProjectDataAccessInterface,
                                         RefactorProjectOutputBoundary refactorProjectOutputBoundary, ProjectFactory projectFactory, TaskFactory taskFactory) {
            this.userDataAccessObject = refactorProjectDataAccessInterface;
            this.userPresenter = refactorProjectOutputBoundary;
            this.projectFactory=projectFactory;
            this.taskFactory=taskFactory;
        }

        @Override
        public void execute(RefactorProjectInputData refactorProjectInputData) {
            // Getting the time right now
            LocalDate now = LocalDate.now();
            // getting the user and the project Id from the dao
            User user = userDataAccessObject.getUser(refactorProjectInputData.getUserName());
            String projectID = refactorProjectInputData.getId();
            ArrayList<Project> projects = new ArrayList<>();
            projects = user.getProjects();
            Project project = null;
            for (Project project1 : projects) {
                if (project1.getProjectId().equals(projectID)) {
                    project = project1;
                }
            }
            if (project == null) {
                throw new RuntimeException("Error occured while refactoring the project");
            }
            ArrayList<Task> tasks = project.getTasks();
            ArrayList<Task> incomplete_tasks = new ArrayList<>();
            ArrayList<Task> complete_tasks = new ArrayList<>();
            for (Task task : tasks) {
                if (task.getStatus() == false) {
                    incomplete_tasks.add(task);
                } else {
                    complete_tasks.add(task);
                }
            }
            //getting the final task since the final task has the same deadline as the project.
            Task finalTask = tasks.get(tasks.size() - 1);
            // getting the deadline
            LocalDate deadline = finalTask.getDeadline();
            // assuming the TasksDeadline is in "YYYY-MM-DD" format
            // claculating the time difference between the current date and the deadline date
            LocalDateTime q =deadline.atStartOfDay();
            LocalDateTime v=now.atStartOfDay();
            long timeDifference= ChronoUnit.DAYS.between(v,q)*86400000;
            if (timeDifference > 0 || timeDifference == 0) {
                double deciDays = timeDifference / 86400000;

                double time_to_add = deciDays / incomplete_tasks.size();
                long days = Math.round(time_to_add);
                ArrayList<String> list_task = new ArrayList<String>();
                for (int i = 0; i < incomplete_tasks.size(); i++) {
                    LocalDate taskDeadline = incomplete_tasks.get(i).getDeadline();

                    LocalDate shiftedTaskDeadline = taskDeadline.plusDays(days);
                    Task task = incomplete_tasks.get(i);

                    Task shiftedTask = taskFactory.create(task.getName(), shiftedTaskDeadline, task.getDescription());

                    incomplete_tasks.set(i, shiftedTask);
//                String t=task.toStringUwu();
//                list_task.add(t);
//                userDataAccessObject.setTaskDeadline(projectID,tasks.get(i).getId(),shiftedTaskDeadline);
                }
                ArrayList<Task> updated_tasks = new ArrayList<>();

                updated_tasks.addAll(complete_tasks);

                updated_tasks.addAll(incomplete_tasks);
                Project refactored_project = projectFactory.create(projectID, project.getProjectName(), project.getProjectDescription(), updated_tasks);

                Project old_project = user.deleteProject(projectID);
                user.addProject(refactored_project);

                userDataAccessObject.saveUser(user);
                // TODO:Update the project entities
                StringBuilder list_tasks = new StringBuilder();
                for(Task task:updated_tasks){
                   String new_task= task.toString();
                   list_tasks.append(new_task).append("|uwu|");
                }
                RefactorProjectOutputData refactorProjectOutputData = new RefactorProjectOutputData(projectID,project.getProjectName(),project.getProjectDescription(), String.valueOf(list_tasks));

                userPresenter.prepareSuccessView(refactorProjectOutputData);
            }
            else{
                userPresenter.prepareFailView("unable to refactor since the deadline has already crossed");
            }
        }
    }
