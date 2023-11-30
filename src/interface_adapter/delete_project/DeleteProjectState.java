package interface_adapter.delete_project;

/**
 * Has variables deletedProjectId and deletedProjectName for popup after project is deleted.
 */
public class DeleteProjectState {
    private String deletedProjectId;
    private String deletedProjectName;

    DeleteProjectState() {}

//    TODO: determine if this alternative constructor is necessary
    DeleteProjectState(DeleteProjectState copy){
        deletedProjectId = copy.deletedProjectId;
        deletedProjectName = copy.deletedProjectName;
    }

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
        return this.deletedProjectId;
    }
}
