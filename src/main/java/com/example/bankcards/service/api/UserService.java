package com.example.bankcards.service.api;

import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.dto.user.UserFilter;
import com.example.bankcards.dto.user.UserUpdateRequest;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public interface UserService {
    UserDetailsService userDetailsService();

    Page<UserResponse> getUsersPage(Pageable pageable, UserFilter filter);
    UserResponse updateUser(UserUpdateRequest request);

    void create(User user);

    String getCurrentUsername();
}
