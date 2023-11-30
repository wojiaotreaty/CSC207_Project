package interface_adapter.set_status;

import use_case.set_status.SetStatusInputBoundary;
import use_case.set_status.SetStatusInputData;

public class SetStatusController {
    final SetStatusInputBoundary setStatusInteractor;
    public SetStatusController(SetStatusInputBoundary setStatusInteractor) {
        this.setStatusInteractor = setStatusInteractor;
    }
    public void execute(String projectId, String taskId) {
        SetStatusInputData setStatusInputData = new SetStatusInputData(projectId, taskId);
        setStatusInteractor.execute(setStatusInputData);
    }
}
