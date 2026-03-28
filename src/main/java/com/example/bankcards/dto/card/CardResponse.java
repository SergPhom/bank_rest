package com.example.bankcards.dto.card;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с созданной картой")
public class CardResponse {
    @Schema(description = "ID карты")
    private UUID id;

    @Schema(description = "номер карты")
    private String number;

    @Schema(description = "имя владельца карты")
    private String cardHolderName;

    @Schema(description = "ID владельца карты")
    private UUID cardHolderId;

    @Schema(description = "действительна до")
    private YearMonth validThrough;

    @Schema(description = "статус карты")
    private String status;

    @Schema(description = "баланс карты")
    private BigDecimal balance;

    @Schema(description = "флаг запроса от пользователя на блокировку")
    private boolean hasBlockRequest;
}
