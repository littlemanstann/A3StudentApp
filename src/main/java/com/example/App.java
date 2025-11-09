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
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
            conn.close();
        // The catch block handles any SQL exceptions that may occur during the connection or query execution    
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewStudents(Connection conn) {
        String query = "SELECT * FROM students";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("ID | Name | Age");
            while (rs.next()) {
                System.out.printf("%d | %s | %d%n", rs.getInt("id"), rs.getString("name"), rs.getInt("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();

    private static void addStudent(Connection conn, Scanner scanner) {
        String insertSQL = "INSERT INTO employees (id, name, age) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, 1);
            pstmt.setString(2, "Jane");
            pstmt.setInt(3, 28);
            pstmt.executeUpdate();
            System.out.println("Data inserted using PreparedStatement.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}