package ers.group;

import java.util.ArrayList;

public class Student {

    // identity
    private String studentID;
    private String studentName;
    private int age;
    private String dob;
    private String yearLevel;
    private String studentType;
    private ArrayList<String> subjectsEnrolled;
    private double gwa;

    // constructors
    public Student(String studentID, String studentName, int age,
                    String dob, String yearLevel, String studentType,
                    ArrayList<String> subjectsEnrolled, double gwa) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.age = age;
        this.dob = dob;
        this.yearLevel = yearLevel;
        this.studentType = studentType;
        this.subjectsEnrolled = subjectsEnrolled;
        this.gwa = gwa;
    }

    // methods
    public String getStudentID() {
        return studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getAge() {
        return age;
    }

    public String getDateOfBirth() {
        return dob;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    public String getStudentType() {
        return studentType;
    }

    public ArrayList<String> getSubjectsEnrolled() {
        return subjectsEnrolled;
    }

    public double getGwa() {
        return gwa;
    }

    // Setters
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }

    public void setStudentType(String studentType) {
        this.studentType = studentType;
    }

    public void setSubjectsEnrolled(ArrayList<String> subjectsEnrolled) {
        this.subjectsEnrolled = subjectsEnrolled;
    }

    public void setGwa(double gwa) {
        this.gwa = gwa;
    }

    // Display method
    public void displayStudentInfo() {
        System.out.println("Student ID: " + studentID);
        System.out.println("Student Name: " + studentName);
        System.out.println("Age: " + age);
        System.out.println("Date of Birth: " + dob);
        System.out.println("Year Level: " + yearLevel);
        System.out.println("Student Type: " + studentType);
        System.out.println("Subjects Enrolled: " + subjectsEnrolled);
        System.out.println("GWA: " + gwa);
    }
    public boolean hasPassed(ArrayList<CourseSubject> prerequisites) {
        if (prerequisites == null || prerequisites.isEmpty()) {
            return true;
        }
        // Check if all prerequisites are in the subjectsEnrolled list
        for (CourseSubject prerequisite : prerequisites) {
            if (!subjectsEnrolled.contains(prerequisite.getCourseSubjectID())) {
                return false;
            }
        }
        return true;
    }}
