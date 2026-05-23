package by.staryhroft.userbalance.service;

public interface ContactService {
    void addEmail(Long userId, String email);
    void deleteEmail(Long userId, Long emailId);
    void updateEmail(Long userId, Long emailId, String newEmail);

    void addPhone(Long userId, String phone);
    void deletePhone(Long userId, Long phoneId);
    void updatePhone(Long userId, Long phoneId, String newPhone);
}
