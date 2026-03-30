package com.example.bankcards.service;

import com.example.bankcards.dto.card.CardCreateRequest;
import com.example.bankcards.dto.card.CardFilter;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.dto.card.CardStatusChangeRequest;
import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.Status;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.repository.specification.CardSpecificationGenerator;
import com.example.bankcards.service.api.CardService;
import com.example.bankcards.service.api.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final UserService userService;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardSpecificationGenerator specificationGenerator;
    private final CardMapper mapper;
    private final EntityManager entityManager;


    @Override
    public CardResponse createCard(CardCreateRequest request) {
        var user = userRepository.findById(UUID.fromString(request.getCardHolderGuid()))
                .orElseThrow(RuntimeException::new);
        var newCard = Card.builder()
                .cardHolder(user)
                .number(getNextNumber())
                .validThrough(YearMonth.now().plusYears(1L))
                .build();
        var savedCard = cardRepository.save(newCard);

        return mapper.toDto(savedCard);
    }

    @Transactional
    @Override
    public CardResponse changeCardStatus(CardStatusChangeRequest request) {
        var card = cardRepository.findById(UUID.fromString(request.getCardGuid()))
                .orElseThrow(CardNotFoundException::new);

        card.setStatus(request.getNewStatus());
        cardRepository.save(card);

        return mapper.toDto(card);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CardResponse> getCardsPage(Pageable pageable, CardFilter filter) {
        var spec = specificationGenerator.generateCardSpec(filter);
        var cardsPage = cardRepository.findAll(spec, pageable);

        return cardsPage.map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CardResponse> getUserCardsPage(Pageable pageable, CardFilter filter) {
        var username = userService.getCurrentUsername();
        var statuses = Set.of(Status.ACTIVE, Status.BLOCKED);

        filter.setUsername(username);
        filter.setStatuses(statuses);

        return getCardsPage(pageable, filter);
    }

    @Transactional
    @Override
    public CardResponse requestBlock(UUID cardId) {
        var card = cardRepository.findById(cardId)
                .orElseThrow(CardNotFoundException::new);
        card.setHasBlockRequest(true);
        var savedCard = cardRepository.save(card);

        return mapper.toDto(savedCard);
    }

    @Override
    public BigDecimal getCardBalance(Long number) {
        return cardRepository.findByNumber(number)
                .map(Card::getBalance)
                .orElseThrow(CardNotFoundException::new);
    }

    private Long getNextNumber() {
        return ((Number) entityManager.createNativeQuery("SELECT nextval('card_number_seq')")
                .getSingleResult()).longValue();
    }
}
