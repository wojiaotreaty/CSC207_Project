package interface_adapter.refactor_project;

public class RefactorProjectState {
    private String ID;
    private String tasks;
    public void setRefactoredProjectId(String ID) {
    this.ID =ID;
    }

    public void setRefactoredProjectTasks(String tasks) {
        this.tasks=tasks;
    }
}