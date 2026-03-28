package com.example.bankcards.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "Ответ с данными пользователя")
public class UserResponse {
    @Schema(description = "ID пользователя")
    private UUID uuid;
    @Schema(description = "имя пользователя")
    private String name;
    @Schema(description = "Email пользователя")
    private String email;
    @Schema(description = "роли пользователя")
    private Set<String> roles;
    @Schema(description = "флаг удаления")
    private boolean isActive;
}
