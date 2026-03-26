package service;

import exception.BookNotAvailableException;
import exception.BorrowLimitExceededException;
import exception.InvalidReturnException;
import model.Book;
import model.LibraryUser;
import model.Transaction;
import util.StorageUtil;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class LibraryService {
    private Map<String, Book> books;
    private List<LibraryUser> members;
    private Queue<String> reservations; // Queue of member IDs waiting for a book
    private List<Transaction> transactions;
    
    // File paths for persistence
    private final String DATA_DIR = "data";
    private final String BOOKS_FILE = DATA_DIR + "/books.dat";
    private final String MEMBERS_FILE = DATA_DIR + "/members.dat";
    private final String TRANSACTIONS_FILE = DATA_DIR + "/transactions.dat";

    @SuppressWarnings("unchecked")
    public LibraryService() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        books = (Map<String, Book>) StorageUtil.loadObject(BOOKS_FILE);
        if (books == null) books = new HashMap<>();
        
        members = (List<LibraryUser>) StorageUtil.loadObject(MEMBERS_FILE);
        if (members == null) members = new ArrayList<>();
        
        transactions = (List<Transaction>) StorageUtil.loadObject(TRANSACTIONS_FILE);
        if (transactions == null) transactions = new ArrayList<>();
        
        reservations = new LinkedList<>(); // Reservations are session-based or could be persisted
    }

    public void saveData() {
        StorageUtil.saveObject(books, BOOKS_FILE);
        StorageUtil.saveObject(members, MEMBERS_FILE);
        StorageUtil.saveObject(transactions, TRANSACTIONS_FILE);
    }

    public void addBook(Book book) {
        books.put(book.getBookId(), book);
        saveData();
    }

    public void registerMember(LibraryUser user) {
        members.add(user);
        saveData();
    }

    public Book getBook(String bookId) {
        return books.get(bookId);
    }

    public LibraryUser getMember(String memberId) {
        return members.stream()
                .filter(m -> m.getMemberId().equals(memberId))
                .findFirst()
                .orElse(null);
    }

    public void issueBook(String bookId, String memberId) throws BookNotAvailableException, BorrowLimitExceededException {
        Book book = getBook(bookId);
        LibraryUser member = getMember(memberId);

        if (book == null || !book.isAvailable()) {
            throw new BookNotAvailableException("Book is either not found or already issued.");
        }
        if (member == null) {
            throw new IllegalArgumentException("Member not found.");
        }
        if (member.getBorrowedBooks().size() >= member.getMaxBooks()) {
            throw new BorrowLimitExceededException("Borrow limit exceeded for member: " + member.getName());
        }

        book.setAvailable(false);
        member.borrowBook(bookId);
        
        Transaction tx = new Transaction(UUID.randomUUID().toString(), bookId, memberId, LocalDate.now());
        transactions.add(tx);
        
        saveData();
    }

    public double returnBook(String bookId, String memberId) throws InvalidReturnException {
        Book book = getBook(bookId);
        LibraryUser member = getMember(memberId);

        if (book == null || member == null || !member.getBorrowedBooks().contains(bookId)) {
            throw new InvalidReturnException("Invalid return request. Book was not issued to this member.");
        }

        book.setAvailable(true);
        member.returnBook(bookId);

        // Find open transaction
        Transaction openTx = null;
        for (Transaction tx : transactions) {
            if (tx.getBookId().equals(bookId) && tx.getMemberId().equals(memberId) && tx.getReturnDate() == null) {
                openTx = tx;
                break;
            }
        }

        double fineAmount = 0.0;
        if (openTx != null) {
            openTx.setReturnDate(LocalDate.now());
            long daysBorrowed = ChronoUnit.DAYS.between(openTx.getIssueDate(), LocalDate.now());
            int daysLate = (int) daysBorrowed - member.getBorrowDurationDays();
            fineAmount = member.calculateFine(daysLate);
            openTx.setFineAmount(fineAmount);
        }

        saveData();
        return fineAmount;
    }

    public void reserveBook(String memberId) {
        LibraryUser member = getMember(memberId);
        if (member != null) {
            reservations.offer(memberId);
        }
    }
    
    public Queue<String> getReservations() {
        return reservations;
    }
    
    public Collection<Book> getAllBooks() {
        return books.values();
    }
    
    public List<LibraryUser> getAllMembers() {
        return members;
    }
    
    public List<Transaction> getTransactionHistory() {
        return transactions;
    }
}
