package com.bulavkov.service;

import com.bulavkov.dto.CategorizeRequestDto;

import java.util.Map;
import java.util.Set;

public interface CategoryService {
    Map<String, Set<String>> categorize(CategorizeRequestDto request);
}
