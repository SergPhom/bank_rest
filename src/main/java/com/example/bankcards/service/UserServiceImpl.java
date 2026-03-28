package com.example.bankcards.service;

import com.example.bankcards.dto.mapper.UserMapper;
import com.example.bankcards.dto.user.UserFilter;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.dto.user.UserUpdateRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.UserAlreadyExistsException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.repository.specification.UserSpecificationGenerator;
import com.example.bankcards.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserSpecificationGenerator specificationGenerator;
    private final UserMapper mapper;

    @Override
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    @Override
    public Page<UserResponse> getUsersPage(Pageable pageable, UserFilter filter) {
        var spec = specificationGenerator.generateUserSpec(filter);
        var userPage = repository.findAll(spec, pageable);

        return userPage.map(mapper::toDto);
    }

    @Transactional
    @Override
    public UserResponse updateUser(UserUpdateRequest request) {
        var user = repository.findById(request.getUuid())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким ID не найден"));

        mapper.updateEntityFromDto(request, user);
        var savedUser = repository.save(user);

        return mapper.toDto(savedUser);
    }

    @Override
    public void create(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        repository.save(user);
    }

    @Override
    public String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private User getByUsername(String username) {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким email не найден"));
    }
}
