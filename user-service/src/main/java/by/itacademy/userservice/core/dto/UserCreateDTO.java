package by.itacademy.userservice.core.dto;

import by.itacademy.userservice.core.enums.UserRole;
import by.itacademy.userservice.core.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class UserCreateDTO {

    @Email(message = "must be a valid address")
    @NotBlank(message = "must be a valid address")
    private String mail;

    @NotBlank(message = "should not be empty")
    private String fio;
    @NotNull(message = "role not assigned")
    private UserRole role;
    @NotNull(message = "status not set")
    private UserStatus status;

    @Size(min = 5, message = "must be at least five characters")
    @NotNull(message = "must be at least five characters")
    private String password;

    public UserCreateDTO() {
    }

    public UserCreateDTO(String mail, String fio, UserRole role, UserStatus status, String password) {
        this.mail = mail;
        this.fio = fio;
        this.role = role;
        this.status = status;
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}