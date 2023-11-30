package interface_adapter;

import use_case.RefactorProject.RefactorProjectInputBoundary;
import use_case.RefactorProject.RefactorProjectInputData;

public class RefactorProjectController {
    final RefactorProjectInputBoundary refactorProjectInteractor;
    public RefactorProjectController(RefactorProjectInputBoundary refactorProjectInteractor) {
        this.refactorProjectInteractor = refactorProjectInteractor;
    }
    public void execute(String ID) {
        RefactorProjectInputData refactorProjectInputData = new RefactorProjectInputData(ID);
        refactorProjectInteractor.execute(refactorProjectInputData);
    }
}