package by.itacademy.userservice.endpoints.web;

import by.itacademy.userservice.core.dto.UserDTO;
import by.itacademy.userservice.core.dto.UserLoginDTO;
import by.itacademy.userservice.dao.entity.User;
import by.itacademy.userservice.core.dto.UserRegistrationDTO;
import by.itacademy.userservice.service.api.IUserAuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserAuthenticationController {
    private final IUserAuthenticationService userAuthenticationService;
    private final ConversionService conversionService;


    public UserAuthenticationController(IUserAuthenticationService userAuthenticationService,
                                        ConversionService conversionService) {
        this.userAuthenticationService = userAuthenticationService;
        this.conversionService = conversionService;
    }


    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        userAuthenticationService.registration(userRegistrationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/verification")
    public ResponseEntity<?> verify(@RequestParam String code,
                                    @RequestParam @Email String mail) {
        userAuthenticationService.verify(code, mail);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Valid UserLoginDTO userLoginDTO
    ) {
        return ResponseEntity.ok()
                .header("Authorization", "Bearer "
                        + userAuthenticationService.login(userLoginDTO))
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> me() {
        User user = userAuthenticationService.getMe();
        return ResponseEntity.status(HttpStatus.OK).body(conversionService.convert(user, UserDTO.class));
    }

}
