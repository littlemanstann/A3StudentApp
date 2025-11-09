package com.example;

// Import necessary JDBC classes
import java.sql.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // Information for connecting to the PostgreSQL database (Formatting taken from Lecture 12 slides)
        // Currently contains my own Database URL, username and password
        String url = "jdbc:postgresql://localhost:5432/Assignment3";
        String user = "postgres";
        String password = "$Qli73%r";

        // Establish connection and execute a simple query
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Initialize a scanner for user input
            Scanner scanner = new Scanner(System.in);

            // Check if connection is successful
            if (conn != null) {
                System.out.println("Connected to PostgreSQL successfully!");
            } else {
                System.out.println("Failed to establish connection.");
            }

            // Main menu loop for user interaction
            while (true) {
                System.out.println("\n==== Student Menu ====");
                System.out.println("1. View all students");
                System.out.println("2. Add a new student");
                System.out.println("3. Delete a student");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        viewStudents(conn);
                        break;
                    case 2:
                        addStudent(conn, scanner);
                        break;
                    case 3:
                        deleteStudent(conn, scanner);
                        break;
                    case 4:
                        System.out.println("Goodbye!");
                        scanner.close();
                        conn.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        
        // The catch block handles any SQL exceptions that may occur during the connection or query execution    
        } catch (SQLException e) { // Removed ClassNotFoundException as it's not needed for modern JDBC
            e.printStackTrace();
        }
    }

    private static void viewStudents(Connection conn) {
        String query = "SELECT * FROM students";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("student_id | first_name | last_name | enrollment_date");
            while (rs.next()) {
                System.out.printf("%10d | %10s | %9s | %15s%n",
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("enrollment_date").toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addStudent(Connection conn, Scanner scanner) {
        String insertSQL = "INSERT INTO students (student_id, first_name, last_name, enrollment_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            System.out.print("Enter student ID: ");
            pstmt.setInt(1, scanner.nextInt());
            scanner.nextLine(); // consume newline

            System.out.print("Enter first name: ");
            pstmt.setString(2, scanner.nextLine());

            System.out.print("Enter last name: ");
            pstmt.setString(3, scanner.nextLine());

            System.out.print("Enter enrollment date (YYYY-MM-DD): ");
            pstmt.setDate(4, Date.valueOf(scanner.nextLine()));

            pstmt.executeUpdate();
            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteStudent(Connection conn, Scanner scanner) {
        String deleteSQL = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            System.out.print("Enter student ID to delete: ");
            pstmt.setInt(1, scanner.nextInt());
            scanner.nextLine(); // consume newline

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("No student found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}