package by.staryhroft.userbalance.service.impl;

import by.staryhroft.userbalance.dto.SearchRequest;
import by.staryhroft.userbalance.dto.UserDto;
import by.staryhroft.userbalance.entity.User;
import by.staryhroft.userbalance.repository.UserRepository;
import by.staryhroft.userbalance.service.UserService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Page<UserDto> searchUsers(SearchRequest request, Pageable pageable) {
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

        return userRepository.findAll(spec, pageable)
                .map(this::toDto);
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return toDto(user);
    }

    private UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .dateOfBirth(user.getDateOfBirth())
                .emails(user.getEmails().stream().map(e -> e.getEmail()).collect(Collectors.toList()))
                .phones(user.getPhones().stream().map(p -> p.getPhone()).collect(Collectors.toList()))
                .build();
    }
}