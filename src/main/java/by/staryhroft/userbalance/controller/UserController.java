package by.staryhroft.userbalance.controller;

import by.staryhroft.userbalance.dto.*;
import by.staryhroft.userbalance.service.ContactService;
import by.staryhroft.userbalance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ContactService contactService;

    @GetMapping("/search")
    public Page<UserDto> search(SearchRequest request, Pageable pageable) {
        return userService.searchUsers(request, pageable);
    }

    @GetMapping("/me")
    public UserDto getMe(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return userService.getUserById(userId);
    }

    // Управление email
    @PostMapping("/me/emails")
    public void addEmail(Authentication authentication, @Valid @RequestBody UpdateContactRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        contactService.addEmail(userId, request.getValue());
    }

    @DeleteMapping("/me/emails/{emailId}")
    public void deleteEmail(Authentication authentication, @PathVariable Long emailId) {
        Long userId = (Long) authentication.getPrincipal();
        contactService.deleteEmail(userId, emailId);
    }

    @PutMapping("/me/emails/{emailId}")
    public void updateEmail(Authentication authentication,
                            @PathVariable Long emailId,
                            @Valid @RequestBody UpdateContactRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        contactService.updateEmail(userId, emailId, request.getValue());
    }

    // Аналогично для телефонов
    @PostMapping("/me/phones")
    public void addPhone(Authentication authentication, @Valid @RequestBody UpdateContactRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        contactService.addPhone(userId, request.getValue());
    }

    @DeleteMapping("/me/phones/{phoneId}")
    public void deletePhone(Authentication authentication, @PathVariable Long phoneId) {
        Long userId = (Long) authentication.getPrincipal();
        contactService.deletePhone(userId, phoneId);
    }

    @PutMapping("/me/phones/{phoneId}")
    public void updatePhone(Authentication authentication,
                            @PathVariable Long phoneId,
                            @Valid @RequestBody UpdateContactRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        contactService.updatePhone(userId, phoneId, request.getValue());
    }
}
