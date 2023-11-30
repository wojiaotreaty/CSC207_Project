package entity;

public class UwuUserFactory implements UserFactory {
    UwuUser create(String username, String password, ArrayList<Project> projects) {
        return new UwuUser(username, password, projects);
    }
}
