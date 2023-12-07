package interface_adapter.refactor_project;

import use_case.refactor_project.RefactorProjectInputBoundary;
import use_case.refactor_project.RefactorProjectInputData;

import java.time.LocalDate;

public class RefactorProjectController {

    final RefactorProjectInputBoundary refactorProjectInteractor;
// The constructor for refactor project controller
    public RefactorProjectController(RefactorProjectInputBoundary refactorProjectInteractor) {

        this.refactorProjectInteractor = refactorProjectInteractor;
    }
   // execution of the refactor project controller
    public void execute (String username, String projectID){
          RefactorProjectInputData refactorProjectInputData = new RefactorProjectInputData(projectID,
                  username);
            refactorProjectInteractor.execute(refactorProjectInputData);


    }

}
