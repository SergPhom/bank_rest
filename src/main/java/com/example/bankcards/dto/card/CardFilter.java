package com.example.bankcards.dto.card;

import com.example.bankcards.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Data
@Schema(description = "Фильтр для карт")
public class CardFilter {
    @Schema(description = "Email пользователя")
    private String username;
    @Schema(description = "Статус карты")
    private Set<Status> statuses;
    @Schema(description = "Наличие флага запроса блокировки")
    @JsonProperty(value = "hasBlockRequest")
    private boolean hasBlockRequest;
}
