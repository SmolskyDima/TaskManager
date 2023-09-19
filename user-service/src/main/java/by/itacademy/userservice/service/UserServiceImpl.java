package by.itacademy.userservice.service;

import by.itacademy.userservice.core.dto.UserCreateDTO;
import by.itacademy.userservice.core.dto.UserRegistrationDTO;
import by.itacademy.userservice.core.enums.UserRole;
import by.itacademy.userservice.core.enums.UserStatus;
import by.itacademy.userservice.core.exceptions.FindEntityException;
import by.itacademy.userservice.core.exceptions.GeneralException;
import by.itacademy.userservice.core.exceptions.NotActivatedException;
import by.itacademy.userservice.dao.api.IUserRepository;
import by.itacademy.userservice.dao.entity.User;
import by.itacademy.userservice.service.api.IAuditService;
import by.itacademy.userservice.service.api.IUserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
public class UserServiceImpl implements IUserService {

    private static final String USER_SAVED = "User: %s was created";

    private static final String ERROR_GET_RESPONSE = "Failed to get user(s). Try again or contact support!";

    private static final String NOT_VERIFIED_RESPONSE = "User: %s, is not activated. " +
            "To activate, follow the link sent to the email specified during registration. " +
            "If you didn't receive a link, please contact your administrator.";

    private final IUserRepository userRepository;
    private final IAuditService auditService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(IUserRepository userRepository,
                           IAuditService auditService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void save(UserCreateDTO createDTO) {

        User user = convert(createDTO);
        user = saveUserEntity(user);

        String text = String.format(USER_SAVED, user.getMail());
        auditService.send(user, text);

    }

    @Override
    @Transactional
    public User save(UserRegistrationDTO userRegistrationDTO) {

        User user = convert(userRegistrationDTO);
        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> get(PageRequest pageRequest) {
        try {
            return userRepository.findAll(pageRequest);
        } catch (DataAccessException ex) {
            //TODO разобраться как правильно обрабатывать исключения
            throw new FindEntityException(ERROR_GET_RESPONSE, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User get(UUID uuid) {
        return userRepository.findByUuid(uuid).orElseThrow(
                () -> new EntityNotFoundException("User not found with UUID: " + uuid));
    }

    @Override
    @Transactional(readOnly = true)
    public User get(String mail) {
        return userRepository.findByMail(mail)
                .orElseThrow(() -> new NoSuchElementException("User with email " + mail + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User get(String mail, UserStatus userStatus) {
        return userRepository.findByMailAndStatus(mail, userStatus)
                .orElseThrow(() -> new NotActivatedException(String.format(NOT_VERIFIED_RESPONSE, mail)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> validate(List<UUID> uuids) {
        return null;
        //TODO для чего нужен и где используется?
    }

    @Override
    @Transactional
    public void update(UUID uuid, LocalDateTime dt_update, UserCreateDTO userCreateDTO) {
        User toUpdate = userRepository.findById(uuid).orElseThrow(
                () -> new GeneralException("User with this uuid does not exist!")
        );

        if (!toUpdate.getDtUpdate().equals(dt_update)) {
            throw new GeneralException("Data inconsistency: User was updated by someone else.");
        }
        //TODO доделать метод
    }

    @Override
    @Transactional
    public void activateUser(User user) {
        user.setStatus(UserStatus.ACTIVATED);
        userRepository.save(user);
    }

    private User convert(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setMail(userCreateDTO.getMail());
        user.setFio(userCreateDTO.getFio());
        user.setRole(userCreateDTO.getRole());
        user.setStatus(userCreateDTO.getStatus());
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        return user;
    }

    private User convert(UserRegistrationDTO userRegistrationDTO) {
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setMail(userRegistrationDTO.getMail());
        user.setFio(userRegistrationDTO.getFio());
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.WAITING_ACTIVATION);
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        return user;
    }

    private User saveUserEntity(User user) {
        //TODO добавить try catch
        return userRepository.saveAndFlush(user);
    }
}
