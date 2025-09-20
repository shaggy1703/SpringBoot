package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Stream;

@Service
public class StorageService {
    private final Map<UUID, Product> products;
    private final Map<UUID, Article> articles;

    public StorageService() {
        this.products = new HashMap<>();
        this.articles = new HashMap<>();
        initializeTestData();
    }

    private void initializeTestData() {
        Product apple = new Product(UUID.randomUUID(), "Яблоко") {
            @Override
            public int getPrice() {
                return 50;
            }
        };
        products.put(apple.getId(), apple);

        Product banana = new Product(UUID.randomUUID(), "Банан") {
            @Override
            public int getPrice() {
                return 70;
            }
        };
        products.put(banana.getId(), banana);

        Product orange = new Product(UUID.randomUUID(), "Апельсин") {
            @Override
            public int getPrice() {
                return 80;
            }
        };
        products.put(orange.getId(), orange);

        Product milk = new Product(UUID.randomUUID(), "Молоко") {
            @Override
            public int getPrice() {
                return 90;
            }
        };
        products.put(milk.getId(), milk);

        Product bread = new Product(UUID.randomUUID(), "Хлеб") {
            @Override
            public int getPrice() {
                return 40;
            }
        };
        products.put(bread.getId(), bread);

        Article article1 = new Article(
                UUID.randomUUID(),
                "Как выбрать фрукты",
                "Выбирайте фрукты с ярким цветом и умеренной мягкостью."
        );
        articles.put(article1.getId(), article1);

        Article article2 = new Article(
                UUID.randomUUID(),
                "Рецепт шоколадного торта",
                "Вам понадобится какао, мука и сахар."
        );
        articles.put(article2.getId(), article2);

        Article article3 = new Article(
                UUID.randomUUID(),
                "Польза молока",
                "Молоко богато кальцием."
        );
        articles.put(article3.getId(), article3);
    }

    public Collection<Product> getAllProducts() {
        return products.values();
    }

    public Collection<Article> getAllArticles() {
        return articles.values();
    }

    public Collection<Searchable> getAllSearchable() {
        return Stream.concat(
                products.values().stream(),
                articles.values().stream()
        ).toList();
    }

    public Product getProductById(UUID id) {
        return products.get(id);
    }

    public Article getArticleById(UUID id) {
        return articles.get(id);
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public void addArticle(Article article) {
        articles.put(article.getId(), article);
    }
}