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
            Project project = refactorProjectInputData.getProject();
            ArrayList<HashMap<String, String>> tasks = refactorProjectInputData.getTasks();
            int task_index = 0;
            while (tasks.get(task_index).get("Done") == "1") {
                task_index++;
            }
            //getting the task which was completed most recently just before the deadline
            HashMap<String, String> completed_task = tasks.get(task_index - 1);
            // getting the deadline
            String deadline = completed_task.get("TaskDeadline");
            // assuming the TasksDeadline is in "YYYY-MM-DD" format
            SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
            Date deadline_date = null;
            try {
                deadline_date = formatter.parse(deadline);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            // claculating the time difference between the current date and the deadline date
            long time_difference = deadline_date.getTime() - now.getTime();
            double deci_days = time_difference / 86400000;
            long days = Math.round(deci_days);
            ArrayList<LocalDate> dates = new ArrayList<>();
            for (int i = task_index; i < tasks.size(); i++) {

            }
        }
    }
