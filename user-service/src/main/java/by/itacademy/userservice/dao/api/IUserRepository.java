package by.itacademy.userservice.dao.api;

import by.itacademy.userservice.core.enums.UserStatus;
import by.itacademy.userservice.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends
        JpaRepository<User, UUID> {
    Optional<User> findByMail(String mail);
    Optional<User> findByMailAndStatus(String mail, UserStatus status);
    Optional<User> findByUuid(UUID uuid);
}


