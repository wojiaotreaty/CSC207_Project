package use_case.set_status;

import entity.Project;
import entity.Task;
import entity.User;

import java.util.ArrayList;

public class SetStatusInteractor implements SetStatusInputBoundary {
    final SetStatusUsersDataAccessInterface usersDataAccessObject;
    final SetStatusOutputBoundary setStatusPresenter;
    public SetStatusInteractor(SetStatusUsersDataAccessInterface projectsDataAccessObject, SetStatusOutputBoundary setStatusPresenter) {
        this.usersDataAccessObject = projectsDataAccessObject;
        this.setStatusPresenter = setStatusPresenter;
    }

    @Override
    public void execute(SetStatusInputData setStatusInputData) {
        User user = usersDataAccessObject.getUser(setStatusInputData.getUsername());
        for (Project p : user) {
            if (p.getProjectId().equals(setStatusInputData.getProjectId())) {
                for (Task t : p) {
                    if (t.toString().equals(setStatusInputData.getTaskString())) {
                        t.setStatus(!t.getStatus());
                        usersDataAccessObject.saveUser(user);
                        break;
                    }
                }
                break;
            }
        }
        SetStatusOutputData setStatusOutputData = new SetStatusOutputData(setStatusInputData.getProjectId(), setStatusInputData.getTaskString());
        setStatusPresenter.prepareSuccessView(setStatusOutputData);
    }
}
