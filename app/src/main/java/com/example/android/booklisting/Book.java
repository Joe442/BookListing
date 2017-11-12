package com.example.android.booklisting;

/**
 * Created by Johannes on 05.11.2017.
 * This class represents a Book
 */

public class Book {
    private String title;
    private String author;
    private String description;
    private String imageLink;

    public Book(String title, String author, String description, String imageLink) {
        this.title = title;
        this.author = author;
        this.description  = description;
        this.imageLink = imageLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
