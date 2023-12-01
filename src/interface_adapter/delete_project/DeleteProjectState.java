package interface_adapter.delete_project;

/**
 * Has variables deletedProjectId and deletedProjectName for popup after project is deleted.
 */
public class DeleteProjectState {
    private String deletedProjectId;
    private String deletedProjectName;

    DeleteProjectState() {}

    void setDeletedProjectId(String deletedProjectId){
        this.deletedProjectId = deletedProjectId;
    }

    void setDeletedProjectName(String deletedProjectName){
        this.deletedProjectName = deletedProjectName;
    }

    String getDeletedProjectId(){
        return this.deletedProjectId;
    }

    String getDeletedProjectName(){
        return this.deletedProjectName;
    }
}
