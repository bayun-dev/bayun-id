package dev.bayun.id.core.service.email;

import com.nimbusds.jose.util.StandardCharset;
import dev.bayun.id.core.entity.account.Email;
import dev.bayun.id.core.repository.EmailRepository;
import dev.bayun.id.core.util.UUIDGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.UUID;

@Slf4j
@Service
@Import(org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration.class)
public class DefaultEmailService implements EmailService {

    @Value("${spring.mail.username}")
    private String sender;

    @Setter
    private JavaMailSender emailSender;

    @Setter
    private SpringTemplateEngine templateEngine;

    @Setter
    private EmailRepository emailRepository;

    public DefaultEmailService(JavaMailSender emailSender, SpringTemplateEngine templateEngine, EmailRepository emailRepository) {
        Assert.notNull(emailSender, "The emailSender must not be null");
        Assert.notNull(templateEngine, "The templateEngine must not be null");

        setEmailSender(emailSender);
        setTemplateEngine(templateEngine);
        setEmailRepository(emailRepository);
    }

    @Override
    public Email loadById(UUID id) {
        if (id == null) {
            return null;
        }

        return emailRepository.findById(id).orElse(null);
    }

    public Email save(String emailStr) {
        Email email = new Email();
        email.setId(UUIDGenerator.getV7());
        email.setEmail(emailStr);
        email.setConfirmed(false);

        return emailRepository.save(email);
    }

    public void send(String to, EmailContext email) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharset.UTF_8.name());

        Context context = new Context();
        context.setVariables(email.getVariables());
        String emailContent = this.templateEngine.process(email.getTemplateLocation(), context);

        messageHelper.setTo(to);
        messageHelper.setFrom(this.sender);
        messageHelper.setSubject(email.getSubject());
        messageHelper.setText(emailContent, true);
        emailSender.send(message);
    }

    @Override
    public void sendConfirm(Email email) throws MessagingException {
        EmailContext context = new EmailContext("Linking an email to an account", "email-confirm");
        context.getVariables().put("link", "http://localhost:8181/api/methods/me.emailConfirm?hash="+email.getId());
        context.getVariables().put("email", toHidden(email.getEmail()));

        send(email.getEmail(), context);
    }

    @Override
    public void sendRawPassword(Email email, String rawPassword) throws MessagingException {
        EmailContext context = new EmailContext("Recovery Bayun ID", "email-reset-password");
        context.getVariables().put("password", rawPassword);

        send(email.getEmail(), context);
    }

    @Override
    public Email confirm(UUID id) {
        Email email = loadById(id);
        if (email.isConfirmed()) {
            throw new RuntimeException("email already confirmed");
        }

        email.setConfirmed(true);
        return emailRepository.save(email);
    }

    @Override
    public String toHidden(String email) {
        String[] emailParts = email.split("@");
        Assert.state(emailParts.length == 2, "invalid email");
        String local = emailParts[0];
        String domain = emailParts[1];

        return local.charAt(0) + (local.length() > 2 ? String.valueOf(local.charAt(1)) : "") + "***@" + domain;
    }

//    @Override
//    public void emailConfirm(UUID id, String tokenId) {
//        Account account = loadUserById(id);
//        UUID emailUpdateTokenId = UUID.fromString(tokenId);
//        emailUpdateTokenRepository.findById(emailUpdateTokenId).ifPresent(token -> {
//            if (token.getAccountId().equals(account.getId())) {
//                account.getContact().setEmail(token.getEmail());
//                account.getContact().setEmailConfirmed(true);
//            }
//        });
//        emailUpdateTokenRepository.deleteAllByAccountId(account.getId());
//    }

}
