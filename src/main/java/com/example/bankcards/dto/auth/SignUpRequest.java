package com.example.bankcards.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию")
public class SignUpRequest {
    @Schema(description = "Имя пользователя")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    private String username;
    @Schema(description = "Адрес электронной почты")
    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email не может быть пустым")
    @Size(max = 50, message = "Адрес почты пользователя должно содержать до 50 символов")
    private String email;
    @Schema(description = "Пароль")
    @Size(max = 50, message = "Пароль должно содержать до 50 символов")
    private String password;
}
