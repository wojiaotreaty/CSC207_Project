package use_case.RefactorProject;

public interface RefactorProjectOutputBoundary {
    void prepareSuccessView(RefactorProjectOutputData refactorProjectOutputData);
    void prepareFailView(String error);
}
