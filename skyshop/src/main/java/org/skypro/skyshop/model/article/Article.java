package org.skypro.skyshop.model.article;

import org.skypro.skyshop.model.search.Searchable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import java.util.UUID;

public class Article implements Searchable {
    private final UUID id;
    private final String title;
    private final String text;

    public Article(UUID id, String title, String text) {
        if (id == null) {
            throw new IllegalArgumentException("ID не может быть null");
        }
        this.id = id;
        this.title = title;
        this.text = text;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return title + "\n" + text;
    }

    @Override
    @JsonIgnore
    public String getSearchTerm() {
        return toString(); // поиск по заголовку + тексту
    }

    @Override
    @JsonIgnore
    public String getType() {
        return "ARTICLE";
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String getStringRepresentation() {
        return Searchable.super.getStringRepresentation();
    }
}