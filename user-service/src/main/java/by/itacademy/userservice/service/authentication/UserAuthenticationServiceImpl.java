package by.itacademy.userservice.service.authentication;

import by.itacademy.userservice.core.dto.UserShortDTO;
import by.itacademy.userservice.core.dto.UserLoginDTO;
import by.itacademy.userservice.core.dto.UserRegistrationDTO;
import by.itacademy.userservice.core.enums.UserStatus;
import by.itacademy.userservice.core.exceptions.NotActivatedException;
import by.itacademy.userservice.core.exceptions.VerificationException;
import by.itacademy.userservice.dao.entity.User;
import by.itacademy.userservice.dao.entity.VerificationUser;
import by.itacademy.userservice.endpoints.utils.JwtTokenHandler;
import by.itacademy.userservice.service.api.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Service
@Validated
public class UserAuthenticationServiceImpl implements IUserAuthenticationService {

    private static final String ERROR_VERIFY_TOKEN_RESPONSE = "Unverified";
    private static final String WRONG_PASSWORD_RESPONSE = "Wrong password";
    private static final String NOT_VERIFIED_RESPONSE = "User is not activated";
    private static final String USER_REGISTERED = "User %s registered and waiting activation";
    private static final String ERROR_VERIFY_RESPONSE = "Failed to verify user!";
    private static final String USER_ACTIVATED = "User: %s was activated";

    private final IUserService userService;
    private final IVerificationService verificationService;
    private final IEmailService emailService;
    private final PasswordEncoder encoder;
    private final JwtTokenHandler jwtHandler;
    private final IAuditService auditService;
    private final IUserHolder userHolder;

    public UserAuthenticationServiceImpl(IUserService userService,
                                         IVerificationService verificationService,
                                         IEmailService emailService,
                                         PasswordEncoder encoder,
                                         JwtTokenHandler jwtHandler,
                                         IAuditService auditService,
                                         IUserHolder userHolder) {
        this.userService = userService;
        this.verificationService = verificationService;
        this.emailService = emailService;
        this.encoder = encoder;
        this.jwtHandler = jwtHandler;
        this.auditService = auditService;
        this.userHolder = userHolder;
    }


    @Override
    @Transactional
    public void registration(UserRegistrationDTO userRegistrationDTO) {

        User user = userService.save(userRegistrationDTO);

        VerificationUser verificationUser = verificationService.save(user);

        emailService.sendMail(user, verificationUser);

        UserShortDTO userShortDTO = convertUserForAudit(user);

        String text = String.format(USER_REGISTERED, user.getMail());
        auditService.send(user, userShortDTO, text);
    }

    @Override
    public void verify(String code, String mail) {

        User user = userService.get(mail);

        VerificationUser verificationUser = verificationService.get(mail);

        if (user == null || verificationUser == null) {
            throw new VerificationException("User or verification data not found");
        }

        if (!verificationUser.getUuid().toString().equals(code) ||
                !user.getStatus().equals(UserStatus.WAITING_ACTIVATION)) {
            throw new VerificationException(ERROR_VERIFY_RESPONSE);
        }

        userService.activateUser(user);

        verificationService.delete(verificationUser.getUuid());
        verificationService.deleteByDtCreateLessThan(LocalDateTime.now().minusDays(7));

        UserShortDTO userShortDTO = convertUserForAudit(user);
        String text = String.format(USER_ACTIVATED, user.getMail());
        auditService.send(user, userShortDTO, text);
    }

    @Override
    @Transactional(readOnly = true)
    public String login(UserLoginDTO userLoginDTO) {

        User user = userService.get(userLoginDTO.getMail(), UserStatus.ACTIVATED);

        if(!encoder.matches(userLoginDTO.getPassword(), user.getPassword())){
            throw new IllegalArgumentException(WRONG_PASSWORD_RESPONSE);
        }
        if(!user.getStatus().equals(UserStatus.ACTIVATED)) {
            throw new NotActivatedException(NOT_VERIFIED_RESPONSE);
        }

        return jwtHandler.generateAccessToken(convertUserForAudit(user), user.getMail());
    }

    @Override
    public User getMe() {
        return userService.get(userHolder.getUser().getMail());
    }


    private UserShortDTO convertUserForAudit(User user) {
        UserShortDTO userShortDTO = new UserShortDTO();
        userShortDTO.setUuid(user.getUuid());
        userShortDTO.setMail(user.getMail());
        userShortDTO.setFio(user.getFio());
        userShortDTO.setRole(user.getRole());
        return userShortDTO;
    }
}
