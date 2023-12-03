package interface_adapter.set_status;

import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.dashboard.ProjectData;
import use_case.set_status.SetStatusOutputBoundary;
import use_case.set_status.SetStatusOutputData;

import java.util.ArrayList;

public class SetStatusPresenter implements SetStatusOutputBoundary {
    private final DashboardViewModel dashboardViewModel;
    public SetStatusPresenter(DashboardViewModel dashboardViewModel) {
        this.dashboardViewModel = dashboardViewModel;
    }
    @Override
    public void prepareSuccessView(SetStatusOutputData setStatusOutputData) {
        DashboardState dashboardState = dashboardViewModel.getState();
        ArrayList<ProjectData> projects = dashboardState.getProjects();
        for (ProjectData projectData : projects) {
            if (projectData.getProjectID().equals(setStatusOutputData.getProjectId())) {
                String taskString = setStatusOutputData.getTaskString();
                String[] taskArray = taskString.split("`");
                for (ArrayList<String> task : projectData.getProjectTasks()) {
                    if (task.get(0).equals(taskArray[0]) &&  task.get(1).equals(taskArray[1]) && task.get(2).equals(taskArray[2])) {
                        if (taskArray[3].equals("true")) {
                            task.set(3, "false");
                        }
                        else {
                            task.set(3, "true");
                        }
                        break;
                    }
                }
                break;
            }
        }
        dashboardViewModel.setState(dashboardState);
    }
}
