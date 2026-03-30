package com.example.bankcards.controller;

import com.example.bankcards.BaseTester;
import com.example.bankcards.dto.card.CardCreateRequest;
import com.example.bankcards.dto.card.CardStatusChangeRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.entity.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CardControllerTest extends BaseTester {
    @Autowired
    private CardController controller;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void createCard() throws Exception {
        var guid = UUID.randomUUID();
        var request = CardCreateRequest.builder()
                .cardHolderGuid(guid.toString())
                .build();
        var user = User.builder()
                .id(guid)
                .email("user@example.com")
                .roles(Set.of(Role.ROLE_USER))
                .build();

        when(userRepository.findById(guid)).thenReturn(Optional.of(user));
        doReturn(query).when(entityManager).createNativeQuery("SELECT nextval('card_number_seq')");
        doReturn(123L).when(query).getSingleResult();

        var response = mockMvc.perform(post("/bank/api/card/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(request)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void changeCardStatus() throws Exception {
        var guid = UUID.randomUUID();
        var request = CardStatusChangeRequest.builder()
                .cardGuid(guid.toString())
                .newStatus(Status.ACTIVE)
                .build();
        var card = Card.builder().build();

        doReturn(Optional.of(card)).when(cardRepository).findById(guid);

        var result = mockMvc.perform(post("/bank/api/card/set-new-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(request)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getCardsPage() throws Exception {
        var cards = new ArrayList<Card>();
        cards.add(Card.builder().build());
        var page = new PageImpl<>(cards, PageRequest.of(0, 10), cards.size());

        doReturn(page).when(cardRepository).findAll(any(Specification.class), any(Pageable.class));

        var result = mockMvc
                .perform(get("/bank/api/card/get-filtered-page")
                        .param("page", "0")
                        .param("size", "10")
                        .param("statuses", "ACTIVE"))
                .andExpect(status().isOk())
                .andReturn();
    }
}