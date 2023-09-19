import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents a Student Management System with various operations
 * such as adding, deleting, updating, searching, and printing students.
 */
public class StudentManagement {

    /**
     * The main method of the Student Management System.
     *
     * @param args The command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Student Management System");
        System.out.println("Press the option number to perform the action");
        System.out.println("1. Add student");
        System.out.println("2. Delete student");
        System.out.println("3. Update student");
        System.out.println("4. Search student");
        System.out.println("5. Print all students");
        System.out.println("6. Exit");

        List<Student> studentList = new ArrayList<>();
        Connection connection = createDatabaseConnection();

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    Student newStudent = new Student();
                    System.out.print("Enter student name: ");
                    newStudent.setName(scanner.next());
                    System.out.print("Enter student age: ");
                    newStudent.setAge(scanner.nextInt());
                    System.out.print("Enter student id: ");
                    newStudent.setId(scanner.nextInt());
                    addStudent(connection, newStudent);
                    break;

                case 2:
                    System.out.print("Enter student name to delete: ");
                    String deleteName = scanner.next();
                    deleteStudent(connection, deleteName);
                    break;

                case 3:
                    System.out.print("Enter student name to update: ");
                    String oldName = scanner.next();
                    System.out.print("Enter new name: ");
                    String newName = scanner.next();
                    System.out.print("Enter new age: ");
                    int newAge = scanner.nextInt();
                    System.out.print("Enter new id: ");
                    int newId = scanner.nextInt();
                    updateStudent(connection, oldName, newName, newAge, newId);
                    break;

                case 4:
                    System.out.println("Search by:");
                    System.out.println("1. Name");
                    System.out.println("2. ID");
                    int searchOption = scanner.nextInt();

                    switch (searchOption) {
                        case 1:
                            System.out.print("Enter student name to search: ");
                            String searchName = scanner.next();
                            searchStudentByName(connection, searchName);
                            break;

                        case 2:
                            System.out.print("Enter student ID to search: ");
                            int searchID = scanner.nextInt();
                            searchStudentByID(connection, searchID);
                            break;

                        default:
                            System.out.println("Invalid search option. Please try again.");
                            break;
                    }
                    break;

                case 5:
                    studentList = getAllStudents(connection);
                    printStudents(studentList);
                    break;

                case 6:
                    closeDatabaseConnection(connection);
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 6);

        scanner.close();
    }

    /**
     * Creates a database connection to the MySQL database.
     *
     * @return A Connection object representing the database connection.
     * @throws RuntimeException If the MySQL JDBC driver is not found or
     *                          if there is a failure to connect to the database.
     */
    private static Connection createDatabaseConnection() {
        try {
            // Load/register the MySQL JDBC driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
    
            String url = "jdbc:mysql://localhost:3306/student_management_db";
            String username = "root";
            String password = "AngryCuddle5";
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL JDBC driver not found.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }
    }
    
    /**
     * Closes the database connection.
     *
     * @param connection The Connection object to be closed.
     */
    private static void closeDatabaseConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Adds a new student to the database.
     *
     * @param connection The database connection.
     * @param student    The Student object to be added to the database.
     */

    // We use PreparedStatement to safely handle SQL queries and parameterize them to prevent SQL injection.
    private static void addStudent(Connection connection, Student student) {
        String insertQuery = "INSERT INTO students (name, age, id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) { 
            preparedStatement.setString(1, student.getName());
            preparedStatement.setInt(2, student.getAge());
            preparedStatement.setInt(3, student.getId());
            preparedStatement.executeUpdate();
            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding student: " + e.getMessage());
        }
    }

    /**
     * Updates an existing student's information in the database.
     *
     * @param connection The database connection.
     * @param oldName    The old name of the student to be updated.
     * @param newName    The new name for the student.
     * @param newAge     The new age for the student.
     * @param newId      The new ID for the student.
     */
    private static void updateStudent(Connection connection, String oldName, String newName, int newAge, int newId) {
        String updateQuery = "UPDATE students SET name=?, age=?, id=? WHERE name=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, newAge);
            preparedStatement.setInt(3, newId);
            preparedStatement.setString(4, oldName);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating student: " + e.getMessage());
        }
    }

    /**
     * Deletes a student from the database by their name.
     *
     * @param connection The database connection.
     * @param name       The name of the student to be deleted.
     */
    private static void deleteStudent(Connection connection, String name) {
        String deleteQuery = "DELETE FROM students WHERE name=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, name);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting student: " + e.getMessage());
        }
    }

    /**
     * Retrieves a list of all students from the database.
     *
     * @param connection The database connection.
     * @return A list of Student objects representing all students in the database.
     */
    private static List<Student> getAllStudents(Connection connection) {
        List<Student> studentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM students";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Student student = new Student();
                student.setName(resultSet.getString("name"));
                student.setAge(resultSet.getInt("age"));
                student.setId(resultSet.getInt("id"));
                studentList.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching students: " + e.getMessage());
        }
        return studentList;
    }

    /**
     * Searches for a student in the database by their name.
     *
     * @param connection The database connection.
     * @param name       The name of the student to search for.
     */
    private static void searchStudentByName(Connection connection, String name) {
        String selectQuery = "SELECT * FROM students WHERE name=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Student found: " + resultSet.getString("name") + " // Age: " + resultSet.getInt("age") + " // ID: " + resultSet.getInt("id"));
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error searching student by name: " + e.getMessage());
        }
    }

    /**
     * Searches for a student in the database by their ID.
     *
     * @param connection The database connection.
     * @param id         The ID of the student to search for.
     */
    private static void searchStudentByID(Connection connection, int id) {
        String selectQuery = "SELECT * FROM students WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Student found: " + resultSet.getString("name") + " // Age: " + resultSet.getInt("age") + " // ID: " + resultSet.getInt("id"));
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error searching student by ID: " + e.getMessage());
        }
    }

    /**
     * Prints the details of a list of students.
     *
     * @param studentList A list of Student objects to be printed.
     */
    private static void printStudents(List<Student> studentList) {
        for (Student student : studentList) {
            System.out.println("Name: " + student.getName() + " // Age: " + student.getAge() + " // ID: " + student.getId());
        }
    }
}
