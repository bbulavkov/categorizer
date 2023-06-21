package com.bulavkov.controller;

import com.bulavkov.dto.CategorizeRequestDto;
import com.bulavkov.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @MockBean
    private CategoryService categoryService;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnCategorizedResult() throws Exception {
        String url = "https://cottonon.com/UK/star-wars-basketball-size-7/1684391-04.html";
        Set<String> categories = new HashSet<>(Arrays.asList("Star Wars", "Basketball"));
        Map<String, Set<String>> response = Collections.singletonMap(url, categories);

        CategorizeRequestDto request = new CategorizeRequestDto();
        request.getUrls().add(url);
        request.setCategories(categories);

        when(categoryService.categorize(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/categorize")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['" + url + "']").isArray())
                .andExpect(jsonPath("$['" + url + "']", hasSize(2)))
                .andExpect(jsonPath("$['" + url + "']", hasItem("Basketball")))
                .andExpect(jsonPath("$['" + url + "']", hasItem("Star Wars")));
    }
}