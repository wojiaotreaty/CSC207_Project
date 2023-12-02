package use_case.add_project;
import entity.Project;
import entity.Task;
import entity.User;
import entity.ProjectFactory;
import entity.TaskFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class AddProjectInteractor implements AddProjectInputBoundary {
    final private AddProjectDataAccessInterface userDataAccessObject;
    final private AddProjectOutputBoundary userPresenter;
    final private ProjectFactory projectFactory;
    final private TaskFactory taskFactory;

    public AddProjectInteractor(AddProjectDataAccessInterface addProjectDataAccessInterface,
                                AddProjectOutputBoundary addProjectOutputBoundary,
                                ProjectFactory projectFactory,
                                TaskFactory taskFactory) {
        this.userDataAccessObject = addProjectDataAccessInterface;
        this.userPresenter = addProjectOutputBoundary;
        this.projectFactory = projectFactory;
        this.taskFactory = taskFactory;
    }

    @Override
    public void execute(AddProjectInputData addProjectInputData) {

        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = null;

        try (BufferedReader br = new BufferedReader(new FileReader("../apikey.txt"))) {
            apiKey = br.readLine();
        } catch (IOException e) {
            userPresenter.prepareFailView("Failed to fetch API key: " + e.toString());
        }
        String model = "gpt-3.5-turbo";

        //A list of tasks. Each task is a list structured as follows: {Task Name, Task Desc, Task Deadline}
        ArrayList<Task> tasks = new ArrayList<Task>();
        StringBuilder tasksString = new StringBuilder();

        try {
            URL obj = new URI(url).toURL();
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // The request body
            String body = "{\"model\": \"" + model + "\", \"messages\": " +
                    "[{\"role\": \"user\", \"content\": \"Here are the details of a project I am working on: Project Title: " +
                    addProjectInputData.getProjectTitle() + ". Project Description: " + addProjectInputData.getProjectDetails() +
                    ". Project Deadline: " + addProjectInputData.getProjectDeadline() + ". Given this project, " +
                    "break it down into smaller subtasks. I want your response formatted EXACTLY in the following " +
                    "way: <insert task name>~<insert brief task description>~<insert task deadline as YYYY-MM-DD>" +
                    " .... with each task on the next line. Do not include an opening or closing sentence in your response.\"}]}";
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


            String tasksAsString = extractContentString(String.valueOf(response));

            String [] tasksArray = tasksAsString.split("\\\\n");

            for (String task : tasksArray) {
                String[] taskAttributes = task.split("~");
                tasksString.append(taskAttributes[0]).append('`');
                tasksString.append(taskAttributes[1]).append('`');
                tasksString.append(taskAttributes[2]).append("|uwu|");

                Task taskObject = taskFactory.create(taskAttributes[0], LocalDate.parse(taskAttributes[2].strip()), taskAttributes[1]);
                tasks.add(taskObject);

            }

        } catch (IOException | URISyntaxException e) {
            userPresenter.prepareFailView("Task generation failed: " + e.toString());
        }

        String projectID = userDataAccessObject.generateNewProjectId();
        Project project = projectFactory.create(
                projectID,
                addProjectInputData.getProjectTitle(),
                addProjectInputData.getProjectDetails(),
                tasks
                );

        User user = userDataAccessObject.getUser(addProjectInputData.getUsername());
        user.addProject(project);
        userDataAccessObject.saveUser(user);

        ArrayList<String> outputDataProject = new ArrayList<String>();
        outputDataProject.add(projectID);
        outputDataProject.add(addProjectInputData.getProjectTitle());
        outputDataProject.add(addProjectInputData.getProjectDetails());
        outputDataProject.add(String.valueOf(tasksString));


        AddProjectOutputData addProjectOutputData = new AddProjectOutputData(outputDataProject);
        userPresenter.prepareSuccessView(addProjectOutputData);

    }

    private String extractContentString(String response) {
        int start = response.indexOf("content") + 11;

        int end = response.indexOf("}", start) - 7;

        return response.substring(start, end);
    }
}