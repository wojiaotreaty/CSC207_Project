package interface_adapter.set_status;

import use_case.set_status.SetStatusInputBoundary;
import use_case.set_status.SetStatusInputData;

public class SetStatusController {
    final SetStatusInputBoundary setStatusInteractor;
    public SetStatusController(SetStatusInputBoundary setStatusInteractor) {
        this.setStatusInteractor = setStatusInteractor;
    }
    public void execute(String username, String projectId, String taskString) {
        SetStatusInputData setStatusInputData = new SetStatusInputData(username, projectId, taskString);
        setStatusInteractor.execute(setStatusInputData);
    }
}
