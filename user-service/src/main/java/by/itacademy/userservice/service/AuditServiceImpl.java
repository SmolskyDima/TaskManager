package by.itacademy.userservice.service;

import by.itacademy.userservice.config.properites.JWTProperty;
import by.itacademy.userservice.core.dto.AuditCreateDTO;
import by.itacademy.userservice.core.dto.UserDTO;
import by.itacademy.userservice.core.dto.UserShortDTO;
import by.itacademy.userservice.core.enums.EssenceType;
import by.itacademy.userservice.dao.entity.User;
import by.itacademy.userservice.endpoints.utils.JwtTokenHandler;
import by.itacademy.userservice.service.api.IAuditService;
import by.itacademy.userservice.service.api.IUserHolder;
import by.itacademy.userservice.service.feign.AuditServiceFeign;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AuditServiceImpl implements IAuditService {
    private final AuditServiceFeign auditServiceFeign;
    private final JwtTokenHandler jwtHandler;
    private final JWTProperty property;
    private final IUserHolder holder;

    public AuditServiceImpl(AuditServiceFeign auditServiceFeign,
                            JwtTokenHandler jwtHandler,
                            JWTProperty property,
                            IUserHolder holder) {
        this.auditServiceFeign = auditServiceFeign;
        this.jwtHandler = jwtHandler;
        this.property = property;
        this.holder = holder;
    }

    @Override
    public void send(User user, String text) {
        UserShortDTO userShortDTO = fillUserShortDTO(holder.getUser());
        AuditCreateDTO auditCreateDTO = fillUserSendDTO(userShortDTO, user, text);
        String bearerToken = "Bearer " + jwtHandler.generateSystemAccessToken(property.getSystem());
        auditServiceFeign.send(bearerToken, auditCreateDTO);
    }

    @Override
    public void send(User entity, UserShortDTO dto, String text) {
        AuditCreateDTO auditCreateDTO = fillUserSendDTO(dto, entity, text);
        String bearerToken = "Bearer " + jwtHandler.generateSystemAccessToken(property.getSystem());
        auditServiceFeign.send(bearerToken, auditCreateDTO);
    }

    private AuditCreateDTO fillUserSendDTO(UserShortDTO userShortDTO, User user, String text) {
        AuditCreateDTO auditCreateDTO = new AuditCreateDTO();

        //user, который произвёл операцию
        auditCreateDTO.setUser(userShortDTO);

        //EssenceType для аудита
        auditCreateDTO.setType(EssenceType.USER);
        auditCreateDTO.setText(text);
        //id кого создали
        auditCreateDTO.setId(user.getUuid().toString());

        return auditCreateDTO;
    }

    private UserShortDTO fillUserShortDTO(UserDTO userDTO) {
        UserShortDTO userShortDTO = new UserShortDTO();
        userShortDTO.setUuid(userDTO.getUuid());
        userShortDTO.setMail(userDTO.getMail());
        userShortDTO.setFio(userDTO.getFio());
        userShortDTO.setRole(userDTO.getUserRole());
        return userShortDTO;
    }
}
