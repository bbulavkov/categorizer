package com.bulavkov.service;

import com.bulavkov.configuration.CategoryKeywordsConfiguration;
import com.bulavkov.dto.CategorizeRequestDto;
import lombok.RequiredArgsConstructor;
import org.ahocorasick.trie.PayloadEmit;
import org.ahocorasick.trie.PayloadTrie;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Categorizes web pages based on a keyword category, searching for keywords provided by {@link CategoryKeywordsConfiguration}.
 *
 * <p>If a keyword from a category is found in the text, then the page is categorized with that
 * category. Match is case-insensitive.
 *
 * <p>Implementation leverages Aho-Corasick algorithm.
 * <p>See <a href="http://cr.yp.to/bib/1975/aho.pdf">white paper</a> by Aho and Corasick for algorithmic details.
 * <p><a href="https://github.com/robert-bor/aho-corasick#:~:text=The%20algorithm%20is%20O(n)."></a>.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryKeywordsConfiguration keywordsConfig;
    private final BrowserService browserService;
    private PayloadTrie<String> payloadTrie;

    @PostConstruct
    protected void init() {
        PayloadTrie.PayloadTrieBuilder<String> trieBuilder =
                PayloadTrie.<String>builder()
                        .ignoreCase()
                        .onlyWholeWords();

        keywordsConfig.getCategoryKeywords()
                .forEach((cat, words) -> words.forEach(w -> trieBuilder.addKeyword(w, cat)));

        this.payloadTrie = trieBuilder.build();
    }

    /**
     * @param request CategorizeRequestDto
     * @return Map<String, Set < String>> where key is a url, values set of matching categories.
     * Meaning text could have some categories (cat, dog, feed) request only  (cat, dog ). The result categories set
     * will be cat and dog.
     */
    @Override
    public Map<String, Set<String>> categorize(CategorizeRequestDto request) {
        Map<String, Set<String>> result = new HashMap<>();

        request.getUrls().stream().parallel().forEach(url -> {
            String text = browserService.browse(url);
            Set<String> matchedCategories = getMatchingCategories(text);

            if (!matchedCategories.isEmpty()) {
                result.put(url, matchedCategories);
            }
        });

        return result;
    }

    private Set<String> getMatchingCategories(String text) {
        return payloadTrie.parseText(text)
                .stream()
                .map(PayloadEmit::getPayload)
                .collect(Collectors.toSet());
    }
}
