package by.staryhroft.userbalance.service;

import by.staryhroft.userbalance.dto.SearchRequest;
import by.staryhroft.userbalance.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDto> searchUsers(SearchRequest request, Pageable pageable);
    UserDto getUserById(Long id);
}
