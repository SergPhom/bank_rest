package com.example.bankcards.dto.card;

import com.example.bankcards.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
