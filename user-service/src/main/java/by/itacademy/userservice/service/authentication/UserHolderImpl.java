package by.itacademy.userservice.service.authentication;

import by.itacademy.userservice.core.dto.UserDTO;
import by.itacademy.userservice.service.api.IUserHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserHolderImpl implements IUserHolder {

    public UserDTO getUser(){
        return (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
