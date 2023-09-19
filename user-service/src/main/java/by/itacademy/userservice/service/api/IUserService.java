package by.itacademy.userservice.service.api;

import by.itacademy.userservice.core.dto.UserCreateDTO;
import by.itacademy.userservice.core.dto.UserRegistrationDTO;
import by.itacademy.userservice.core.enums.UserStatus;
import by.itacademy.userservice.dao.entity.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Validated
public interface IUserService {
    void save(@Valid UserCreateDTO userCreateDTO);
    User save(@Valid UserRegistrationDTO userRegistrationDTO);
    Page<User> get(PageRequest pageRequest);
    User get(UUID uuid);
    User get(String mail);
    User get(String mail, UserStatus userStatus);
    void update(UUID uuid, LocalDateTime dt_update, @Valid UserCreateDTO userCreateDTO);
    List<User> validate(List<UUID> uuids);
    void activateUser(@Valid User user);
}
