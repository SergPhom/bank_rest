package com.example.bankcards.dto.transfer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Schema(description = "Запрос на перевод между картами")
public class TransferRequest {
    @Schema(description = "ID карты откуда совершается перевод")
    @NotBlank(message = "ID карты откуда совершается перевод не может быть пустым")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    private UUID sourceCardId;
    @NotBlank(message = "ID карты куда совершается перевод не может быть пустым")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    @Schema(description = "ID карты куда совершается перевод")
    private UUID targetCardId;
    @NotBlank(message = "сумма перевода не может быть пустой")
    @Schema(description = "сумма перевода")
    @DecimalMin(value = "0.01", message = "Минимальная сумма — 0.01")
    @DecimalMax(value = "1000000.00", message = "Максимальная сумма — 1 млн")
    private BigDecimal transferAmount;
}
