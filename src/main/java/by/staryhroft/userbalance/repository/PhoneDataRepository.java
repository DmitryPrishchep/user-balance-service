package by.staryhroft.userbalance.repository;

import by.staryhroft.userbalance.entity.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {

    boolean existsByPhone(String phone);

    List<PhoneData> findByUserId(Long userId);
}