package by.staryhroft.userbalance.controller;

import by.staryhroft.userbalance.dto.*;
import by.staryhroft.userbalance.service.ContactService;
import by.staryhroft.userbalance.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final ContactService contactService;

    public UserController(UserService userService, ContactService contactService) {
        this.userService = userService;
        this.contactService = contactService;
    }

    @GetMapping("/search")
    public Page<UserDto> search(SearchRequest request, Pageable pageable) {
        log.info("Запрос на поиск пользователей: {}", request);
        Page<UserDto> result = userService.searchUsers(request, pageable);
        log.info("Найдено {} записей", result.getTotalElements());
        return result;
    }

    @GetMapping("/me")
    public UserDto getMe(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        log.info("Запрос профиля пользователя с id {}", userId);
        return userService.getUserById(userId);
    }

    @PostMapping("/me/emails")
    public void addEmail(Authentication authentication, @Valid @RequestBody UpdateContactRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        log.info("Добавление email '{}' для пользователя с id {}", request.getValue(), userId);
        contactService.addEmail(userId, request.getValue());
    }

    @DeleteMapping("/me/emails/{emailId}")
    public void deleteEmail(Authentication authentication, @PathVariable Long emailId) {
        Long userId = (Long) authentication.getPrincipal();
        log.info("Удаление email с id {} для пользователя {}", emailId, userId);
        contactService.deleteEmail(userId, emailId);
    }

    @PutMapping("/me/emails/{emailId}")
    public void updateEmail(Authentication authentication,
                            @PathVariable Long emailId,
                            @Valid @RequestBody UpdateContactRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        log.info("Обновление email с id {} на '{}' для пользователя {}", emailId, request.getValue(), userId);
        contactService.updateEmail(userId, emailId, request.getValue());
    }

    @PostMapping("/me/phones")
    public void addPhone(Authentication authentication, @Valid @RequestBody UpdateContactRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        log.info("Добавление телефона '{}' для пользователя с id {}", request.getValue(), userId);
        contactService.addPhone(userId, request.getValue());
    }

    @DeleteMapping("/me/phones/{phoneId}")
    public void deletePhone(Authentication authentication, @PathVariable Long phoneId) {
        Long userId = (Long) authentication.getPrincipal();
        log.info("Удаление телефона с id {} для пользователя {}", phoneId, userId);
        contactService.deletePhone(userId, phoneId);
    }

    @PutMapping("/me/phones/{phoneId}")
    public void updatePhone(Authentication authentication,
                            @PathVariable Long phoneId,
                            @Valid @RequestBody UpdateContactRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        log.info("Обновление телефона с id {} на '{}' для пользователя {}", phoneId, request.getValue(), userId);
        contactService.updatePhone(userId, phoneId, request.getValue());
    }
}