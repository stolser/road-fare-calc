package com.stolser.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class MailSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;
    private MimeMessage mimeMessage;
    private MimeMessageHelper helper;

    public MailSender(JavaMailSenderImpl mailSender, MimeMessage mimeMessage, MimeMessageHelper helper) {
        this.mailSender = mailSender;
        this.mimeMessage = mimeMessage;
        this.helper = helper;
    }

    public void sendEmailTo(String userEmail, String messageText) {
        try {
            helper.setTo(userEmail);
            mimeMessage.setContent(messageText, "text/html");
            mailSender.send(mimeMessage);

        } catch (MessagingException | MailException e) {
            LOGGER.info("Error during sending an email to {}", userEmail, e);
        }
    }
}
