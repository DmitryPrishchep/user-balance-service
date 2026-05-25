package by.staryhroft.userbalance.service.impl;

import by.staryhroft.userbalance.entity.EmailData;
import by.staryhroft.userbalance.entity.PhoneData;
import by.staryhroft.userbalance.entity.User;
import by.staryhroft.userbalance.exception.*;
import by.staryhroft.userbalance.repository.EmailDataRepository;
import by.staryhroft.userbalance.repository.PhoneDataRepository;
import by.staryhroft.userbalance.repository.UserRepository;
import by.staryhroft.userbalance.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final UserRepository userRepository;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;

    public ContactServiceImpl(UserRepository userRepository,
                              EmailDataRepository emailDataRepository,
                              PhoneDataRepository phoneDataRepository) {
        this.userRepository = userRepository;
        this.emailDataRepository = emailDataRepository;
        this.phoneDataRepository = phoneDataRepository;
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void addEmail(Long userId, String email) {
        log.info("Добавление email '{}' для пользователя с id {}", email, userId);
        if (emailDataRepository.existsByEmail(email)) {
            throw new EmailAlreadyTakenException("Этот email уже занят");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        EmailData emailData = new EmailData();
        emailData.setEmail(email);
        emailData.setUser(user);
        emailDataRepository.save(emailData);
        log.info("Email '{}' успешно добавлен", email);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void deleteEmail(Long userId, Long emailId) {
        log.info("Удаление email с id {} у пользователя {}", emailId, userId);
        List<EmailData> emails = emailDataRepository.findByUserId(userId);
        if (emails.size() <= 1) {
            throw new IllegalStateException("У пользователя должен быть хотя бы один email");
        }
        EmailData emailData = emailDataRepository.findById(emailId)
                .orElseThrow(() -> new ContactNotFoundException("Email не найден"));
        if (!emailData.getUser().getId().equals(userId)) {
            throw new SecurityException("Доступ запрещён");
        }
        emailDataRepository.delete(emailData);
        log.info("Email с id {} удалён", emailId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void updateEmail(Long userId, Long emailId, String newEmail) {
        log.info("Обновление email с id {} на '{}' для пользователя {}", emailId, newEmail, userId);
        if (emailDataRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyTakenException("Этот email уже занят");
        }
        EmailData emailData = emailDataRepository.findById(emailId)
                .orElseThrow(() -> new ContactNotFoundException("Email не найден"));
        if (!emailData.getUser().getId().equals(userId)) {
            throw new SecurityException("Доступ запрещён");
        }
        emailData.setEmail(newEmail);
        emailDataRepository.save(emailData);
        log.info("Email обновлён на '{}'", newEmail);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void addPhone(Long userId, String phone) {
        log.info("Добавление телефона '{}' для пользователя с id {}", phone, userId);
        if (phoneDataRepository.existsByPhone(phone)) {
            throw new PhoneAlreadyTakenException("Этот телефон уже занят");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        PhoneData phoneData = new PhoneData();
        phoneData.setPhone(phone);
        phoneData.setUser(user);
        phoneDataRepository.save(phoneData);
        log.info("Телефон '{}' успешно добавлен", phone);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void deletePhone(Long userId, Long phoneId) {
        log.info("Удаление телефона с id {} у пользователя {}", phoneId, userId);
        List<PhoneData> phones = phoneDataRepository.findByUserId(userId);
        if (phones.size() <= 1) {
            throw new IllegalStateException("У пользователя должен быть хотя бы один телефон");
        }
        PhoneData phoneData = phoneDataRepository.findById(phoneId)
                .orElseThrow(() -> new ContactNotFoundException("Телефон не найден"));
        if (!phoneData.getUser().getId().equals(userId)) {
            throw new SecurityException("Доступ запрещён");
        }
        phoneDataRepository.delete(phoneData);
        log.info("Телефон с id {} удалён", phoneId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void updatePhone(Long userId, Long phoneId, String newPhone) {
        log.info("Обновление телефона с id {} на '{}' для пользователя {}", phoneId, newPhone, userId);
        if (phoneDataRepository.existsByPhone(newPhone)) {
            throw new PhoneAlreadyTakenException("Этот телефон уже занят");
        }
        PhoneData phoneData = phoneDataRepository.findById(phoneId)
                .orElseThrow(() -> new ContactNotFoundException("Телефон не найден"));
        if (!phoneData.getUser().getId().equals(userId)) {
            throw new SecurityException("Доступ запрещён");
        }
        phoneData.setPhone(newPhone);
        phoneDataRepository.save(phoneData);
        log.info("Телефон обновлён на '{}'", newPhone);
    }
}