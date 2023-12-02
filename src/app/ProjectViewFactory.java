//package app;
//
//import interface_adapter.delete_project.DeleteProjectController;
//import interface_adapter.delete_project.DeleteProjectViewModel;
//import interface_adapter.delete_project.DeleteProjectPresenter;
//import use_case.delete_project.DeleteProjectInputBoundary;
//import use_case.delete_project.DeleteProjectDataAccessInterface;
//import use_case.delete_project.DeleteProjectInteractor;
//import use_case.delete_project.DeleteProjectOutputBoundary;
//
//import java.io.IOException;
//
//public class ProjectViewFactory {
//
//    /** Prevent instantiation. */
//    private ProjectViewFactory() {}
//
////    NOTE: to be implemented by Verdant when he's done writing ProjectView
////    public static ProjectView create() {
////        return null;
////    }
//
//    private static DeleteProjectController createDeleteProjectUseCase(
//            DeleteProjectViewModel deleteProjectViewModel,
//            DeleteProjectDataAccessInterface userDataAccessObject) throws IOException {
//
//        // Notice how we pass this method's parameters to the Presenter.
//        DeleteProjectOutputBoundary deleteProjectOutputBoundary = new DeleteProjectPresenter(deleteProjectViewModel);
//
//        DeleteProjectInputBoundary deleteProjectInteractor = new DeleteProjectInteractor(
//                userDataAccessObject, deleteProjectOutputBoundary);
//
//        return new DeleteProjectController(deleteProjectInteractor);
//    }
//}
