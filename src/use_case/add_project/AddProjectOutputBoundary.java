package use_case.add_project;

public interface AddProjectOutputBoundary {
    void prepareSuccessView(AddProjectOutputData addProjectOutputData);

    void prepareFailView(String error);
}