package com.example.bankcards.dto.card;

import com.example.bankcards.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на активацию карты")
public class CardStatusChangeRequest {
    @Schema(description = "ID карты")
    @NotBlank(message = "ID карты не может быть пустым")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    private String cardGuid;

    @Schema(description = "новый статус карты")
    @NotNull(message = "новый статус карты не может быть пустым")
    private Status newStatus;
}
