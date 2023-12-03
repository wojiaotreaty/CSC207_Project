package data_access;

import entity.Project;
import entity.ProjectFactory;
import entity.Task;
import entity.TaskFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * This DAO interacts with project information (name, description, list of deadlines).
 * It DOES NOT have user information.
 */
public class ProjectsDataAccessObject {

    private final File projectsCsvFile;

    private final Map<String, Integer> headers = new LinkedHashMap<>();

    private ProjectFactory projectFactory;

    private TaskFactory taskFactory;

    /**
     * Note that no Projects are built at time of DAO construction.
     * Instead, Projects are built only when they are needed.
     */
    public ProjectsDataAccessObject(String projectsCsvPath, ProjectFactory projectFactory,
                                    TaskFactory taskFactory) throws IOException {
        this.projectFactory = projectFactory;
        this.taskFactory = taskFactory;

        projectsCsvFile = new File(projectsCsvPath);
        headers.put("projectId", 0);
        headers.put("projectName", 1);
        headers.put("projectDesc", 2);
        headers.put("projectTasks", 3);

        if (projectsCsvFile.length() == 0) {
//            If no existing csv file exists, write a csv file that only contains the headers
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(projectsCsvFile))) {
                writer.write(String.join("%%", headers.keySet()));
                writer.newLine();
            }
        }

    }


    /**
     * Precondition: all of the ids exist in the database
     * @return an ArrayList containing the Project objects that correspond to the ids given
     */
    public ArrayList<Project> getProjects(String[] ids) {

        try (BufferedReader reader = new BufferedReader(new FileReader(projectsCsvFile))) {
            String header = reader.readLine();
            // For later: clean this up by creating a new Exception subclass and handling it in the UI.
            assert header.equals("projectId%%projectName%%projectDesc%%projectTasks");

            ArrayList<String> projectIds = new ArrayList<>(Arrays.asList(ids));
            ArrayList<Project> result = new ArrayList<>();

            String row;
            while ((row = reader.readLine()) != null) {
                String[] col = row.split("%%");
                String currentId = String.valueOf(col[headers.get("projectId")]);
                if (projectIds.contains(currentId)){
                    String currentName = String.valueOf(col[headers.get("projectName")]);
                    String currentDes = String.valueOf(col[headers.get("projectDesc")]);
                    String currentTasks = String.valueOf(col[headers.get("projectTasks")]);
                    ArrayList<Task> tasksList = getTasksList(currentTasks);

                    Project newProject = projectFactory.create(currentId, currentName, currentDes, tasksList);
                    result.add(newProject);
                }
            }

            if (projectIds.size() > result.size()){
                throw new NoSuchElementException("Not all project ids were found when getting projects.");
            }
            if (projectIds.size() < result.size()){
                throw new RuntimeException("List of project ids contain duplicates.");
            }

            return result;

        } catch (IOException e){
            throw new RuntimeException("ERROR: problem reading file when getting the projects");
        }
    }

    /**
     * Helper method for getProjects.
     */
    private ArrayList<Task> getTasksList(String currentTasks) {
        String[] rawTasksList = currentTasks.split("[|]uwu[|]");

        ArrayList<Task> tasksList = new ArrayList<>();

        for (int i = 0; i < rawTasksList.length; i++){
            String rawTask = rawTasksList[i];
//            Get rid of the |uwu| at the end if this is the last task
//            if (i == rawTasksList.length - 1){
//                rawTask = rawTask.substring(0, rawTask.length() - "|uwu|".length());
//            }
            String[] rawTaskInfo = rawTask.split("`");
            String taskName = rawTaskInfo[0];
            String taskDesc = rawTaskInfo[1];
            LocalDate taskDeadline = LocalDate.parse(rawTaskInfo[2]);

            Task newTask = taskFactory.create(taskName, taskDeadline, taskDesc);
            if (rawTaskInfo[3].equals("true")){
                newTask.setStatus(true);
            }

            tasksList.add(newTask);
        }
        return tasksList;
    }


    /**
     * For each project in projects:
     * if the project already exists in the database, then it is updated;
     * if the project does not already exist in the database, then a new entry is added.
     */
    public void saveProjects(ArrayList<Project> projects) {

        ArrayList<Project> projectsToSave = (ArrayList<Project>) projects.clone();

        try (BufferedReader reader = new BufferedReader(new FileReader(projectsCsvFile))) {
            StringBuffer inputBuffer = new StringBuffer();

            String row;
            while ((row = reader.readLine()) != null) {
                String[] col = row.split("%%");
                String currentId = String.valueOf(col[headers.get("projectId")]);

//                Iterate through projects to see if there is a match in ids
                for (int i = 0; i < projectsToSave.size(); i++){
                    Project projToSave = projectsToSave.get(i);

                    if (projToSave.getProjectId().equals(currentId)){
                        row = projectToString(projToSave);
                        projectsToSave.remove(i);
                        i--;
                    }
                }

                inputBuffer.append(row);
                inputBuffer.append('\n');
            }

//            All projects that already exist in the database has been updated and removed from projectsToSave
//            It remains to add the new projects that are still in projectsToSave
            for (Project newProject : projectsToSave){
                inputBuffer.append(projectToString(newProject)).append('\n');
            }

            reader.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(projectsCsvFile);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();

        } catch (IOException e){
            throw new RuntimeException("ERROR: problem reading file when saving the projects");
        }
    }

    /**
     * Helper method for saveProjects; converts project to a String according to the format of the database.
     */
    private String projectToString(Project project){
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(project.getProjectId()).append("%%");
        resultBuilder.append(project.getProjectName()).append("%%");
        resultBuilder.append(project.getProjectDescription()).append("%%");

        StringBuilder rawTasks = new StringBuilder();
        ArrayList<Task> tasksToSave = project.getTasks();
        for (Task task: tasksToSave){
            rawTasks.append(task.toString()).append("|uwu|");
        }

        resultBuilder.append(rawTasks);
        return String.valueOf(resultBuilder);
    }

    public String generateNewProjectIdHelper(){
        try (BufferedReader reader = new BufferedReader(new FileReader(projectsCsvFile))){
            String lastLine = "";
            String currentLine;
            while ((currentLine = reader.readLine()) != null)
            {
                lastLine = currentLine;
            }

            String[] col = lastLine.split("%%");
            String lastId = String.valueOf(col[headers.get("projectId")]);
            if (lastId.equals("projectId")) return "1";
            int largestIdPlusOne = Integer.parseInt(lastId) + 1;
            return Integer.toString(largestIdPlusOne);

        } catch (IOException e){
            throw new RuntimeException("ERROR: problem reading file when generating new project id");
        }
    }


    public void deleteProject(String id){

        try (BufferedReader reader = new BufferedReader(new FileReader(projectsCsvFile))) {
            StringBuffer inputBuffer = new StringBuffer();

            String row;
            while ((row = reader.readLine()) != null) {
                String[] col = row.split("%%");
                String currentId = String.valueOf(col[headers.get("projectId")]);

//                If the id matches the id of the project that needs to be deleted,
//                Remove the corresponding row in the modified output
                if (!id.equals(currentId)){
                    inputBuffer.append(row);
                    inputBuffer.append('\n');
                }
            }

            reader.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(projectsCsvFile);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();

        } catch (IOException e){
            throw new RuntimeException("ERROR: problem reading file when saving the projects");
        }
    }

}
