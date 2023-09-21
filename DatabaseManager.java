import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    public Connection connection;

    public DatabaseManager(String url, String username, String password) {
        try {
            // Load/register the MySQL JDBC driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }
    }

    public void closeConnection() {
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
    public void addStudent(Student student) {
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
   public void updateStudent(Connection connection, String oldName, String newName, int newAge, int newId) {
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
    public void deleteStudent(Connection connection, String name) {
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
    public List<Student> getAllStudents(Connection connection) {
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
    public void searchStudentByName(Connection connection, String name) {
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
    public void searchStudentByID(Connection connection, int id) {
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


}
