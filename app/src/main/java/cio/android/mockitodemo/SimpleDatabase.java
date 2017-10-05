package cio.android.mockitodemo;


import java.util.ArrayList;
import java.util.List;

import cio.android.mockitodemo.exception.UserNotFoundException;

public class SimpleDatabase implements Database {

    private List<String> users = new ArrayList<>();

    @Override
    public void addUser(String emailAddress) {
        users.add(emailAddress);
    }

    @Override
    public boolean hasUser(String emailAddress) {
        return users.contains(emailAddress);
    }

    @Override
    public void deleteUser(String emailAddress) throws UserNotFoundException {
        if (!hasUser(emailAddress)) {
            throw new UserNotFoundException();
        }
        users.remove(emailAddress);
    }

    @Override
    public List<String> getUsers() {
        return users;
    }

    @Override
    public int numberOfUsers() {
        return users.size();
    }

    @Override
    public boolean isReadWriteSupported() {
        return true;
    }

    @Override
    public String getDatabaseName() {
        return "SimpleDatabase";
    }

    @Override
    public void dropDatabase() {

    }

}
