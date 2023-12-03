package use_case.set_status;

public class SetStatusInteractor implements SetStatusInputBoundary {
    final SetStatusUsersDataAccessInterface projectsDataAccessObject;
    public SetStatusInteractor(SetStatusUsersDataAccessInterface projectsDataAccessObject) {
        this.projectsDataAccessObject = projectsDataAccessObject;
    }

    @Override
    public void execute(SetStatusInputData setStatusInputData) {

    }
}
