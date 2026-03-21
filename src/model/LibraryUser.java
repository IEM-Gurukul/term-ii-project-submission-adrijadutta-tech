package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class LibraryUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private String memberId;
    private String name;
    private List<String> borrowedBooks; // Storing book IDs

    public LibraryUser(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public List<String> getBorrowedBooks() { return borrowedBooks; }

    public void borrowBook(String bookId) {
        this.borrowedBooks.add(bookId);
    }

    public void returnBook(String bookId) {
        this.borrowedBooks.remove(bookId);
    }

    // Abstract methods to define borrowing rules
    public abstract int getMaxBooks();
    public abstract int getBorrowDurationDays();
    public abstract double calculateFine(int daysLate);

    @Override
    public String toString() {
        return "Member{" +
                "id='" + memberId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + this.getClass().getSimpleName() + '\'' +
                ", borrowed=" + borrowedBooks.size() +
                '}';
    }
}
