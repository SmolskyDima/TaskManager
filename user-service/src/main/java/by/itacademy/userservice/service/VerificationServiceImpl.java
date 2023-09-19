package by.itacademy.userservice.service;

import by.itacademy.userservice.core.exceptions.FindEntityException;
import by.itacademy.userservice.dao.api.IUserVerificationRepository;
import by.itacademy.userservice.dao.entity.User;
import by.itacademy.userservice.dao.entity.VerificationUser;
import by.itacademy.userservice.service.api.IVerificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationServiceImpl implements IVerificationService {

    private static final String WRONG_MAIL_RESPONSE = "Wrong mail";
    private final IUserVerificationRepository userVerificationRepository;

    public VerificationServiceImpl(IUserVerificationRepository userVerificationRepository) {
        this.userVerificationRepository = userVerificationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public VerificationUser get(String mail) {

        return userVerificationRepository.findByMail(mail)
                .orElseThrow(() -> new FindEntityException(WRONG_MAIL_RESPONSE));
    }

    @Override
    public VerificationUser save(User user) {

        VerificationUser  verificationUser = new VerificationUser();
        verificationUser.setUuid(UUID.randomUUID());
        verificationUser.setMail(user.getMail());

        userVerificationRepository.save(verificationUser);
        return verificationUser;
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        userVerificationRepository.deleteById(uuid);
    }

    @Override
    @Transactional
    public void deleteByDtCreateLessThan(LocalDateTime dateTime) {
        userVerificationRepository.deleteByDtCreateLessThan(dateTime);
    }
}
