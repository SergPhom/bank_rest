package com.example.bankcards.controller;

import com.example.bankcards.BaseTester;
import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.entity.enums.Status;
import com.example.bankcards.entity.enums.TransferResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserCardControllerTest extends BaseTester {
    @BeforeEach
    void setup() {
        UserDetails userDetails = User.withUsername("user@example.com")
                .password("password")
                .roles("USER")
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void getCardsPage() throws Exception {
        var user = com.example.bankcards.entity.User.builder()
                .email("user@example.com")
                .build();
        var cards = new ArrayList<Card>();
        cards.add(Card.builder()
                .cardHolder(user)
                .build());
        var page = new PageImpl<>(cards, PageRequest.of(0, 10), cards.size());

        doReturn(page).when(cardRepository).findAll(any(Specification.class), any(Pageable.class));

        var result = mockMvc
                .perform(get("/bank/api/user-cards/get-filtered-page")
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
    void getCardBalance() throws Exception {
        var card = Card.builder()
                .balance(BigDecimal.ZERO)
                .build();

        doReturn(Optional.of(card)).when(cardRepository).findByNumber(1234L);

        var result = mockMvc
                .perform(get("/bank/api/user-cards/balance/1234"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertEquals("0", result);
    }

    @Test
    void requestCardBlock() throws Exception {
        var uuid = UUID.randomUUID();
        var card = Card.builder().build();
        var blockedCard = Card.builder().hasBlockRequest(true).build();

        doReturn(Optional.of(card)).when(cardRepository).findById(uuid);
        doReturn(blockedCard).when(cardRepository).save(card);

        var result = mockMvc
                .perform(get("/bank/api/user-cards/block/{cardId}", uuid))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("{\"id\":null,\"number\":\"**** **** **** null\",\"cardHolderName\":null," +
                "\"cardHolderId\":null,\"validThrough\":null,\"status\":\"CREATED\",\"balance\":0," +
                "\"hasBlockRequest\":true}", result, JSONCompareMode.LENIENT);
    }

    @Test
    void cardTransfer() throws Exception {
        var sourceId = UUID.randomUUID();
        var targetId = UUID.randomUUID();
        var request = TransferRequest.builder()
                .sourceCardId(sourceId)
                .targetCardId(targetId)
                .transferAmount(BigDecimal.valueOf(1000L))
                .build();
        var transfer = Transfer.builder()
                .sourceCardId(sourceId)
                .targetCardId(targetId)
                .transferAmount(BigDecimal.valueOf(1000L))
                .result(TransferResult.SUCCESS)
                .build();
        var source = Card.builder()
                .id(sourceId)
                .balance(BigDecimal.valueOf(2000L))
                .status(Status.ACTIVE)
                .build();
        var target = Card.builder()
                .id(targetId)
                .status(Status.ACTIVE)
                .build();

        doReturn(Optional.of(source)).when(cardRepository).findById(sourceId);
        doReturn(Optional.of(target)).when(cardRepository).findById(targetId);
        doReturn(transfer).when(transferRepository).save(any(Transfer.class));

        var result = mockMvc
                .perform(post("/bank/api/user-cards/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("{\"result\":\"SUCCESS\",\"rejectionReason\":null}", result,
                JSONCompareMode.LENIENT);
    }

    @Test
    void transferWithException() throws Exception {
        mockMvc
                .perform(post("/bank/api/user-cards/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sourceCardId\":\"sourceCardId\",\"targetCardId\":\"targetCardId\",\"transferAmount\":\"amount\"}"))
                .andExpect(status().is4xxClientError());
    }
}