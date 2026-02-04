package ers.group;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ERSGroup {

    private static final Scanner scanner = new Scanner(System.in);
    
    // File Loaders
    private final static StudentFileLoader studentLoader = new StudentFileLoader();
    private final static CourseSubjectFileLoader courseLoader = new CourseSubjectFileLoader();
    private final static TeacherFileLoader teacherLoader = new TeacherFileLoader();
    private final static RoomFileLoader roomLoader = new RoomFileLoader();
    private final static ScheduleFileLoader scheduleLoader = new ScheduleFileLoader();
    private final static EnrollmentFileLoader enrollmentLoader = new EnrollmentFileLoader();
    
    // File Savers
    private final static StudentFileSaver studentSaver = new StudentFileSaver();
    private final static ScheduleFileSaver scheduleSaver = new ScheduleFileSaver();
    private final static EnrollmentFileSaver enrollmentSaver = new EnrollmentFileSaver();
    
    // File paths
    private static final String STUDENT_FILE = "ERS-group/src/ers/group/master files/student.txt";
    private static final String COURSE_FILE = "ERS-group/src/ers/group/master files/courseSubject.txt";
    private static final String TEACHER_FILE = "ERS-group/src/ers/group/master files/teachers.txt";
    private static final String ROOM_FILE = "ERS-group/src/ers/group/master files/rooms.txt";
    private static final String SCHEDULE_FILE = "ERS-group/src/ers/group/master files/schedule.txt";
    private static final String ENROLLMENT_FILE = "ERS-group/src/ers/group/master files/studentcourse.txt";

    public static void main(String[] args) {
        loadAllData();
        mainMenu();
    }

    public static void loadAllData() {
        System.out.println("Loading all data from master files...");
        studentLoader.load(STUDENT_FILE);
        courseLoader.load(COURSE_FILE);
        teacherLoader.load(TEACHER_FILE);
        roomLoader.load(ROOM_FILE);
        scheduleLoader.load(SCHEDULE_FILE);
        enrollmentLoader.load(ENROLLMENT_FILE);
        System.out.println("Data loaded successfully!\n");
    }

    public static void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║   ENROLLMENT & RECORDS SYSTEM (ERS)    ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Student Management");
            System.out.println("2. Course Management");
            System.out.println("3. Schedule Management");
            System.out.println("4. Enrollment Management");
            System.out.println("5. View All Data");
            System.out.println("6. Reload Data from Files");
            System.out.println("7. Exit");
            System.out.print("\nEnter your choice: ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1: studentMenu(); break;
                case 2: courseMenu(); break;
                case 3: scheduleMenu(); break;
                case 4: enrollmentMenu(); break;
                case 5: viewAllData(); break;
                case 6: loadAllData(); break;
                case 7:
                    System.out.println("Exiting ERS System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    // ============ STUDENT MANAGEMENT ============
    public static void studentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- STUDENT MANAGEMENT ---");
            System.out.println("1. View All Students");
            System.out.println("2. Search Student");
            System.out.println("3. Add New Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1: viewAllStudents(); break;
                case 2: searchStudent(); break;
                case 3: addNewStudent(); break;
                case 4: updateStudent(); break;
                case 5: deleteStudent(); break;
                case 6: back = true; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    public static void viewAllStudents() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        ALL STUDENTS                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        
        List<Student> students = new ArrayList<>(studentLoader.getAllStudents());
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        System.out.printf("%-10s %-25s %-5s %-12s %-15s %-10s %-5s%n", 
            "ID", "Name", "Age", "DOB", "Year Level", "Type", "GWA");
        System.out.println("─".repeat(90));
        
        for (Student s : students) {
            System.out.printf("%-10s %-25s %-5d %-12s %-15s %-10s %-5.2f%n",
                s.getStudentID(),
                truncate(s.getStudentName(), 25),
                s.getAge(),
                s.getDateOfBirth(),
                s.getYearLevel(),
                s.getStudentType(),
                s.getGwa()
            );
        }
        System.out.println("\nTotal students: " + students.size());
    }

    public static void searchStudent() {
        System.out.print("\nEnter Student ID or Name: ");
        String search = scanner.nextLine().trim();
        
        List<Student> results = new ArrayList<>();
        for (Student s : studentLoader.getAllStudents()) {
            if (s.getStudentID().equalsIgnoreCase(search) || 
                s.getStudentName().toLowerCase().contains(search.toLowerCase())) {
                results.add(s);
            }
        }
        
        if (results.isEmpty()) {
            System.out.println("No students found matching: " + search);
        } else {
            System.out.println("\n--- Search Results ---");
            for (Student s : results) {
                s.displayStudentInfo();
                System.out.println();
            }
        }
    }

    public static void addNewStudent() {
        System.out.println("\n--- ADD NEW STUDENT ---");
        
        System.out.print("Student ID: ");
        String id = scanner.nextLine().trim();
        
        // Check if ID already exists
        for (Student s : studentLoader.getAllStudents()) {
            if (s.getStudentID().equalsIgnoreCase(id)) {
                System.out.println("Error: Student ID already exists!");
                return;
            }
        }
        
        System.out.print("Student Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Date of Birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine().trim();
        int age = calculateAge(dob);
        
        System.out.print("Gender (Male/Female): ");
        String gender = scanner.nextLine().trim();
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine().trim();
        
        System.out.print("Father's Name: ");
        String fatherName = scanner.nextLine().trim();
        
        System.out.print("Mother's Name: ");
        String motherName = scanner.nextLine().trim();
        
        System.out.print("Guardian's Phone Number: ");
        String guardianPhone = scanner.nextLine().trim();
        
        System.out.print("Address: ");
        String address = scanner.nextLine().trim();
        
        // Create new student with defaults
        Student newStudent = new Student(
            id, name, age, dob, "", "", "", new ArrayList<>(), 0.0,
            email, phone, gender, address, fatherName, motherName, guardianPhone
        );
        
        // Add to loader
        List<Student> students = new ArrayList<>(studentLoader.getAllStudents());
        students.add(newStudent);
        
        // Save to file
        try {
            studentSaver.save(STUDENT_FILE, students);
            System.out.println("✓ Student added successfully!");
            studentLoader.load(STUDENT_FILE); // Reload
        } catch (IOException e) {
            System.out.println("✗ Error saving student: " + e.getMessage());
        }
    }

    public static void updateStudent() {
        System.out.print("\nEnter Student ID to update: ");
        String id = scanner.nextLine().trim();
        
        List<Student> students = new ArrayList<>(studentLoader.getAllStudents());
        Student toUpdate = null;
        
        for (Student s : students) {
            if (s.getStudentID().equalsIgnoreCase(id)) {
                toUpdate = s;
                break;
            }
        }
        
        if (toUpdate == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println("\n--- Current Information ---");
        toUpdate.displayStudentInfo();
        
        System.out.println("\n--- Update Student (press Enter to keep current value) ---");
        
        System.out.print("Student Name [" + toUpdate.getStudentName() + "]: ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) toUpdate.setStudentName(name);
        
        System.out.print("Email [" + toUpdate.getEmail() + "]: ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) toUpdate.setEmail(email);
        
        System.out.print("Phone Number [" + toUpdate.getPhoneNumber() + "]: ");
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty()) toUpdate.setPhoneNumber(phone);
        
        System.out.print("Address [" + toUpdate.getAddress() + "]: ");
        String address = scanner.nextLine().trim();
        if (!address.isEmpty()) toUpdate.setAddress(address);
        
        // Save
        try {
            studentSaver.save(STUDENT_FILE, students);
            System.out.println("✓ Student updated successfully!");
            studentLoader.load(STUDENT_FILE);
        } catch (IOException e) {
            System.out.println("✗ Error updating student: " + e.getMessage());
        }
    }

    public static void deleteStudent() {
        System.out.print("\nEnter Student ID to delete: ");
        String id = scanner.nextLine().trim();
        
        List<Student> students = new ArrayList<>(studentLoader.getAllStudents());
        Student toDelete = null;
        
        for (Student s : students) {
            if (s.getStudentID().equalsIgnoreCase(id)) {
                toDelete = s;
                break;
            }
        }
        
        if (toDelete == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println("\nStudent to delete:");
        toDelete.displayStudentInfo();
        
        System.out.print("\nAre you sure you want to delete this student? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        
        if (confirm.equalsIgnoreCase("yes")) {
            students.remove(toDelete);
            try {
                studentSaver.save(STUDENT_FILE, students);
                System.out.println("✓ Student deleted successfully!");
                studentLoader.load(STUDENT_FILE);
            } catch (IOException e) {
                System.out.println("✗ Error deleting student: " + e.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // ============ COURSE MANAGEMENT ============
    public static void courseMenu() {
        System.out.println("\n--- COURSE MANAGEMENT ---");
        System.out.println("1. View All Courses");
        System.out.println("2. View Course Details");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter your choice: ");
        
        int choice = getIntInput();
        switch (choice) {
            case 1: viewAllCourses(); break;
            case 2: viewCourseDetails(); break;
            case 3: break;
            default: System.out.println("Invalid choice.");
        }
    }

    public static void viewAllCourses() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        ALL COURSES                             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        
        System.out.printf("%-10s %-35s %-8s %-10s %-10s%n", 
            "Code", "Course Name", "Units", "Year", "Semester");
        System.out.println("─".repeat(80));
        
        for (CourseSubject c : courseLoader.getAllSubjects()) {
            System.out.printf("%-10s %-35s %-8d %-10s %-10s%n",
                c.getCourseSubjectID(),
                truncate(c.getCourseSubjectName(), 35),
                c.getUnits(),
                c.getYearLevel(),
                c.getSemester()
            );
        }
    }

    public static void viewCourseDetails() {
        System.out.print("\nEnter Course Code: ");
        String code = scanner.nextLine().trim();
        
        for (CourseSubject c : courseLoader.getAllSubjects()) {
            if (c.getCourseSubjectID().equalsIgnoreCase(code)) {
                System.out.println("\n--- Course Details ---");
                System.out.println("Code: " + c.getCourseSubjectID());
                System.out.println("Name: " + c.getCourseSubjectName());
                System.out.println("Units: " + c.getUnits());
                System.out.println("Year Level: " + c.getYearLevel());
                System.out.println("Semester: " + c.getSemester());
                System.out.println("Prerequisites: " + c.getPrerequisitesString());
                return;
            }
        }
        System.out.println("Course not found!");
    }

    // ============ SCHEDULE MANAGEMENT ============
    public static void scheduleMenu() {
        System.out.println("\n--- SCHEDULE MANAGEMENT ---");
        System.out.println("1. View All Schedules");
        System.out.println("2. View Student Schedule");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter your choice: ");
        
        int choice = getIntInput();
        switch (choice) {
            case 1: viewAllSchedules(); break;
            case 2: viewStudentSchedule(); break;
            case 3: break;
            default: System.out.println("Invalid choice.");
        }
    }

    public static void viewAllSchedules() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        ALL SCHEDULES                           ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        
        System.out.printf("%-12s %-10s %-8s %-10s %-10s %-10s %-20s%n", 
            "Schedule ID", "Course", "Room", "Day", "Start", "End", "Teacher");
        System.out.println("─".repeat(90));
        
        for (Schedule s : scheduleLoader.getAllSchedules()) {
            System.out.printf("%-12s %-10s %-8s %-10s %-10s %-10s %-20s%n",
                s.getScheduleID(),
                s.getCourseID(),
                s.getRoom(),
                s.getDay(),
                s.getStartTime(),
                s.getEndTime(),
                truncate(s.getTeacherName(), 20)
            );
        }
    }

    public static void viewStudentSchedule() {
        System.out.print("\nEnter Student ID: ");
        String studentID = scanner.nextLine().trim();
        
        // Find student
        Student student = null;
        for (Student s : studentLoader.getAllStudents()) {
            if (s.getStudentID().equalsIgnoreCase(studentID)) {
                student = s;
                break;
            }
        }
        
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println("\n--- Schedule for " + student.getStudentName() + " ---");
        System.out.println("GWA: " + student.getGwa());
        System.out.println("\nEnrolled Courses:");
        
        List<String> courses = student.getSubjectsEnrolled();
        if (courses.isEmpty()) {
            System.out.println("No courses enrolled.");
            return;
        }
        
        System.out.printf("%-10s %-10s %-8s %-10s - %-10s %-20s%n", 
            "Course", "Day", "Room", "Start", "End", "Teacher");
        System.out.println("─".repeat(75));
        
        for (String courseID : courses) {
            for (Schedule sch : scheduleLoader.getAllSchedules()) {
                if (sch.getCourseID().equalsIgnoreCase(courseID)) {
                    System.out.printf("%-10s %-10s %-8s %-10s - %-10s %-20s%n",
                        sch.getCourseID(),
                        sch.getDay(),
                        sch.getRoom(),
                        sch.getStartTime(),
                        sch.getEndTime(),
                        truncate(sch.getTeacherName(), 20)
                    );
                }
            }
        }
    }

    // ============ ENROLLMENT MANAGEMENT ============
    public static void enrollmentMenu() {
        System.out.println("\n--- ENROLLMENT MANAGEMENT ---");
        System.out.println("1. View All Enrollments");
        System.out.println("2. Enroll Student in Course");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter your choice: ");
        
        int choice = getIntInput();
        switch (choice) {
            case 1: viewAllEnrollments(); break;
            case 2: enrollStudent(); break;
            case 3: break;
            default: System.out.println("Invalid choice.");
        }
    }

    public static void viewAllEnrollments() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     ALL ENROLLMENTS                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        
        System.out.printf("%-12s %-12s %-10s%n", "Student ID", "Section ID", "Course ID");
        System.out.println("─".repeat(40));
        
        for (Enrollment e : enrollmentLoader.getAllEnrollments()) {
            System.out.printf("%-12s %-12s %-10s%n",
                e.getStudentID(),
                e.getSectionID(),
                e.getCourseID()
            );
        }
    }

    public static void enrollStudent() {
        System.out.print("\nEnter Student ID: ");
        String studentID = scanner.nextLine().trim();
        
        System.out.print("Enter Section ID: ");
        String sectionID = scanner.nextLine().trim();
        
        System.out.print("Enter Course ID: ");
        String courseID = scanner.nextLine().trim();
        
        Enrollment newEnrollment = new Enrollment(studentID, sectionID, courseID);
        
        List<Enrollment> enrollments = new ArrayList<>(enrollmentLoader.getAllEnrollments());
        enrollments.add(newEnrollment);
        
        try {
            enrollmentSaver.save(ENROLLMENT_FILE, enrollments);
            System.out.println("✓ Student enrolled successfully!");
            enrollmentLoader.load(ENROLLMENT_FILE);
        } catch (IOException e) {
            System.out.println("✗ Error enrolling student: " + e.getMessage());
        }
    }

    // ============ VIEW ALL DATA ============
    public static void viewAllData() {
        viewAllStudents();
        viewAllCourses();
        viewAllSchedules();
        
        System.out.println("\n--- Teachers ---");
        for (Teachers t : teacherLoader.getAllTeachers()) {
            System.out.println("Teacher: " + t.getTeacherID() + " " + t.getTeacherName());
        }
        
        System.out.println("\n--- Rooms ---");
        for (Rooms r : roomLoader.getAllRooms()) {
            System.out.println("Room: " + r.getRoomID() + " " + r.getRoomName() + 
                " | Capacity: " + r.getCapacity() + " | Lab: " + r.isLabRoom());
        }
    }

    // ============ UTILITY METHODS ============
    public static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter a number: ");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return input;
    }

    public static String truncate(String str, int length) {
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }

    public static int calculateAge(String dob) {
        try {
            LocalDate birthDate = LocalDate.parse(dob, DateTimeFormatter.ISO_LOCAL_DATE);
            return Period.between(birthDate, LocalDate.now()).getYears();
        } catch (Exception e) {
            return 0;
        }
    }
}