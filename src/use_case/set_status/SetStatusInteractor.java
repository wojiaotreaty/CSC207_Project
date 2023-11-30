package use_case.set_status;

public class SetStatusInteractor implements SetStatusInputBoundary {
    final SetStatusProjectsDataAccessInterface projectsDataAccessObject;
    public SetStatusInteractor(SetStatusProjectsDataAccessInterface projectsDataAccessObject) {
        this.projectsDataAccessObject = projectsDataAccessObject;
    }

    @Override
    public void execute(SetStatusInputData setStatusInputData) {
        projectsDataAccessObject.setStatus(setStatusInputData.getProjectId(), setStatusInputData.getTaskId());
    }
}
