package dev.bayun.id.core.service.email;

import jakarta.mail.MessagingException;

public interface EmailService {

    public void send(String to, EmailContext email) throws MessagingException;

}
