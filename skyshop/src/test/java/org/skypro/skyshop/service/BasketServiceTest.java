package org.skypro.skyshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import java.util.*;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private ProductBasket productBasket;

    @Mock
    private StorageService storageService;

    private BasketService basketService;

    @BeforeEach
    void setUp() {
        basketService = new BasketService(productBasket, storageService);
    }

    @Test
    void shouldThrowExceptionWhenAddingNonexistentProduct() {

        UUID nonExistentId = UUID.randomUUID();
        when(storageService.getProductById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(NoSuchProductException.class, () -> {
            basketService.addProductToBasket(nonExistentId);
        });

        verify(storageService).getProductById(nonExistentId);
        verifyNoInteractions(productBasket);
    }

    @Test
    void shouldAddProductToBasketWhenProductExists() {

        UUID productId = UUID.randomUUID();
        Product product = createMockProduct(productId, "Test Product", 100);
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));

        basketService.addProductToBasket(productId);

        verify(storageService).getProductById(productId);
        verify(productBasket).addProduct(productId);
    }

    @Test
    void shouldReturnEmptyBasketWhenProductBasketIsEmpty() {

        when(productBasket.getProducts()).thenReturn(Collections.emptyMap());

        UserBasket userBasket = basketService.getUserBasket();

        assertNotNull(userBasket);
        assertTrue(userBasket.getItems().isEmpty());
        assertEquals(0, userBasket.getTotal());
        verify(productBasket).getProducts();
    }

    @Test
    void shouldReturnBasketWithItemsWhenProductBasketHasProducts() {

        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        Product product1 = createMockProduct(productId1, "Product 1", 100);
        Product product2 = createMockProduct(productId2, "Product 2", 200);

        Map<UUID, Integer> basketMap = new HashMap<>();
        basketMap.put(productId1, 2); // 2 штуки продукта 1
        basketMap.put(productId2, 1); // 1 штука продукта 2

        when(productBasket.getProducts()).thenReturn(basketMap);
        when(storageService.getProductByIdOrThrow(productId1)).thenReturn(product1);
        when(storageService.getProductByIdOrThrow(productId2)).thenReturn(product2);

        UserBasket userBasket = basketService.getUserBasket();

        assertNotNull(userBasket);
        assertEquals(2, userBasket.getItems().size());
        assertEquals(400, userBasket.getTotal()); // (100 * 2) + (200 * 1) = 400

        List<UUID> itemIds = userBasket.getItems().stream()
                .map(item -> item.getProduct().getId())
                .collect(Collectors.toList());

        assertTrue(itemIds.contains(productId1));
        assertTrue(itemIds.contains(productId2));

        verify(productBasket).getProducts();
        verify(storageService).getProductByIdOrThrow(productId1);
        verify(storageService).getProductByIdOrThrow(productId2);
    }

    @Test
    void shouldThrowExceptionWhenAddingNullId() {

        UUID nullId = null;

        assertThrows(IllegalArgumentException.class, () -> {
            basketService.addProductToBasket(nullId);
        });
    }

    @Test
    void shouldVerifyCorrectMethodCalls() {

        UUID productId = UUID.randomUUID();
        Product product = createMockProduct(productId, "Test Product", 100);
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));

        basketService.addProductToBasket(productId);

        verify(storageService, times(1)).getProductById(productId);
        verify(productBasket, times(1)).addProduct(productId);
    }

    private Product createMockProduct(UUID id, String name, int price) {
        Product product = mock(Product.class);
        lenient().when(product.getId()).thenReturn(id);
        lenient().when(product.getName()).thenReturn(name);
        lenient().when(product.getPrice()).thenReturn(price);
        return product;
    }
}