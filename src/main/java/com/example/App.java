package com.example;

// Import necessary JDBC classes
import java.sql.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        /// Information for connecting to the PostgreSQL database (Formatting taken from Lecture 12 slides)
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
                System.out.println("3. Update student email");
                System.out.println("4. Delete a student");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consumes newline

                // Handle user choices
                switch (choice) {
                    case 1:
                        getAllStudents(conn);
                        break;
                    case 2:
                        addStudent(conn, scanner);
                        break;
                    case 3:
                        updateStudentEmail(conn, scanner);
                        break;
                    case 4:
                        deleteStudent(conn, scanner);
                        break;
                    case 5:
                        System.out.println("Goodbye!");
                        scanner.close();
                        conn.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        
        // The catch block handles any SQL exceptions that may occur during the connection or query execution    
        } catch (SQLException e) { // Removed ClassNotFoundException from the lecture slides as it's not needed for modern JDBC
            e.printStackTrace();
        }
    }

    /// Get All Students Function
    private static void getAllStudents(Connection conn) {
        // Start a simple query to retrieve all students
        String query = "SELECT * FROM students";

        // Use try to ensure SQL resources are closed properly
        try (Statement stmt = conn.createStatement();

            // Take the results of the query and store them in a ResultSet
            ResultSet rs = stmt.executeQuery(query)) {
                // Print the results in a formatted manner
                System.out.println("student_id | first_name | last_name | email                      | enrollment_date");
                while (rs.next()) {
                    System.out.printf("%-10d | %-10s | %-9s | %-26s | %-15s%n",
                            rs.getInt("student_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getDate("enrollment_date").toString());
            }

        // Catch any SQL exceptions that may occur during the retrieval
        } catch (SQLException e) {
            System.out.println("ERROR: Unable to retrieve students.");
            e.printStackTrace();
        }
    }

    /// Add Student Function
    private static void addStudent(Connection conn, Scanner scanner) {
        // Prepare SQL statement for inserting a new student
        String insertSQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";

        // Use try to ensure SQL query is formatted properly
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            System.out.print("Enter student first name: ");
            pstmt.setString(1, scanner.nextLine());

            System.out.print("Enter last name: ");
            pstmt.setString(2, scanner.nextLine());

            System.out.print("Enter student email: ");
            pstmt.setString(3, scanner.nextLine());

            System.out.print("Enter enrollment date (YYYY-MM-DD): ");
            pstmt.setDate(4, Date.valueOf(scanner.nextLine())); // NOTE: Date.valueOf expects format YYYY-MM-DD

            pstmt.executeUpdate();
            System.out.println("Student added successfully.");
        
        // Catch any SQL exceptions that may occur during the insertion
        } catch (SQLException e) {
            System.out.println("ERROR: Unable to add student.");
            e.printStackTrace();
        }
    }

    /// Update Student Function
    private static void updateStudentEmail(Connection conn, Scanner scanner) {
        // Prepare SQL statement for updating an existing student
        String updateSQL = "UPDATE students SET email = ? WHERE student_id = ?";

        // Use try to ensure SQL query is formatted properly
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            System.out.print("Enter student ID to update: ");
            int studentId = scanner.nextInt();
            scanner.nextLine(); // Consumes newline

            System.out.print("Enter new student email: ");
            pstmt.setString(1, scanner.nextLine());

            // Set the student ID parameter for the WHERE clause
            pstmt.setInt(2, studentId);
            
            // Execute the update and check if any tuples are affected; output success or error message accordingly
            int tupleAffected = pstmt.executeUpdate();
            if (tupleAffected > 0) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("ERROR: No student found with the given ID.");
            }

        // Catch any SQL exceptions that may occur during the update
        } catch (SQLException e) {
            System.out.println("ERROR: Unable to update student.");
            e.printStackTrace();
        }
    }

    /// Delete Student Function
    private static void deleteStudent(Connection conn, Scanner scanner) {
        // Prepare SQL statement for deleting a student
        String deleteSQL = "DELETE FROM students WHERE student_id = ?";

        // Use try to ensure SQL query is formatted properly
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            System.out.print("Enter student ID to delete: ");
            pstmt.setInt(1, scanner.nextInt());
            scanner.nextLine(); // Consumes newline

            // Execute the deletion and check if any tuples are affected; output success or error message accordingly
            int tupleAffected = pstmt.executeUpdate();
            if (tupleAffected > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("No student found with the given ID.");
            }

        // Catch any SQL exceptions that may occur during the deletion
        } catch (SQLException e) {
            System.out.println("ERROR: Unable to delete student.");
            e.printStackTrace();
        }
    }
}