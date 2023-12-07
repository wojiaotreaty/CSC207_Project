package use_case.refactor_project;
public class RefactorProjectInputData {
private final String id;
private final String userName;

    public RefactorProjectInputData(String id,String userName) {
        this.id=id;
        this.userName=userName;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }
}
