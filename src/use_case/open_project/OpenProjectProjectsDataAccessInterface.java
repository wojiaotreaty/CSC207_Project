package use_case.open_project;

import entity.Project;

import java.util.ArrayList;

public interface OpenProjectProjectsDataAccessInterface {
    public ArrayList<Project> getProjects(String[] ids);
}
