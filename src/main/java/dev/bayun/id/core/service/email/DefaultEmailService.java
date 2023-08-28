package dev.bayun.id.core.service.email;

import com.nimbusds.jose.util.StandardCharset;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@Import(org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration.class)
public class DefaultEmailService {

    @Value("${spring.mail.username}")
    private String sender;

    private JavaMailSender emailSender;

    private SpringTemplateEngine templateEngine;

    public DefaultEmailService(JavaMailSender emailSender, SpringTemplateEngine templateEngine) {
        Assert.notNull(emailSender, "The emailSender must not be null");
        Assert.notNull(templateEngine, "The templateEngine must not be null");

        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
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

}
