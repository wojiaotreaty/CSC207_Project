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
            Date now = new Date();
            Project Project = refactorProjectInputData.getProject();
            ArrayList<HashMap<String, String>> tasks = refactorProjectInputData.getTasks();
            int taskIndex = 0;
            while (tasks.get(taskIndex).get("Done") == "1") {
                taskIndex++;
            }
            //getting the task which was completed most recently just before the deadline
            HashMap<String, String> completedTask = tasks.get(taskIndex - 1);
            // getting the deadline
            String deadline = completedTask.get("TaskDeadline");
            // assuming the TasksDeadline is in "YYYY-MM-DD" format
            SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
            Date deadlineDate = null;
            try {
                deadlineDate = formatter.parse(deadline);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            // claculating the time difference between the current date and the deadline date
            long timeDifference = deadlineDate.getTime() - now.getTime();
            double deciDays = timeDifference / 86400000;
            long days = Math.round(deciDays);
            ArrayList<LocalDate> dates = new ArrayList<>();
            for (int i = taskIndex; i < tasks.size(); i++) {
                String taskDeadline=tasks.get(i).get("TaskDeadline");
                LocalDate date= LocalDate.parse(taskDeadline);
                 LocalDate shiftedTaskDeadline=date.minusDays(days);
                 String shiftedTaskDeadlineString=shiftedTaskDeadline.toString();
                 tasks.get(i).put("TaskDeadline",shiftedTaskDeadlineString);
            }

        }
    }
