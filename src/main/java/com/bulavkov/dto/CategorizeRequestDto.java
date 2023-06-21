package com.bulavkov.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
public class CategorizeRequestDto {
    @NotEmpty
    private Set<String> urls = new HashSet<>();
    @NotEmpty
    private Set<String> categories = new HashSet<>();
}
