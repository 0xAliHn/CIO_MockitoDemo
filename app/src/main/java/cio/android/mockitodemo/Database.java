package cio.android.mockitodemo;

import java.util.List;

import cio.android.mockitodemo.exception.UserNotFoundException;

public interface Database {

    void addUser(String emailAddress);

    boolean hasUser(String emailAddress);

    void deleteUser(String emailAddress) throws UserNotFoundException;

    List<String> getUsers();

    int numberOfUsers();

    boolean isReadWriteSupported();

    String getDatabaseName();

    void dropDatabase();
}
