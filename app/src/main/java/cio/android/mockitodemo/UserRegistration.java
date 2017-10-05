package cio.android.mockitodemo;

import java.util.List;

import cio.android.mockitodemo.exception.EmailFailedException;
import cio.android.mockitodemo.exception.UserAlreadyRegisteredException;
import cio.android.mockitodemo.exception.UserNotFoundException;
import cio.android.mockitodemo.model.RegistrationEmail;

public class UserRegistration {

    private Database database;
    private EmailSender emailSender;

    public UserRegistration(Database database, EmailSender emailSender) {
        this.database = database;
        this.emailSender = emailSender;
    }

    public void registerNewUser(String emailAddress) throws UserAlreadyRegisteredException, EmailFailedException {
        if (database.hasUser(emailAddress)) {
            throw new UserAlreadyRegisteredException();
        }

        if(!emailSender.sendRegistrationEmail(new RegistrationEmail(emailAddress))) {
            throw new EmailFailedException();
        }
        database.addUser(emailAddress);
    }

    public void deleteUser(String emailAddress) throws UserNotFoundException {
        database.deleteUser(emailAddress);
    }

    public void deleteUsers(List<String> userIds) throws UserNotFoundException {
        for (String id : userIds) {
            database.deleteUser(id);
        }
    }

}