package ers.group;

import java.util.ArrayList;

public class Student {

    // identity
    private final String studentID;
    private String studentName;
    private int age;
    private final String dob;
    private String yearLevel;
    private String studentType;
    private ArrayList<String> subjectsEnrolled;
    private double gwa;
    
    // contact information
    private String email;
    private String phoneNumber;
    private final String gender;
    private String address;
    
    // family information
    private String fathersName;
    private String mothersName;
    private String guardiansPhoneNumber;

    // constructors
    public Student(String studentID, String studentName, int age,
                    String dob, String yearLevel, String studentType,
                    ArrayList<String> subjectsEnrolled, double gwa,
                    String email, String phoneNumber, String gender,
                    String address, String fathersName, String mothersName,
                    String guardiansPhoneNumber) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.age = age;
        this.dob = dob;
        this.yearLevel = yearLevel;
        this.studentType = studentType;
        this.subjectsEnrolled = subjectsEnrolled;
        this.gwa = gwa;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.address = address;
        this.fathersName = fathersName;
        this.mothersName = mothersName;
        this.guardiansPhoneNumber = guardiansPhoneNumber;
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

    // Contact information setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Family information setters
    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public void setMothersName(String mothersName) {
        this.mothersName = mothersName;
    }

    public void setGuardiansPhoneNumber(String guardiansPhoneNumber) {
        this.guardiansPhoneNumber = guardiansPhoneNumber;
    }

    // Contact information getters
    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    // Family information getters
    public String getFathersName() {
        return fathersName;
    }

    public String getMothersName() {
        return mothersName;
    }

    public String getGuardiansPhoneNumber() {
        return guardiansPhoneNumber;
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
        System.out.println("Email: " + email);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Gender: " + gender);
        System.out.println("Address: " + address);
        System.out.println("Father's Name: " + fathersName);
        System.out.println("Mother's Name: " + mothersName);
        System.out.println("Guardian's Phone Number: " + guardiansPhoneNumber);
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
