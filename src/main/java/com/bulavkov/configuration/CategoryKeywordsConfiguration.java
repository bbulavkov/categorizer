package com.bulavkov.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "application-configuration")
public class CategoryKeywordsConfiguration {
    @NotEmpty
    private Map<String, List<String>> categoryKeywords;
}
