package yash;

import java.sql.*;
import java.util.Scanner;

public class StudentOperations {

    public static void addStudent() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter student name: ");
        String name = sc.nextLine();

        System.out.print("Enter student email: ");
        String email = sc.nextLine();

        System.out.print("Enter student phone: ");
        String phone = sc.nextLine();

        System.out.print("Enter student department: ");
        String department = sc.nextLine();

        // Show available courses before asking course ID
        try {
            Connection courseConn = DBConnection.getConnection();
            Statement stmt = courseConn.createStatement();
            ResultSet courseRs = stmt.executeQuery("SELECT * FROM courses");

            System.out.println("\n📘 Available Courses:");
            while (courseRs.next()) {
                System.out.println(courseRs.getInt("course_id") + ". " +
                        courseRs.getString("course_name") + " (" +
                        courseRs.getString("duration") + ")");
            }

            courseConn.close();
        } catch (Exception e) {
            System.out.println("⚠️ Could not fetch courses.");
        }

        System.out.print("Enter course ID: ");
        int courseId = sc.nextInt();
        sc.nextLine(); // to consume newline

        System.out.print("Enter enrollment date (yyyy-mm-dd): ");
        String enrollDate = sc.nextLine();

        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            try {
                // Step 1: Insert student
                String sql = "INSERT INTO students (name, email, phone, department) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, phone);
                pstmt.setString(4, department);

                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    System.out.println("✅ Student added successfully.");

                    // Step 2: Get generated student_id
                    ResultSet rs = pstmt.getGeneratedKeys();
                    int studentId = -1;
                    if (rs.next()) {
                        studentId = rs.getInt(1);
                    }

                    // Step 3: Insert into enrollments
                    String enrollSql = "INSERT INTO enrollments (student_id, course_id, enrollment_date) VALUES (?, ?, ?)";
                    PreparedStatement enrollStmt = conn.prepareStatement(enrollSql);
                    enrollStmt.setInt(1, studentId);
                    enrollStmt.setInt(2, courseId);
                    enrollStmt.setString(3, enrollDate);

                    int enrollRows = enrollStmt.executeUpdate();
                    if (enrollRows > 0) {
                        System.out.println("✅ Enrollment added successfully.");
                    } else {
                        System.out.println("❌ Failed to add enrollment.");
                    }

                } else {
                    System.out.println("❌ Failed to add student.");
                }

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("❌ Could not connect to database.");
        }
    }

    public static void viewEnrollments() {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                String sql = "SELECT s.name, s.email, c.course_name, e.enrollment_date " +
                        "FROM enrollments e " +
                        "JOIN students s ON e.student_id = s.student_id " +
                        "JOIN courses c ON e.course_id = c.course_id";

                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                System.out.println("\n===== Enrollments =====");
                while (rs.next()) {
                    System.out.println("👤 Name: " + rs.getString("name") +
                            ", 📧 Email: " + rs.getString("email") +
                            ", 📚 Course: " + rs.getString("course_name") +
                            ", 🗓️ Date: " + rs.getDate("enrollment_date"));
                }

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("❌ Database connection failed.");
        }
    }

    public static void viewAllStudents() {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                String sql = "SELECT * FROM students";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                System.out.println("\n===== All Students =====");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("student_id") +
                            ", Name: " + rs.getString("name") +
                            ", Email: " + rs.getString("email") +
                            ", Phone: " + rs.getString("phone") +
                            ", Department: " + rs.getString("department"));
                }

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("❌ Database connection failed.");
        }
    }

    public static void searchStudent() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter name or email to search: ");
        String keyword = sc.nextLine();

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                String sql = "SELECT * FROM students WHERE name LIKE ? OR email LIKE ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, "%" + keyword + "%");
                stmt.setString(2, "%" + keyword + "%");

                ResultSet rs = stmt.executeQuery();

                System.out.println("\n===== Search Results =====");
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println("ID: " + rs.getInt("student_id") +
                            ", Name: " + rs.getString("name") +
                            ", Email: " + rs.getString("email") +
                            ", Phone: " + rs.getString("phone") +
                            ", Department: " + rs.getString("department"));
                }

                if (!found) {
                    System.out.println("🔍 No student found with that keyword.");
                }

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("❌ Database connection failed.");
        }
    }
}
