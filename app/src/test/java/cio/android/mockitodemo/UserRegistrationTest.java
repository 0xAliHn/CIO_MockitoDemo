package cio.android.mockitodemo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cio.android.mockitodemo.exception.EmailFailedException;
import cio.android.mockitodemo.exception.UserAlreadyRegisteredException;
import cio.android.mockitodemo.exception.UserNotFoundException;
import cio.android.mockitodemo.model.RegistrationEmail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserRegistrationTest {

    private UserRegistration testee;

    private Database mockDatabase;
    private EmailSender mockEmailSender;

    @Before
    public void setup() {
        mockDatabase = Mockito.mock(Database.class);
        mockEmailSender = Mockito.mock(EmailSender.class);

        testee = new UserRegistration(mockDatabase, mockEmailSender);
    }

    @Test
    public void shouldAddNewUserToDatabaseUsingAnyMatcherCasted() throws UserAlreadyRegisteredException, IOException {
        String emailAddress = "foo@example.com";
        when(mockDatabase.hasUser(emailAddress)).thenReturn(false);

        when(mockEmailSender
                .sendRegistrationEmail((RegistrationEmail) any()))
                .thenReturn(true);

        testee.registerNewUser(emailAddress);
        verify(mockDatabase).addUser(emailAddress);
    }

    @Test
    public void shouldAddNewUserToDatabaseUsingAnyMatcherTypedOutside() throws UserAlreadyRegisteredException, IOException {
        String emailAddress = "foo@example.com";
        when(mockDatabase.hasUser(emailAddress)).thenReturn(false);

        when(mockEmailSender
                .sendRegistrationEmail(ArgumentMatchers.<RegistrationEmail>any()))
                .thenReturn(true);

        testee.registerNewUser(emailAddress);
        verify(mockDatabase).addUser(emailAddress);
    }

    @Test
    public void shouldAddNewUserToDatabase() throws UserAlreadyRegisteredException, IOException {
        UserRegistration testee = new UserRegistration(mockDatabase, mockEmailSender);

        String emailAddress = "foo@example.com";
        when(mockDatabase.hasUser(emailAddress)).thenReturn(false);

        when(mockEmailSender
                .sendRegistrationEmail(any(RegistrationEmail.class)))
                .thenReturn(true);

        testee.registerNewUser(emailAddress);
        verify(mockDatabase).addUser(emailAddress);
    }

    @Test(expected = UserAlreadyRegisteredException.class)
    public void shouldThrowExceptionAddingDuplicateEmail() throws UserAlreadyRegisteredException, EmailFailedException {
        Database mockDatabase = Mockito.mock(Database.class);
        EmailSender mockEmailSender = Mockito.mock(EmailSender.class);

        UserRegistration testee = new UserRegistration(mockDatabase, mockEmailSender);

        Mockito.when(mockDatabase.hasUser("foo@example.com")).thenReturn(true);
        testee.registerNewUser("foo@example.com");
    }

    @Test
    public void shouldReturnSensibleDefaultReturnValues() throws UserAlreadyRegisteredException, IOException {

        // ints, floats, doubles etc.. return 0 by default
        int defaultIntReturn = mockDatabase.numberOfUsers();
        assertEquals(0, defaultIntReturn);

        // collections return an empty collection
        List<String> defaultCollectionReturn = mockDatabase.getUsers();
        assertEquals(0, defaultCollectionReturn.size());

        // booleans return 'false' by default
        boolean defaultBooleanReturn = mockDatabase.isReadWriteSupported();
        assertFalse(defaultBooleanReturn);

        // Strings return null
        String defaultStringReturn = mockDatabase.getDatabaseName();
        assertNull(defaultStringReturn);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionDeletingUserNotInDatabase() throws UserNotFoundException {
        String email = "foo@example.com";

        Mockito.doThrow(UserNotFoundException.class).when(mockDatabase).deleteUser(email);

        testee.deleteUser(email);
    }

    @Test
    public void shouldReturnTrueForAnyString() {
        when(mockDatabase.hasUser((String) ArgumentMatchers.any())).thenReturn(true);

        assertTrue(mockDatabase.hasUser("foo@example.com"));
        assertTrue(mockDatabase.hasUser("foo @ example.com"));
        assertTrue(mockDatabase.hasUser(null));
    }

    /**
     * Example of chaining stubbed answers
     * Chaining allows you to specify multiple answers that should be returned from the stubbed method
     * <p>
     * Mockito allows any number of arguments to be specified.
     * Every time the method is called, it will return the next value in the given sequence.
     * The last value in the chain will be used over and over if the method is called more times than there were chained answers.
     * <p>
     * Note, there isn't a good example of why you'd need this in this example.
     * However, it might prove useful to know in other situations.
     */
    @Test
    public void shouldChainResponses() {
        String email = "foo@example.com";
        when(mockDatabase.hasUser(email)).thenReturn(true, false);
        assertTrue(mockDatabase.hasUser(email));
        assertFalse(mockDatabase.hasUser(email));
        assertFalse(mockDatabase.hasUser(email));
    }


    /**
     * Verifying a Method Was Never Called
     */
    @Test
    public void shouldNotDropDatabaseWhenDeletingUsers() throws IOException, UserNotFoundException {
        List<String> userIds = new ArrayList<>();
        testee.deleteUsers(userIds);

        // both of these lines are functionally identical
        verify(mockDatabase, times(0)).dropDatabase();
        verify(mockDatabase, never()).dropDatabase();
    }

    /**
     * Verifying a Method Was Called An Exact Number of Times
     */
    @Test
    public void shouldDeleteMultipleUsers() throws IOException, UserNotFoundException {
        List<String> userIds = new ArrayList<>();
        userIds.add("foo");
        userIds.add("bar");

        testee.deleteUsers(userIds);
        verify(mockDatabase, times(2)).deleteUser(anyString());

    }


    /**
     * Verifying That a Method was Called a Minimum or Maximum Number of Times
     */
    @Test
    public void shouldCallMockAMinimumNumberOfTimes() throws IOException, UserNotFoundException {
        List<String> userIds = new ArrayList<>();
        userIds.add("a");
        userIds.add("b");
        userIds.add("c");

        testee.deleteUsers(userIds);

        Mockito.verify(mockDatabase, Mockito.atLeast(2)).deleteUser(anyString());
    }

    @Test
    public void shouldCallMockAMaximumNumberOfTimes() throws IOException, UserNotFoundException {
        List<String> userIds = new ArrayList<>();
        userIds.add("a");
        userIds.add("b");
        userIds.add("c");

        testee.deleteUsers(userIds);

        Mockito.verify(mockDatabase, Mockito.atMost(5)).deleteUser(anyString());
    }

    @Test
    public void shouldCallMockANumberOfTimesAsARange() throws IOException, UserNotFoundException {
        List<String> userIds = new ArrayList<>();
        userIds.add("a");
        userIds.add("b");
        userIds.add("c");

        testee.deleteUsers(userIds);

        Mockito.verify(mockDatabase, Mockito.atLeast(2)).deleteUser(anyString());
        Mockito.verify(mockDatabase, Mockito.atMost(5)).deleteUser(anyString());
    }
}