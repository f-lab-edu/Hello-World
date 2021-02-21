package me.soo.helloworld.exception;

import org.springframework.mail.MailException;

public class MailNotSentException extends MailException {

    public MailNotSentException(String message) {
        super(message);
    }

    public MailNotSentException(String message, Throwable cause) {
        super(message, cause);
    }
}
