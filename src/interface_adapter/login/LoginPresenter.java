package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final DashboardViewModel dashboardViewModel;
    private ViewManagerModel viewManagerModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          DashboardViewModel dashboardViewModel,
                          LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.dashboardViewModel = dashboardViewModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, switch to the logged in view.

        DashboardState dashboardState = dashboardViewModel.getState(); // DashboardState
        dashboardState.setUsername(response.getUsername());
        dashboardState.setProjects(response.getProjectData()); // give the list of projects for dashboard
        this.dashboardViewModel.setState(dashboardState);
        this.dashboardViewModel.firePropertyChanged();

        this.viewManagerModel.setActiveView(dashboardViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        LoginState loginState = loginViewModel.getState();
        loginState.setUsernameError(error);
        loginViewModel.firePropertyChanged();
    }
}
