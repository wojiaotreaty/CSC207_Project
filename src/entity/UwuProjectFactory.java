package entity;

public class UwuProjectFactory implements ProjectFactory {
    UwuProject create(String projectId, String projectName, String projectDesc, ArrayList<Task> tasks) {
        return new UwuProject(projectId, projectName, projectDesc, tasks);
    }
}
