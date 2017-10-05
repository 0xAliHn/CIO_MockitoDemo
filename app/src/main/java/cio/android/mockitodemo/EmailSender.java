package cio.android.mockitodemo;

import cio.android.mockitodemo.model.RegistrationEmail;

public interface EmailSender {

    boolean sendRegistrationEmail(RegistrationEmail email);

}
