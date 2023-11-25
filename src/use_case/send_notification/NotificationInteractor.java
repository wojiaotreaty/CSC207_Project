package use_case.send_notification;
import entity.Project;
import entity.Task;
import entity.User;
import use_case.send_notification.NotificationUsersDataAccessInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;

public class NotificationInteractor implements NotificationInputBoundary {
    final NotificationUsersDataAccessInterface notificationUsersDataAccessInterface;
    final NotificationOutputBoundary notificationPresenter;
    public NotificationInteractor(NotificationUsersDataAccessInterface notificationUsersDataAccessInterface,
                                  NotificationOutputBoundary notificationOutputBoundary) {
        this.notificationUsersDataAccessInterface = notificationUsersDataAccessInterface;
        this.notificationPresenter = notificationOutputBoundary;
    }
    @Override
    public void execute(NotificationInputData notificationInputData) {
        // Pulls current user from DAO and extracts their projects
        User user = notificationUsersDataAccessInterface.getCurrentUser();
        ArrayList<Project> projects = user.getProjects();
        // Initializes date and check dates for two days after today
        LocalDate date = notificationInputData.getDate();
        LocalDate plusOne = date.plusDays(1);
        LocalDate plusTwo = date.plusDays(2);
        // Initializes HashMaps that will be used to store upcoming tasks. The HashMap maps
        // Projects to an ArrayList of their Tasks with upcoming due dates appropriate to
        // the HashMap name.
        HashMap<Project, ArrayList<Task>> duePlusOne = new HashMap<Project, ArrayList<Task>>();
        HashMap<Project, ArrayList<Task>> duePlusTwo = new HashMap<Project, ArrayList<Task>>();
        HashMap<Project, ArrayList<Task>> dueToday = new HashMap<Project, ArrayList<Task>>();
        for (Project project : projects) {
            for (Task task : project.getTasks()) {
                // checks if the task is completed already
                if (!task.getStatus()) {
                    // checks if task is due tomorrow
                    if (task.getDeadline().equals(plusOne)) {
                        if (!duePlusOne.containsKey(project)) {
                            duePlusOne.put(project, new ArrayList<Task>());
                        }
                        duePlusOne.get(project).add(task);
                    // checks if task is due in two days
                    } else if (task.getDeadline().equals(plusTwo)) {
                        if (!duePlusTwo.containsKey(project)) {
                            duePlusTwo.put(project, new ArrayList<Task>());
                        }
                        duePlusTwo.get(project).add(task);
                    // checks if the task is due today
                    } else if (task.getDeadline().equals(date)) {
                        if (!dueToday.containsKey(project)) {
                            dueToday.put(project, new ArrayList<Task>());
                        }
                        dueToday.get(project).add(task);
                    }
                }
            }
        }
        // checks that at least one of the HashMaps is non-empty
        if (!duePlusOne.isEmpty() || !duePlusTwo.isEmpty() || !dueToday.isEmpty()) {
            // generates gpt query and executes the api call using helper functions
            String gptQuery = generateQuery(duePlusOne, duePlusTwo, dueToday);
            String gptOutput = apiCall(gptQuery);
        }



    }
    // Generates the string that will be used to query gpt
    private static String generateQuery(HashMap<Project, ArrayList<Task>> one, HashMap<Project, ArrayList<Task>> two,
                                        HashMap<Project, ArrayList<Task>> today) {
        // initializes the StringBuilder and briefly explains to gpt the situation.
        StringBuilder q = new StringBuilder("I am working on some projects right now and these are some " +
                "upcoming tasks I that are due. \n");
        // Note that all three if statements are essentially exactly the same as this one.
        // They just add information about the tasks due on their specific days.
        if (!today.isEmpty()) {
            // Curly brackets, {}, are used to encompass tasks due on the same day and
            // related to the same project.
            q.append("These tasks are due today: {\n");
            for (Project project : today.keySet()) {
                // The project description is omitted to save space in the call. I aim to have
                // gpt give a relatively brief response, and I feel that the task descriptions
                // along with the project title should be enough information to give some
                // quick advice and a motivational statement.
                q.append("For Project: ").append(project.getName()).append(" {\n");
                ArrayList<Task> tasks = today.get(project);
                for (Task task : tasks) {
                    q.append("Task Name: ").append(task.getName()).append(", ");
                    q.append("Task Description: ").append(task.getDescription()).append("\n");
                }
                q.append("}\n");
            }
            q.append("}\n");
        }
        if (!one.isEmpty()) {
            q.append("These tasks are due tomorrow: {\n");
            for (Project project : one.keySet()) {
                q.append("For Project: ").append(project.getName()).append(" {\n");
                ArrayList<Task> tasks = one.get(project);
                for (Task task : tasks) {
                    q.append("Task Name: ").append(task.getName()).append(", ");
                    q.append("Task Description: ").append(task.getDescription()).append("\n");
                }
                q.append("}\n");
            }
            q.append("}\n");
        }
        if (!two.isEmpty()) {
            q.append("These tasks are due the day after tomorrow: {\n");
            for (Project project : two.keySet()) {
                q.append("For Project: ").append(project.getName()).append(" {\n");
                ArrayList<Task> tasks = two.get(project);
                for (Task task : tasks) {
                    q.append("Task Name: ").append(task.getName()).append(", ");
                    q.append("Task Description: ").append(task.getDescription()).append("\n");
                }
                q.append("}\n");
            }
            q.append("}\n");
        }
        q.append("Briefly give me some advice for how I should get started, as well as some encouragement" +
                "and motivation.");
        return String.valueOf(q);
    }
    private static String apiCall(String query) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey;

        try (BufferedReader br = new BufferedReader(new FileReader("../apikey.txt"))) {
            apiKey = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String model = "gpt-3.5-turbo";

        ArrayList<HashMap<String, String>> tasks = new ArrayList<HashMap<String, String>>();
        try {
            URL obj = new URI(url).toURL();
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // The request body
            String body = "{\"model\": \"" + model + "\", \"messages\": " +
                    "[{\"role\": \"user\", \"content\": \"___ Here are the details of a project I am working on: Project Title: " +
                    addProjectInputData.getProjectTitle() + ". Project Description: " + addProjectInputData.getProjectDetails() +
                    ". Project Deadline: " + addProjectInputData.getProjectDeadline() + ". Given this project, " +
                    "break it down into smaller subtasks. I want your response formatted EXACTLY in the following " +
                    "way: ;<insert task name>, <insert brief task description>, <insert task deadline>; another task;" +
                    " .... Do not include an opening or closing sentence in your response. ___\"___}]}";
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            StringBuilder response = new StringBuilder();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            String[] tasksArray = extractMessageFromJSONResponse(response.toString()).split(";", 0);

            for (String task : tasksArray) {
                String[] taskAttributes = task.split(",");
                HashMap<String, String> taskMap = new HashMap<String, String>();
                taskMap.put("TaskName", taskAttributes[0]);
                taskMap.put("TaskDescription", taskAttributes[1]);
                taskMap.put("TaskDeadline", taskAttributes[2]);

                tasks.add(taskMap);
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}