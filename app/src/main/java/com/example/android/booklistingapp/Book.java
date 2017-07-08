package com.example.android.booklistingapp;

public class Book {
    private int totalItems;
    private String title;
    private String subtitle;
    private String authors;
    private String selfLink;

    public Book(int totalItems, String title, String subtitle, String authors, String selfLink) {
        this.totalItems = totalItems;
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.selfLink = selfLink;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getAuthors() {
        return authors;
    }

    public String getSelfLink() {
        return selfLink;
    }
}
