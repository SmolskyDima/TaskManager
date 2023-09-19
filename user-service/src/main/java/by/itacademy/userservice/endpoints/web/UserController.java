package by.itacademy.userservice.endpoints.web;

import by.itacademy.userservice.core.dto.PageDTO;
import by.itacademy.userservice.core.dto.UserCreateDTO;
import by.itacademy.userservice.core.dto.UserDTO;
import by.itacademy.userservice.dao.entity.User;
import by.itacademy.userservice.service.api.IUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequestMapping("/users")
public class UserController {
    private IUserService userService;
    private ConversionService conversionService;
    private ApplicationEventPublisher eventPublisher;

    public UserController(
            IUserService iuserService,
            ConversionService conversionService,
            ApplicationEventPublisher eventPublisher
    ) {
        this.userService = iuserService;
        this.conversionService = conversionService;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping
    public ResponseEntity<?> save(
            @RequestBody @Valid UserCreateDTO userCreateDTO
    ) {
        userService.save(userCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getPages(
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(required = false, defaultValue = "15") @PositiveOrZero Integer size
    ) {
        Page<User> pageOfUsers = userService.get(PageRequest.of(page, size));

        return new ResponseEntity<>(
                conversionService.convert(pageOfUsers, PageDTO.class),
                HttpStatus.OK
        );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable UUID uuid) {
        UserDTO convert = conversionService.convert(userService.get(uuid), UserDTO.class);
        return new ResponseEntity<>(convert, HttpStatus.OK);
    }

    @PutMapping("{uuid}/dt_update/{dt_update}")
    public ResponseEntity<?> update(
            @PathVariable UUID uuid,
            @PathVariable LocalDateTime dt_update,
            @RequestBody @Valid UserCreateDTO userCreateDTO
    ) {
        userService.update(uuid, dt_update, userCreateDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
