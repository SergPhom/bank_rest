package com.example.bankcards.controller;

import com.example.bankcards.service.api.CardService;
import com.example.bankcards.dto.card.CardCreateRequest;
import com.example.bankcards.dto.card.CardFilter;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.dto.card.CardStatusChangeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Управление картами")
@RequestMapping("bank/api/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @Operation(summary = "Создание карты")
    @PostMapping("/create")
    CardResponse createCard(@RequestBody @Valid CardCreateRequest request) {
        return cardService.createCard(request);
    }

    @Operation(summary = "Активация, блокировка, удаление карты")
    @PostMapping("/set-new-status")
    CardResponse changeCardStatus(@RequestBody @Valid CardStatusChangeRequest request) {
        return cardService.changeCardStatus(request);
    }

    @Operation(summary = "Получение всех карт по фильтрам")
    @GetMapping("/get-filtered-page")
    Page<CardResponse> getCardsPage(@ParameterObject Pageable pageable, @ParameterObject CardFilter filter) {
        return cardService.getCardsPage(pageable, filter);
    }
}
