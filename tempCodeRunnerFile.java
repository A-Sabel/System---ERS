
public class StudentInfo {

    // Fields
    private int studentId;
    private String name;
    private String course;
    private double gwa;

    // ===============================
    // Constructor 1 – Default
    // ===============================
    public StudentInfo() {
        studentId = 0;
        name = "Unknown";
        course = "Not Assigned";
        gwa = 0.0;
    }

    // ===============================
    // Constructor 2 – ID only
    // ===============================
    public StudentInfo(int studentId) {
        this.studentId = studentId;
        name = "Unknown";
        course = "Not Assigned";
        gwa = 0.0;
    }

    // ===============================
    // Constructor 3 – ID + Name
    // ===============================
    public StudentInfo(int studentId, String name) {
        this.studentId = studentId;
        this.name = name;
        course = "Not Assigned";
        gwa = 0.0;
    }

    // ===============================
    // Constructor 4 – Full details
    // ===============================
    public StudentInfo(int studentId, String name, String course, double gwa) {
        this.studentId = studentId;
        this.name = name;
        this.course = course;
        this.gwa = gwa;
    }

    // Method to display info
    public void displayInfo() {
        System.out.println("ID: " + studentId);
        System.out.println("Name: " + name);
        System.out.println("Course: " + course);
        System.out.println("GWA: " + gwa);
        System.out.println("-----------------------");
    }

    // ===============================
    // Main method (Testing)
    // ===============================
    public static void main(String[] args) {

        StudentInfo s1 = new StudentInfo();
        StudentInfo s2 = new StudentInfo(1001);
        StudentInfo s3 = new StudentInfo(1002, "Juan Dela Cruz");
        StudentInfo s4 = new StudentInfo(1003, "Maria Santos", "BSIT", 1.75);

        s1.displayInfo();
        s2.displayInfo();
        s3.displayInfo();
        s4.displayInfo();
    }
}
