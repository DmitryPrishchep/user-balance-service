package by.staryhroft.userbalance.service.impl;

import by.staryhroft.userbalance.entity.EmailData;
import by.staryhroft.userbalance.entity.PhoneData;
import by.staryhroft.userbalance.entity.User;
import by.staryhroft.userbalance.repository.EmailDataRepository;
import by.staryhroft.userbalance.repository.PhoneDataRepository;
import by.staryhroft.userbalance.repository.UserRepository;
import by.staryhroft.userbalance.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final UserRepository userRepository;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void addEmail(Long userId, String email) {
        if (emailDataRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Этот email уже занят");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        EmailData emailData = new EmailData();
        emailData.setEmail(email);
        emailData.setUser(user);
        emailDataRepository.save(emailData);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void deleteEmail(Long userId, Long emailId) {
        List<EmailData> emails = emailDataRepository.findByUserId(userId);
        if (emails.size() <= 1) {
            throw new IllegalStateException("У пользователя должен быть хотя бы один email");
        }
        EmailData emailData = emailDataRepository.findById(emailId)
                .orElseThrow(() -> new RuntimeException("Email не найден"));
        if (!emailData.getUser().getId().equals(userId)) {
            throw new SecurityException("Доступ запрещён");
        }
        emailDataRepository.delete(emailData);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void updateEmail(Long userId, Long emailId, String newEmail) {
        if (emailDataRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Этот email уже занят");
        }
        EmailData emailData = emailDataRepository.findById(emailId)
                .orElseThrow(() -> new RuntimeException("Email не найден"));
        if (!emailData.getUser().getId().equals(userId)) {
            throw new SecurityException("Доступ запрещён");
        }
        emailData.setEmail(newEmail);
        emailDataRepository.save(emailData);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void addPhone(Long userId, String phone) {
        if (phoneDataRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException("Этот телефон уже занят");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        PhoneData phoneData = new PhoneData();
        phoneData.setPhone(phone);
        phoneData.setUser(user);
        phoneDataRepository.save(phoneData);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void deletePhone(Long userId, Long phoneId) {
        List<PhoneData> phones = phoneDataRepository.findByUserId(userId);
        if (phones.size() <= 1) {
            throw new IllegalStateException("У пользователя должен быть хотя бы один телефон");
        }
        PhoneData phoneData = phoneDataRepository.findById(phoneId)
                .orElseThrow(() -> new RuntimeException("Телефон не найден"));
        if (!phoneData.getUser().getId().equals(userId)) {
            throw new SecurityException("Доступ запрещён");
        }
        phoneDataRepository.delete(phoneData);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void updatePhone(Long userId, Long phoneId, String newPhone) {
        if (phoneDataRepository.existsByPhone(newPhone)) {
            throw new IllegalArgumentException("Этот телефон уже занят");
        }
        PhoneData phoneData = phoneDataRepository.findById(phoneId)
                .orElseThrow(() -> new RuntimeException("Телефон не найден"));
        if (!phoneData.getUser().getId().equals(userId)) {
            throw new SecurityException("Доступ запрещён");
        }
        phoneData.setPhone(newPhone);
        phoneDataRepository.save(phoneData);
    }
}