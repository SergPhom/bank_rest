package com.example.bankcards.controller;

import com.example.bankcards.dto.card.CardFilter;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.dto.transfer.TransferResponse;
import com.example.bankcards.service.api.CardService;
import com.example.bankcards.service.api.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@Tag(name = "Управление картами пользователя")
@RequestMapping("bank/api/user-cards")
@RequiredArgsConstructor
public class UserCardController {
    private final CardService cardService;
    private final TransferService transferService;

    @Operation(summary = "Получение всех карт по фильтрам")
    @GetMapping("/get-filtered-page")
    Page<CardResponse> getCardsPage(@ParameterObject Pageable pageable, @ParameterObject CardFilter filter) {
        return cardService.getUserCardsPage(pageable, filter);
    }

    @Operation(summary = "Получение баланса карты по номеру")
    @GetMapping("/balance/{number}")
    BigDecimal getCardsPage(@PathVariable(name = "number") Long number) {
        return cardService.getCardBalance(number);
    }

    @Operation(summary = "Запрос на блокировку карты по ID")
    @GetMapping("/block/{cardId}")
    CardResponse requestCardBlock(@PathVariable(name = "cardId") UUID cardId) {
        return cardService.requestBlock(cardId);
    }

    @Operation(summary = "Запрос на перевод между своими картами")
    @PostMapping("/transfer")
    TransferResponse cardTransfer(@RequestBody @Valid TransferRequest request) {
        return transferService.doTransfer(request);
    }
}
