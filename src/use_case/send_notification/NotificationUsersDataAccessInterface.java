package use_case.send_notification;

import entity.User;

public interface NotificationUsersDataAccessInterface {
    User getUser(String username);
}
