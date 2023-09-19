package by.itacademy.userservice.service.api;

import by.itacademy.userservice.dao.entity.User;
import by.itacademy.userservice.dao.entity.VerificationUser;

import java.time.LocalDateTime;
import java.util.UUID;

public interface IVerificationService {

    VerificationUser get(String mail);
    VerificationUser save(User user);
    void delete(UUID token);
    void deleteByDtCreateLessThan(LocalDateTime dateTime);
}
