package model;

public class FacultyMember extends LibraryUser {
    private static final long serialVersionUID = 1L;

    private static final int MAX_BOOKS = 10;
    private static final int BORROW_DURATION_DAYS = 30;
    private static final double FINE_PER_DAY = 0.5;

    public FacultyMember(String memberId, String name) {
        super(memberId, name);
    }

    @Override
    public int getMaxBooks() {
        return MAX_BOOKS;
    }

    @Override
    public int getBorrowDurationDays() {
        return BORROW_DURATION_DAYS;
    }

    @Override
    public double calculateFine(int daysLate) {
        if (daysLate <= 0) return 0.0;
        return daysLate * FINE_PER_DAY;
    }
}
