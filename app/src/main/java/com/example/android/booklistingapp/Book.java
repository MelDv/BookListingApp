package com.example.android.booklistingapp;

public class Book {
    private String title;
    private String authors;
    private String readerLink;

    public Book(String title, String authors, String readerLink) {
        this.title = title;
        this.authors = authors;
        this.readerLink = readerLink;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getReaderLink() {
        return readerLink;
    }
}
