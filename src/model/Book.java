package model;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String bookId;
    private String title;
    private String author;
    private boolean isAvailable;

    public Book(String bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + bookId + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", available=" + isAvailable +
                '}';
    }
}
