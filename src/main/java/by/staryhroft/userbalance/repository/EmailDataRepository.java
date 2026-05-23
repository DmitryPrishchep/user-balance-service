package by.staryhroft.userbalance.repository;

import by.staryhroft.userbalance.entity.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailDataRepository extends JpaRepository<EmailData, Long> {

    boolean existsByEmail(String email);

    List<EmailData> findByUserId(Long userId);
}
