package com.example.bankcards.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Запрос на обновление данных пользователя")
public class UserUpdateRequest {
    @Schema(description = "ID пользователя")
    @NotNull(message = "ID пользователя не может быть пустым")
    private UUID uuid;
    @Schema(description = "Имя пользователя")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    private String name;
    @Schema(description = "Адрес электронной почты пользователя")
    @Email(message = "Некорректный формат email")
    @Size(max = 50, message = "Адрес почты пользователя должно содержать до 50 символов")
    private String email;

    @Schema(description = "Флаг удаления пользователя")
    @JsonProperty("isActive")
    private boolean isActive;
}
