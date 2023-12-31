package use_case.send_notification;

import entity.Project;
import entity.Task;
import entity.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        User user = notificationUsersDataAccessInterface.getUser(notificationInputData.getCurrentUser());
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
            String[] gptQuery = generateQuery(duePlusOne, duePlusTwo, dueToday);
            String gptOutput = apiCall(gptQuery[0]);
            String[] gptOutputArray1 = gptOutput.split("\\\\n");
            String gptOutputReformat1 = String.join("\n", gptOutputArray1);
            String[] gptOutputArray2 = gptOutputReformat1.split(" ");
            ArrayList<String> gptOutputArrayList = new ArrayList<>(List.of(gptOutputArray2));
            StringBuilder gptOutputReformat2 = new StringBuilder();
            int i = 0;
            int j = 0;
            while (i < gptOutputArrayList.size()) {
                if (j == 20) {
                    gptOutputReformat2.append(String.join(" ", gptOutputArrayList.subList(i - 20, i))).append("\n");
                    j = 0;
                }
                j = j + 1;
                i = i + 1;
            }
            gptOutputReformat2.append(String.join(" ", gptOutputArrayList.subList(i - j, i)));
            String gptResponse = String.valueOf(gptOutputReformat2);


            String image = apiCall2();


            // Creates output data and calls presenter
            NotificationOutputData notificationOutputData = new NotificationOutputData(gptQuery[1] + gptResponse + "\n\nEnjoy this motivational image generated just for you!", image);
            notificationPresenter.prepareNotificationView(notificationOutputData);
        }
        else {
            NotificationOutputData notificationOutputData = new NotificationOutputData(null, null);
            notificationPresenter.prepareNotificationView(notificationOutputData);
        }



    }
    // Generates the string that will be used to query gpt
    private static String[] generateQuery(HashMap<Project, ArrayList<Task>> one, HashMap<Project, ArrayList<Task>> two,
                                        HashMap<Project, ArrayList<Task>> today) {
        // initializes the StringBuilder and briefly explains to gpt the situation.
        StringBuilder m = new StringBuilder("I am working on some projects right now and these are some " +
                "upcoming tasks that are due. ");
        StringBuilder q = new StringBuilder();
        // Note that all three if statements are essentially exactly the same as this one.
        // They just add information about the tasks due on their specific days.
        if (!today.isEmpty()) {
            // Curly brackets, {}, are used to encompass tasks due on the same day and
            // related to the same project.
            m.append("These tasks are due today: [");
            q.append("These tasks are due today: \n");
            for (Project project : today.keySet()) {
                // The project description is omitted to save space in the call. I aim to have
                // gpt give a relatively brief response, and I feel that the task descriptions
                // along with the project title should be enough information to give some
                // quick advice and a motivational statement.
                m.append("For Project: ").append(project.getProjectName()).append(" [");
                q.append("     For Project: ").append(project.getProjectName()).append("\n");
                ArrayList<Task> tasks = today.get(project);
                for (Task task : tasks) {
                    m.append("Task Name: ").append(task.getName()).append(", ");
                    m.append("Task Description: ").append(task.getDescription()).append(". ");
                    q.append("          Task Name: ").append(task.getName()).append("\n");
                    q.append("          Task Description: ").append(task.getDescription()).append("\n\n");
                }
                m.append("]");
            }
            m.append("] ");
        }
        if (!one.isEmpty()) {
            m.append("These tasks are due tomorrow: [");
            q.append("These tasks are due tomorrow: \n");
            for (Project project : one.keySet()) {
                m.append("For Project: ").append(project.getProjectName()).append(" [");
                q.append("     For Project: ").append(project.getProjectName()).append("\n");
                ArrayList<Task> tasks = one.get(project);
                for (Task task : tasks) {
                    m.append("Task Name: ").append(task.getName()).append(", ");
                    m.append("Task Description: ").append(task.getDescription()).append(". ");
                    q.append("          Task Name: ").append(task.getName()).append("\n");
                    q.append("          Task Description: ").append(task.getDescription()).append("\n\n");
                }
                m.append("]");
            }
            m.append("] ");
        }
        if (!two.isEmpty()) {
            m.append("These tasks are due the day after tomorrow: [");
            q.append("These tasks are due the day after tomorrow: \n");
            for (Project project : two.keySet()) {
                m.append("For Project: ").append(project.getProjectName()).append(" [");
                q.append("     For Project: ").append(project.getProjectName()).append("\n");
                ArrayList<Task> tasks = two.get(project);
                for (Task task : tasks) {
                    m.append("Task Name: ").append(task.getName()).append(", ");
                    m.append("Task Description: ").append(task.getDescription()).append(". ");
                    q.append("          Task Name: ").append(task.getName()).append("\n");
                    q.append("          Task Description: ").append(task.getDescription()).append("\n\n");
                }
                m.append("]");
            }
            m.append("] ");
        }
        m.append("Briefly give me some advice for how I should get started, as well as some encouragement" +
                "and motivation. IMPORTANT: keep you response very brief! less than 100 words.");
//        m.append("IMPORTANT: mark the beginning and end of your response with \"|uwu|\"");
        return new String[]{String.valueOf(m), String.valueOf(q)};
    }
    private static String apiCall(String query) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = null;

        try (BufferedReader br = new BufferedReader(new FileReader("src/apikey.txt"))) {
            apiKey = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String model = "gpt-3.5-turbo";

        try {
            URL obj = new URI(url).toURL();
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");


            String body = "{\"model\": \"" + model + "\", \"messages\": " +
                    "[{\"role\": \"user\", \"content\": \"" + query + "\"}]}";

            connection.setDoOutput(true);;
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

            return extractMessage(response.toString());

        } catch (IOException | URISyntaxException e) {
            return "GPT had an Oopsie, but it wishes you good luck on your tasks.";
        }
    }
    private static String apiCall2() {
        String url = "https://api.openai.com/v1/images/generations";
        String apiKey = null;

        try (BufferedReader br = new BufferedReader(new FileReader("src/apikey.txt"))) {
            apiKey = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String model = "dall-e-3";

        try {
            URL obj = new URI(url).toURL();
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");


            String body = "{\"model\": \"" + model + "\", \"prompt\": \"An encouraging and motivational image.\"" +
                    ", \"n\": 1, \"size\": \"1024x1024\"}";

            connection.setDoOutput(true);;
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

            return extractMessage2(response.toString());

        } catch (IOException | URISyntaxException e) {
            return "GPT had an Oopsie, but it wishes you good luck on your tasks.";
        }
    }
    private static String extractMessage(String response) {
        int start = response.indexOf("content") + 11;

        int end = response.indexOf("}", start) - 7;

        return response.substring(start, end);
    }
    private static String extractMessage2(String response) {
        int start = response.indexOf("url") + 7;

        int end = response.indexOf("}", start) - 5;

        return response.substring(start, end);
    }
}
