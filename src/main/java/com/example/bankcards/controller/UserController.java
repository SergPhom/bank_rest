package com.example.bankcards.controller;

import com.example.bankcards.dto.user.UserFilter;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.dto.user.UserUpdateRequest;
import com.example.bankcards.service.api.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Администрирование пользователей")
@RequestMapping("/bank/api/user")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Получение пользователей карт по фильтрам")
    @GetMapping("/get-filtered-page")
    Page<UserResponse> getUsersPage(@ParameterObject Pageable pageable, @ParameterObject UserFilter filter) {
        return userService.getUsersPage(pageable, filter);
    }

    @Operation(summary = "Обновить пользователя")
    @PostMapping("/update")
    UserResponse updateUser(@RequestBody @Valid UserUpdateRequest request) {
        return userService.updateUser(request);
    }
}
