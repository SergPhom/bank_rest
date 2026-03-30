package com.example.bankcards.controller;

import com.example.bankcards.BaseTester;
import com.example.bankcards.dto.card.CardCreateRequest;
import com.example.bankcards.dto.card.CardStatusChangeRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.entity.enums.Status;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
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
        var card = Card.builder()
                .number(123L)
                .cardHolder(user)
                .balance(BigDecimal.ZERO)
                .status(Status.CREATED)
                .build();

        when(userRepository.findById(guid)).thenReturn(Optional.of(user));
        doReturn(query).when(entityManager).createNativeQuery("SELECT nextval('card_number_seq')");
        doReturn(123L).when(query).getSingleResult();
        doReturn(card).when(cardRepository).save(any(Card.class));

        var response = mockMvc.perform(post("/bank/api/card/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("{\"id\":null,\"number\":\"**** **** **** 0123\",\"cardHolderName\":null," +
                "\"cardHolderId\":\"%s\",\"validThrough\":null,\"status\":\"CREATED\",".formatted(guid) +
                "\"balance\":0,\"hasBlockRequest\":false}", response, JSONCompareMode.LENIENT);
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
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("{\"id\":null,\"number\":\"**** **** **** null\",\"cardHolderName\":null," +
                "\"cardHolderId\":null,\"validThrough\":null,\"status\":\"ACTIVE\",\"balance\":0,\"" +
                "hasBlockRequest\":false}", result, JSONCompareMode.LENIENT);
    }

    @Test
    void getCardsPage() throws Exception {
        var user = com.example.bankcards.entity.User.builder()
                .email("user@example.com")
                .build();
        var cards = new ArrayList<Card>();
        cards.add(Card.builder().cardHolder(user).build());
        var page = new PageImpl<>(cards, PageRequest.of(0, 10), cards.size());

        doReturn(page).when(cardRepository).findAll(any(Specification.class), any(Pageable.class));

        var result = mockMvc
                .perform(get("/bank/api/card/get-filtered-page")
                        .param("page", "0")
                        .param("size", "10")
                        .param("statuses", "ACTIVE"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("{\"content\":[{\"id\":null,\"number\":\"**** **** **** null\"," +
                "\"cardHolderName\":null,\"cardHolderId\":null,\"validThrough\":null,\"status\":\"CREATED\"," +
                "\"balance\":0,\"hasBlockRequest\":false}],\"pageable\":{\"pageNumber\":0,\"pageSize\":10," +
                "\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"offset\":0,\"paged\":true,\"" +
                "unpaged\":false},\"last\":true,\"totalElements\":1,\"totalPages\":1,\"first\":true,\"size\":10," +
                "\"number\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"numberOfElements\":1," +
                "\"empty\":false}", result, JSONCompareMode.LENIENT);
    }

    @Test
    void createWithValidationException() throws Exception {
        mockMvc
                .perform(post("/bank/api/card/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardHolderGuid\":\"cardHolderGuid\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createWithCardNotFoundException() throws Exception {
        mockMvc
                .perform(post("/bank/api/card/set-new-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardGuid\":\"12345678-1234-1234-1234-123456789012\",\"newStatus\":\"ACTIVE\"}"))
                .andExpect(status().isNotFound());
    }
}