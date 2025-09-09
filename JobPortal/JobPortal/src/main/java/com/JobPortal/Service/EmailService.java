package com.JobPortal.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom("goelv2610@gmail.com");
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);
            javaMailSender.send(mail);
        } catch(Exception e) {
            log.error("Exception", e);
        }
    }
}