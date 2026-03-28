package com.example.bankcards.service.api;

import com.example.bankcards.dto.card.CardCreateRequest;
import com.example.bankcards.dto.card.CardFilter;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.dto.card.CardStatusChangeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface CardService {
    CardResponse createCard(CardCreateRequest request);

    CardResponse changeCardStatus(CardStatusChangeRequest request);

    Page<CardResponse> getCardsPage(Pageable pageable, CardFilter filter);

    Page<CardResponse> getUserCardsPage(Pageable pageable, CardFilter filter);

    CardResponse requestBlock(UUID cardId);

    BigDecimal getCardBalance(Long number);
}
