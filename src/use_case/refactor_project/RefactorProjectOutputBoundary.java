package use_case.refactor_project;

public interface RefactorProjectOutputBoundary {
    void prepareSuccessView(RefactorProjectOutputData refactorProjectOutputData);
    void prepareFailView(String error);
}
