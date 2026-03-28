import exception.LibraryException;
import model.Book;
import model.FacultyMember;
import model.LibraryUser;
import model.StudentMember;
import service.LibraryService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LibraryService service = new LibraryService();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to Modular Library Management System!");
        
        // Initialize some dummy data if empty
        if (service.getAllBooks().isEmpty() && service.getAllMembers().isEmpty()) {
            service.addBook(new Book("B001", "Design Patterns", "GoF"));
            service.addBook(new Book("B002", "Clean Code", "Robert C. Martin"));
            service.registerMember(new StudentMember("S001", "Alice"));
            service.registerMember(new FacultyMember("F001", "Dr. Bob"));
            System.out.println("Initialized dummy data.");
        }

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. List Books");
            System.out.println("2. List Members");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine();
            
            try {
                switch (choice) {
                    case "1":
                        service.getAllBooks().forEach(System.out::println);
                        break;
                    case "2":
                        service.getAllMembers().forEach(System.out::println);
                        break;
                    case "3":
                        System.out.print("Enter Book ID: ");
                        String bookId = scanner.nextLine();
                        System.out.print("Enter Member ID: ");
                        String memberId = scanner.nextLine();
                        service.issueBook(bookId, memberId);
                        System.out.println("Book issued successfully.");
                        break;
                    case "4":
                        System.out.print("Enter Book ID: ");
                        String retBookId = scanner.nextLine();
                        System.out.print("Enter Member ID: ");
                        String retMemberId = scanner.nextLine();
                        double fine = service.returnBook(retBookId, retMemberId);
                        System.out.println("Book returned successfully.");
                        if (fine > 0) {
                            System.out.println("Fine calculated: $" + fine);
                        }
                        break;
                    case "5":
                        System.out.println("Goodbye!");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (LibraryException e) {
                System.err.println("Library Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected Error: " + e.getMessage());
            }
        }
    }
}
