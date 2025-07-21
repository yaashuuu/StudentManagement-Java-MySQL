package yash;

import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("✅ Connection successful!");

            while (true) {
                System.out.println("\n===== Student Management System =====");
                System.out.println("1. Add Student");
                System.out.println("2. View Enrollments");
                System.out.println("3. View All Students");
                System.out.println("4. Search Student");
                System.out.println("5. Exit");
                System.out.print("Choose option: ");
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        StudentOperations.addStudent();
                        break;
                    case 2:
                        StudentOperations.viewEnrollments();
                        break;
                    case 3:
                        StudentOperations.viewAllStudents();
                        break;
                    case 4:
                        StudentOperations.searchStudent();
                        break;
                    case 5:
                        System.out.println("👋 Exiting...");
                        System.exit(0);
                    default:
                        System.out.println("❌ Invalid choice. Try again.");
                }
            }

        } else {
            System.out.println("❌ Connection failed.");
        }
    }
}
