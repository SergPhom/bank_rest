package com.example.bankcards.dto.transfer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на перевод между картами")
public class TransferRequest {
    @Schema(description = "ID карты откуда совершается перевод")
    @NotNull(message = "ID карты откуда совершается перевод не может быть пустым")
    private UUID sourceCardId;

    @NotNull(message = "ID карты куда совершается перевод не может быть пустым")
    @Schema(description = "ID карты куда совершается перевод")
    private UUID targetCardId;

    @NotNull(message = "сумма перевода не может быть пустой")
    @Schema(description = "сумма перевода")
    @DecimalMin(value = "0.01", message = "Минимальная сумма — 0.01")
    @DecimalMax(value = "1000000.00", message = "Максимальная сумма — 1 млн")
    private BigDecimal transferAmount;
}
