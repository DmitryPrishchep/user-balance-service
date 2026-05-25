package by.staryhroft.userbalance.service.impl;

import by.staryhroft.userbalance.dto.SearchRequest;
import by.staryhroft.userbalance.dto.UserDto;
import by.staryhroft.userbalance.entity.User;
import by.staryhroft.userbalance.exception.UserNotFoundException;
import by.staryhroft.userbalance.repository.UserRepository;
import by.staryhroft.userbalance.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserDto> searchUsers(SearchRequest request, Pageable pageable) {
        log.info("Поиск пользователей с параметрами: name={}, dateOfBirth={}, phone={}, email={}",
                request.getName(), request.getDateOfBirth(), request.getPhone(), request.getEmail());
        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getName() != null && !request.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), request.getName().toLowerCase() + "%"));
            }
            if (request.getDateOfBirth() != null) {
                predicates.add(cb.greaterThan(root.get("dateOfBirth"), request.getDateOfBirth()));
            }
            if (request.getPhone() != null && !request.getPhone().isEmpty()) {
                var phoneJoin = root.join("phones");
                predicates.add(cb.equal(phoneJoin.get("phone"), request.getPhone()));
            }
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                var emailJoin = root.join("emails");
                predicates.add(cb.equal(emailJoin.get("email"), request.getEmail()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<UserDto> result = userRepository.findAll(spec, pageable)
                .map(this::toDto);
        log.info("Найдено {} пользователей", result.getTotalElements());
        return result;
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserDto getUserById(Long id) {
        log.info("Запрос пользователя по id {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден"));
        return toDto(user);
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setEmails(user.getEmails().stream().map(e -> e.getEmail()).collect(Collectors.toList()));
        dto.setPhones(user.getPhones().stream().map(p -> p.getPhone()).collect(Collectors.toList()));
        return dto;
    }
}