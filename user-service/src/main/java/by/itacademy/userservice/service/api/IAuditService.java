package by.itacademy.userservice.service.api;

import by.itacademy.userservice.core.dto.UserShortDTO;
import by.itacademy.userservice.dao.entity.User;
import org.springframework.stereotype.Service;



public interface IAuditService {

    void send(User user, String text);
    void send(User entity, UserShortDTO dto, String text);
}
