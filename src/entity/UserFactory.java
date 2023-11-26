package entity;

public class UserFactory implements UserFactoryInterface {

    @Override
    public User create(String username, String password) {
//        return new User(username, password, projects);
        return new User(); //based off of how entities are made
    }
}
