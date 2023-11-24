package interface_adapter.delete_project;
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
}
