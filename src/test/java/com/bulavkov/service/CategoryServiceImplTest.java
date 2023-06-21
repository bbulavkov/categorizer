package com.bulavkov.service;

import com.bulavkov.dto.CategorizeRequestDto;
import org.ahocorasick.trie.PayloadEmit;
import org.ahocorasick.trie.PayloadTrie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private BrowserService browserService;
    @Mock
    private PayloadTrie<String> stringPayloadTire;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void shouldReturnEmptyResultWhenRequestedUrlsAreEmpty() {
        CategorizeRequestDto requestDto = new CategorizeRequestDto();

        Map<String, Set<String>> categorize = categoryService.categorize(requestDto);

        assertTrue(categorize.isEmpty());
    }

    @Test
    void shouldReturnCategorizedResultWhenCategoriesMatch() {
        String url = "https://www.google.com/search";
        String text = "Some very long text";
        String category = "long text";

        CategorizeRequestDto requestDto = new CategorizeRequestDto();
        requestDto.setUrls(Collections.singleton(url));
        requestDto.setCategories(Collections.singleton(category));

        when(browserService.browse(url)).thenReturn(text);

        PayloadEmit<String> payload = mock(PayloadEmit.class);
        when(payload.getPayload()).thenReturn(category);
        when(stringPayloadTire.parseText(text)).thenReturn(Collections.singletonList(payload));

        ReflectionTestUtils.setField(categoryService, "payloadTrie", stringPayloadTire);

        Map<String, Set<String>> categorize = categoryService.categorize(requestDto);

        assertFalse(categorize.isEmpty());
        Set<String> categories = categorize.get(url);
        assertFalse(categories.isEmpty());
        assertTrue(categories.contains(category));
    }
}