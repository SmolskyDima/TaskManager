package by.itacademy.userservice.service.authentication;

import by.itacademy.userservice.config.properites.AppProperties;
import by.itacademy.userservice.dao.entity.User;
import by.itacademy.userservice.dao.entity.VerificationUser;
import by.itacademy.userservice.service.api.IEmailService;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;
    private final AppProperties appProperties;

    public EmailServiceImpl(JavaMailSender javaMailSender,
                            MailProperties mailProperties,
                            AppProperties appProperties) {
        this.javaMailSender = javaMailSender;
        this.mailProperties = mailProperties;
        this.appProperties = appProperties;
    }


    @Override
    @Async
    public void sendMail(User user, VerificationUser verificationUser) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailProperties.getUsername());
        mailMessage.setTo(user.getMail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here: " +
                appProperties.getUserVerificationPath() +
                "?code=" + verificationUser.getUuid() + "&mail=" + user.getMail());

        javaMailSender.send(mailMessage);
    }
}
