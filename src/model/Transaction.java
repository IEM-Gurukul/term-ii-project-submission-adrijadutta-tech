package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String transactionId;
    private String bookId;
    private String memberId;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private double fineAmount;

    public Transaction(String transactionId, String bookId, String memberId, LocalDate issueDate) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.fineAmount = 0.0;
    }

    public String getTransactionId() { return transactionId; }
    public String getBookId() { return bookId; }
    public String getMemberId() { return memberId; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public double getFineAmount() { return fineAmount; }

    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + transactionId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", issueDate=" + issueDate +
                (returnDate != null ? ", returnDate=" + returnDate : "") +
                (fineAmount > 0 ? ", fine=" + fineAmount : "") +
                '}';
    }
}
