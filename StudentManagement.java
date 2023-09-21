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
        showOptions();
        List<Student> studentList = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager("jdbc:mysql://localhost:3306/student_management_db", "root", "AngryCuddle5");

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            
            switch (choice) {
                case 1:
                    Student newStudent = new Student();
                    System.out.print("Enter student name: ");
                    newStudent.setName(scanner.next());
                    System.out.print("Enter student age: ");
                    newStudent.setAge(scanner.nextInt());
                    System.out.print("Enter student id: ");
                    newStudent.setId(scanner.nextInt());
                    databaseManager.addStudent(newStudent);
                    showOptions();
                    break;

                case 2:
                    System.out.print("Enter student name to delete: ");
                    String deleteName = scanner.next();
                    databaseManager.deleteStudent(databaseManager.connection, deleteName);
                    showOptions();
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
                    databaseManager.updateStudent(databaseManager.connection, oldName, newName, newAge, newId);
                    showOptions();
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
                            databaseManager.searchStudentByName(databaseManager.connection, searchName);
                            showOptions();
                            break;

                        case 2:
                            System.out.print("Enter student ID to search: ");
                            int searchID = scanner.nextInt();
                            databaseManager.searchStudentByID(databaseManager.connection, searchID);
                            showOptions();
                            break;

                        default:
                            System.out.println("Invalid search option. Please try again.");
                            break;
                    }
                    break;

                case 5:
                    studentList = databaseManager.getAllStudents(databaseManager.connection);
                    printStudents(studentList);
                    showOptions();
                    break;

                case 6:
                    databaseManager.closeConnection();
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
     * Shows the menu options.
     *
     */
    private static void showOptions() {
        System.out.println("Press the option number to perform the action");
        System.out.println("1. Add student");
        System.out.println("2. Delete student");
        System.out.println("3. Update student");
        System.out.println("4. Search student");
        System.out.println("5. Print all students");
        System.out.println("6. Exit");
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
