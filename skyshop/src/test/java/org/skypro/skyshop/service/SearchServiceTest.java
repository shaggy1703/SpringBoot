package org.skypro.skyshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private StorageService storageService;
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        searchService = new SearchService(storageService);
    }

    @Test
    void shouldReturnEmptyListWhenNoObjectsInStorage() {

        when(storageService.getAllSearchable()).thenReturn(Collections.emptyList());

        List<SearchResult> results = (List<SearchResult>) searchService.search("test");

        assertTrue(results.isEmpty());
        verify(storageService).getAllSearchable();
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingObjects() {

        Article article = new Article(UUID.randomUUID(), "Test Article", "Test content");
        Product product = new Product(UUID.randomUUID(), "Test Product") {
            @Override
            public int getPrice() {
                return 100;
            }
        };

        when(storageService.getAllSearchable()).thenReturn(Arrays.asList(article, product));

        List<SearchResult> results = (List<SearchResult>) searchService.search("nonexistent");

        assertTrue(results.isEmpty());
        verify(storageService).getAllSearchable();
    }

    @Test
    void shouldReturnMatchingObjectsWhenObjectsExist() {

        UUID articleId = UUID.randomUUID();
        Article article = new Article(articleId, "Test Article", "This contains search term");
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Search Product") {
            @Override
            public int getPrice() {
                return 100;
            }
        };

        when(storageService.getAllSearchable()).thenReturn(Arrays.asList(article, product));

        List<SearchResult> results = (List<SearchResult>) searchService.search("search");

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(result -> result.getId().equals(articleId.toString())));
        assertTrue(results.stream().anyMatch(result -> result.getId().equals(productId.toString())));
        verify(storageService).getAllSearchable();
    }

    @Test
    void shouldReturnAllObjectsWhenSearchPatternIsNull() {

        Article article = new Article(UUID.randomUUID(), "Test Article", "Content");
        Product product = new Product(UUID.randomUUID(), "Test Product") {
            @Override
            public int getPrice() {
                return 100;
            }
        };

        when(storageService.getAllSearchable()).thenReturn(Arrays.asList(article, product));

        List<SearchResult> results = (List<SearchResult>) searchService.search(null);

        assertEquals(2, results.size());
        verify(storageService).getAllSearchable();
    }

    @Test
    void shouldReturnAllObjectsWhenSearchPatternIsEmpty() {

        Article article = new Article(UUID.randomUUID(), "Test Article", "Content");
        Product product = new Product(UUID.randomUUID(), "Test Product") {
            @Override
            public int getPrice() {
                return 100;
            }
        };

        when(storageService.getAllSearchable()).thenReturn(Arrays.asList(article, product));

        List<SearchResult> results = (List<SearchResult>) searchService.search("");

        assertEquals(2, results.size());
        verify(storageService).getAllSearchable();
    }
}
