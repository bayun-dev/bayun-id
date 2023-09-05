package dev.bayun.id.core.service.email;

import dev.bayun.id.core.entity.account.Email;
import jakarta.mail.MessagingException;

import java.util.UUID;

public interface EmailService {

    Email confirm(UUID id);

    Email loadById(UUID id);

    Email save(String email);

    void send(String to, EmailContext email) throws MessagingException;

    void sendConfirm(Email email) throws MessagingException;

    void sendRawPassword(Email email, String rawPassword) throws MessagingException;

    String toHidden(String email);

}
