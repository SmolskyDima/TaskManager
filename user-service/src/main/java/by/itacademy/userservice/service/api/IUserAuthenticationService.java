package by.itacademy.userservice.service.api;

import by.itacademy.userservice.core.dto.UserLoginDTO;
import by.itacademy.userservice.core.dto.UserRegistrationDTO;
import by.itacademy.userservice.dao.entity.User;
import jakarta.validation.Valid;

public interface IUserAuthenticationService {
    void registration(@Valid UserRegistrationDTO userRegistrationDTO);

    void verify(String code, String mail);

    String login(@Valid UserLoginDTO userLoginDTO);

    User getMe();
}
