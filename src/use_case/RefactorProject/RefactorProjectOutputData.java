package use_case.RefactorProject;

import entity.Project;

import java.util.ArrayList;

public class RefactorProjectOutputData {
    private Project oldProject;
    private Project refactoredProject;
    public RefactorProjectOutputData(Project oldProject, Project refactoredProject) {
        this.oldProject=oldProject;
        this.refactoredProject=refactoredProject;
    }
    public Project getOldProject() {
        return oldProject;
    }
    public Project getRefactoredProject() {
        return refactoredProject;
    }

}
