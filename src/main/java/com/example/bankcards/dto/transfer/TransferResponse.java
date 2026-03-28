package com.example.bankcards.dto.transfer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Ответ при переводе между картами")
public class TransferResponse {
    @Schema(description = "Результат перевода (успех/отказ)")
    private String result;
    @Schema(description = "Причина отказа")
    private String rejectionReason;
}
