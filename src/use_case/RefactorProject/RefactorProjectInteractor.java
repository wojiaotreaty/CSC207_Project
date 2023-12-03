package use_case.RefactorProject;
import entity.Project;
import entity.ProjectFactory;
import entity.Task;
import entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

    public class RefactorProjectInteractor implements RefactorProjectInputBoundary {
        final RefactorProjectDataAccessInterface userDataAccessObject;
        final RefactorProjectOutputBoundary userPresenter;
        final ProjectFactory projectFactory;

        public RefactorProjectInteractor(RefactorProjectDataAccessInterface refactorProjectDataAccessInterface,
                                         RefactorProjectOutputBoundary refactorProjectOutputBoundary, ProjectFactory projectFactory) {
            this.userDataAccessObject = refactorProjectDataAccessInterface;
            this.userPresenter = refactorProjectOutputBoundary;
            this.projectFactory=projectFactory;
        }

        @Override
        public void execute(RefactorProjectInputData refactorProjectInputData) {
            // Getting the time right now
            LocalDateTime now = LocalDateTime.now();
            // getting the user and the project Id from the dao
            User user = userDataAccessObject.getUser(refactorProjectInputData.getUserName());
            String  projectID = refactorProjectInputData.getId();
            Project project= user.getProject(projectID);
            if (project == null){
                throw new RuntimeException("Error occured while refactoring the project");
            }
            ArrayList<Task> tasks = project.getTasks();
            ArrayList<Task> incomplete_tasks = new ArrayList<>();
            ArrayList<Task>complete_tasks =new ArrayList<>();
            for (Task task:tasks){
                if (task.getStatus() == false)
                {
                    incomplete_tasks.add(task);
                }
                else{
                    complete_tasks.add(task);
                }
            }
            //getting the final task since the final task has the same deadline as the project.
           Task finalTask = tasks.get(tasks.size()- 1);
            // getting the deadline
            LocalDate deadline = finalTask.getDeadline();
            // assuming the TasksDeadline is in "YYYY-MM-DD" format
            // claculating the time difference between the current date and the deadline date
            long timeDifference = now.until(deadline, ChronoUnit.MILLIS);
            double deciDays = timeDifference / 86400000;
            double time_to_add = deciDays/ incomplete_tasks.size();
            long days = Math.round(time_to_add);
            ArrayList<String> list_task = new ArrayList<String>();
            for (int i = 0; i < incomplete_tasks.size(); i++) {
                LocalDate taskDeadline = incomplete_tasks.get(i).getDeadline();
                LocalDate shiftedTaskDeadline = taskDeadline.plusDays(days);
                Task task=incomplete_tasks.get(i);
                task.setDeadline(shiftedTaskDeadline);
                incomplete_tasks.add(i,task);
//                String t=task.toStringUwu();
//                list_task.add(t);
//                userDataAccessObject.setTaskDeadline(projectID,tasks.get(i).getId(),shiftedTaskDeadline);
            }
            ArrayList<Task>updated_tasks=new ArrayList<>();
            updated_tasks .addAll(complete_tasks);
            updated_tasks.addAll(incomplete_tasks);
            Project refactored_project =projectFactory.create(projectID,project.getProjectName(),project.getProjectDescription(),updated_tasks);
            Project old_project = user.deleteProject(projectID);
            user.addProject(refactored_project);
            userDataAccessObject.saveUser(user);
            // TODO:Update the project entities
            RefactorProjectOutputData refactorProjectOutputData = new RefactorProjectOutputData(old_project,refactored_project);
            userPresenter.prepareSuccessView(refactorProjectOutputData);

        }
    }
