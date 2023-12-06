package use_case.send_notification;

import static org.junit.jupiter.api.Assertions.*;

import entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


public class SendNotificationInteractorBlankUnitTest {

    @Test
    void BlankTest() {
        NotificationInputData notificationInputData = new NotificationInputData(LocalDate.now(), "foo");
        NotificationUsersDataAccessInterface dummyUsersDataAccess = new DummyUsersDataAccessObject();

        NotificationOutputBoundary successPresenter = new NotificationOutputBoundary() {
            @Override
            public void prepareNotificationView(NotificationOutputData notificationOutputData) {
                assertNull(notificationOutputData.getMessage());
                assertNull(notificationOutputData.getImageUrl());
            }
        };
        NotificationInputBoundary notificationInteractor = new NotificationInteractor(dummyUsersDataAccess, successPresenter);
        notificationInteractor.execute(notificationInputData);
    }
    private static class DummyUsersDataAccessObject implements NotificationUsersDataAccessInterface {

        @Override
        public User getUser(String username) {
            UserFactory userFactory = new CommonUserFactory();
            return userFactory.create("foo", "baz");
        }
    }

}
