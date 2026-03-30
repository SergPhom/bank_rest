package com.example.bankcards.controller;

import com.example.bankcards.BaseTester;
import com.example.bankcards.dto.user.UserUpdateRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseTester {
    @Autowired
    private UserController controller;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void getUsersPage() throws Exception {
        var users = new ArrayList<User>();
        users.add(User.builder()
                .roles(Set.of(Role.ROLE_USER))
                .build());
        var page = new PageImpl<>(users, PageRequest.of(0, 10), users.size());

        doReturn(page).when(userRepository).findAll(any(Specification.class), any(Pageable.class));

        var result = mockMvc
                .perform(get("/bank/api/user/get-filtered-page")
                        .param("page", "0")
                        .param("size", "10")
                        .param("isActive", "true"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("{\"content\":[{\"uuid\":null,\"name\":null,\"email\":null," +
                "\"roles\":[\"ROLE_USER\"],\"active\":false}],\"pageable\":{\"pageNumber\":0," +
                "\"pageSize\":10,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"offset\":0," +
                "\"paged\":true,\"unpaged\":false},\"last\":true,\"totalPages\":1,\"totalElements\":1," +
                "\"first\":true,\"size\":10,\"number\":0,\"sort\":{\"empty\":true,\"sorted\":false," +
                "\"unsorted\":true},\"numberOfElements\":1,\"empty\":false}", result, JSONCompareMode.LENIENT);
    }

    @Test
    void updateUser() throws Exception {
        var request = UserUpdateRequest.builder()
                .email("new@example.com")
                .name("newName")
                .build();
        var user = User.builder()
                .roles(Set.of(Role.ROLE_USER))
                .build();

        doReturn(Optional.of(user)).when(userRepository).findById(any());
        doReturn(user).when(userRepository).save(any(User.class));

        var result = mockMvc.perform(post("/bank/api/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("{\"uuid\":null,\"name\":\"newName\",\"email\":\"new@example.com\"," +
                "\"roles\":[\"ROLE_USER\"],\"active\":false}", result, JSONCompareMode.LENIENT);
    }
}