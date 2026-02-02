/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ers.group;

import java.util.Scanner;
public class ERSGroup {

    /**
     * @param args the command line arguments
     */

    public static void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n=== ERS Group Main Menu ===");
            System.out.println("1. Load Course Subjects");
            System.out.println("2. Load Teachers");
            System.out.println("3. Load Rooms");
            System.out.println("4. Load All Data");
            System.out.println("5. Load Students");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    loadAndDisplayCourses();
                    break;
                case 2:
                    loadAndDisplayTeachers();
                    break;
                case 3:
                    loadAndDisplayRooms();
                    break;
                case 4:
                    loadAndDisplayAll();
                    break;
                case 5:
                    loadAndDisplayStudents();
                    break;
                case 6:
                    System.out.println("Exiting ERS System...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
    
    public static void loadAndDisplayCourses() {
        CourseSubjectFileLoader courseLoader = new CourseSubjectFileLoader();
        courseLoader.load("ERS-group/src/ers/group/master files/courseSubject.txt");
        System.out.println("\n--- Course Subjects ---");
        for (CourseSubject s : courseLoader.getAllSubjects()) {
            System.out.println("Subject: " + s.getCourseSubjectID() + " " + s.getCourseSubjectName() + 
                "| Units: " + s.getUnits() + "\nPrerequisites: " + s.getPrerequisitesString() + 
                "| SY: " + s.getYearLevel() + " - Sem: " + s.getSemester() + "\n");
        }
    }
    
    public static void loadAndDisplayTeachers() {
        CourseSubjectFileLoader courseLoader = new CourseSubjectFileLoader();
        courseLoader.load("ERS-group/src/ers/group/master files/courseSubject.txt");
        
        TeacherFileLoader teacherLoader = new TeacherFileLoader();
        teacherLoader.load("ERS-group/src/ers/group/master files/teachers.txt");
        System.out.println("\n--- Teachers ---");
        for (Teachers t : teacherLoader.getAllTeachers()) {
            System.out.println("Teacher: " + t.getTeacherID() + " " + t.getTeacherName() + 
                "\nSubjects: " + t.getQualifiedSubjectNames(courseLoader.getSubjectMap()) + "\n");
        }
    }
    
    public static void loadAndDisplayRooms() {
        RoomFileLoader roomLoader = new RoomFileLoader();
        roomLoader.load("ERS-group/src/ers/group/master files/rooms.txt");
        System.out.println("\n--- Rooms ---");
        for (Rooms r : roomLoader.getAllRooms()) {
            System.out.println("Room: " + r.getRoomID() + " " + r.getRoomName() + 
                "| Capacity: " + r.getCapacity() + "| Lab: " + r.isLabRoom() + "\n");
        }
    }
    
    public static void loadAndDisplayAll() {
        loadAndDisplayCourses();
        loadAndDisplayTeachers();
        loadAndDisplayRooms();
    }

    public static void loadAndDisplayStudents() {
        StudentFileLoader studentLoader = new StudentFileLoader();
        studentLoader.load("ERS-group/src/ers/group/master files/student.txt");
        System.out.println("\n--- Students ---");
        for (Student s : studentLoader.getAllStudents()) {
            s.displayStudentInfo();
            System.out.println();
        }
    }

    public static void main(String[] args) {
        mainMenu();
    }

}