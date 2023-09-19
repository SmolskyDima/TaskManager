package by.itacademy.userservice.core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationDTO {
    @Email(message = "Email is not entered correctly")
    @NotBlank(message = "Enter Email")
    private String mail;
    @NotBlank(message = "Enter fio")
    private String fio;
    @NotBlank(message = "The 'password' field should not be empty")
    @Size(min = 6, message = "Password is too short.Enter more than 5 characters")
    @Size(max = 20, message = "Password is too long. Enter less than 20 characters")
    private String password;

    public UserRegistrationDTO() {
    }

    public UserRegistrationDTO(String mail, String fio, String password) {
        this.mail = mail;
        this.fio = fio;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
