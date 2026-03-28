package com.example.bankcards.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Фильтр для пользователей")
public class UserFilter {
    @Schema(description = "Email пользователя")
    private String username;
    @Schema(description = "Статус пользователя")
    private Boolean isActive;
}
