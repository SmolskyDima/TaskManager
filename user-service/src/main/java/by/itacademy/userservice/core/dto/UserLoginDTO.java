package by.itacademy.userservice.core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserLoginDTO {
    @NotBlank(message = "Поле 'mail' не должно быть пустым")
    @Email(message = "Некорректный формат электронной почты")
    private String mail;
    @NotBlank(message = "Поле 'password' не должно быть пустым")
    @Size(min = 6, message = "Пароль слишком короткий.Введите больше 5 символов")
    @Size(max = 20, message = "Пароль слишком длинный.Введите меньше 20  символов")
    private String password;

    public UserLoginDTO() {
    }

    public UserLoginDTO(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
