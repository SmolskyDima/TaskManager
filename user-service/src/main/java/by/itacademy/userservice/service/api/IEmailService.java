package by.itacademy.userservice.service.api;

import by.itacademy.userservice.dao.entity.User;
import by.itacademy.userservice.dao.entity.VerificationUser;


public interface IEmailService {
    void sendMail(User user, VerificationUser verificationUser);
}
