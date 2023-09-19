package by.itacademy.userservice.dao.api;

import by.itacademy.userservice.dao.entity.VerificationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface IUserVerificationRepository extends
        JpaRepository<VerificationUser, UUID> {
    Optional<VerificationUser> findByMail(String mail);

    void deleteByDtCreateLessThan(LocalDateTime dateTime);
}
