package use_case.send_notification;
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
        User user = notificationUsersDataAccessInterface.getCurrentUser();
        ArrayList<Project> projects = user.getProjects();
        LocalDate date = notificationInputData.getDate();
        LocalDate plusOne = date.plusDays(1);
        LocalDate plusTwo = date.plusDays(2);
        HashMap<Project, ArrayList<Task>> duePlusOne = new HashMap<Project, ArrayList<Task>>();
        HashMap<Project, ArrayList<Task>> duePlusTwo = new HashMap<Project, ArrayList<Task>>();
        for (Project project : projects) {
            for (Task task : project.getTasks()) {
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
                }
            }
        }
        String gptQuery = generateQuery(duePlusOne, duePlusTwo);
        String gptOutput = apiCall(gptQuery);


    }
    private static String generateQuery(HashMap<Project, ArrayList<Task>> One, HashMap<Project, ArrayList<Task>> Two) {
        q = new StringBuilder("");

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
