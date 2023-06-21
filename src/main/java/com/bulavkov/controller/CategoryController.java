package com.bulavkov.controller;

import com.bulavkov.dto.CategorizeRequestDto;
import com.bulavkov.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Categorizes web pages based on categories.
     * Fails fast.
     *
     * @param request CategorizeRequestDto
     * @return Map<String, Set < String>> where key is a url, values set of matching categories.
     * Meaning text could have some categories (cat, dog, feed) request only  (cat, dog ). The result categories set
     * will be cat and dog.
     */
    @PostMapping("/categorize")
    public Map<String, Set<String>> categorize(@Valid @RequestBody CategorizeRequestDto request) {
        return categoryService.categorize(request);
    }
}
