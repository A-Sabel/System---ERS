package ers.group;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Student {


    // identity
    private final String studentID;
    private String studentName;
    private int age;
    private final String dob;
    private String yearLevel;
    private String currentSemester; // Track which semester the student is in
    private String section;
    private String studentType;
    private ArrayList<String> subjectsEnrolled;
    private double gwa;
    private double cumulativeGWA; // Overall GWA across all semesters
    private Map<String, Double> semesterGWAs; // GWA for each semester (key: "1st Year-1st Semester")
    private String latinHonor; // Summa Cum Laude, Magna Cum Laude, Cum Laude, or empty
    private Set<String> completedCourses; // Set of course codes with PASSED status
   
    // contact information
    private String email;
    private String phoneNumber;
    private final String gender;
    private String address;
   
    // family information
    private String fathersName;
    private String mothersName;
    private String guardiansPhoneNumber;


    // generate student number
    private static int nextIdNum = 1;




    // constructors
    // Constructor for loading existing students from file (uses provided ID)
    public Student(String studentID, String studentName, int age, String dob, String yearLevel, String currentSemester, String section, String studentType, ArrayList<String> subjectsEnrolled, double gwa, String email, String phoneNumber, String gender, String address, String fathersName, String mothersName, String guardiansPhoneNumber) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.age = age;
        this.dob = dob;
        this.yearLevel = yearLevel;
        this.currentSemester = currentSemester;
        this.section = section;
        this.studentType = studentType;
        this.subjectsEnrolled = subjectsEnrolled;
        this.gwa = gwa;
        this.cumulativeGWA = 0.0; // Will be calculated
        this.semesterGWAs = new HashMap<>();
        this.latinHonor = "";
        this.completedCourses = new HashSet<>();
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.address = address;
        this.fathersName = fathersName;
        this.mothersName = mothersName;
        this.guardiansPhoneNumber = guardiansPhoneNumber;
    }
   
    // Constructor for creating new students (generates new ID)
    public Student(String studentName, int age, String dob, String yearLevel, String currentSemester, String section, String studentType, ArrayList<String> subjectsEnrolled, double gwa, String email, String phoneNumber, String gender, String address, String fathersName, String mothersName, String guardiansPhoneNumber) {
        this.studentID = generateNewID();
        this.studentName = studentName;
        this.age = age;
        this.dob = dob;
        this.yearLevel = yearLevel;
        this.currentSemester = currentSemester;
        this.section = section;
        this.studentType = studentType;
        this.subjectsEnrolled = subjectsEnrolled;
        this.gwa = gwa;
        this.cumulativeGWA = 0.0; // Will be calculated
        this.semesterGWAs = new HashMap<>();
        this.latinHonor = "";
        this.completedCourses = new HashSet<>();
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


    public String getCurrentSemester() {
        return currentSemester;
    }


    public String getSection() {
        return section;
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

    public double getCumulativeGWA() {
        return cumulativeGWA;
    }

    public Map<String, Double> getSemesterGWAs() {
        return semesterGWAs;
    }

    public String getLatinHonor() {
        return latinHonor;
    }

    public Set<String> getCompletedCourses() {
        return completedCourses;
    }


    public static String generateNewID() {
        return String.format("STU-%03d", nextIdNum++);
    }


    public static void setNextIdNum(int lastID) {
        nextIdNum = lastID + 1;
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


    public void setCurrentSemester(String currentSemester) {
        this.currentSemester = currentSemester;
    }


    public void setSection(String section) {
        this.section = section;
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

    public void setCumulativeGWA(double cumulativeGWA) {
        this.cumulativeGWA = cumulativeGWA;
    }

    public void setSemesterGWAs(Map<String, Double> semesterGWAs) {
        this.semesterGWAs = semesterGWAs;
    }

    public void setSemesterGWA(String semesterKey, double gwa) {
        this.semesterGWAs.put(semesterKey, gwa);
    }

    public void setLatinHonor(String latinHonor) {
        this.latinHonor = latinHonor;
    }

    public void setCompletedCourses(Set<String> completedCourses) {
        this.completedCourses = completedCourses;
    }

    public void addCompletedCourse(String courseCode) {
        this.completedCourses.add(courseCode);
    }

    public void removeCompletedCourse(String courseCode) {
        this.completedCourses.remove(courseCode);
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
        System.out.println("Section: " + section);
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
        // Check if all prerequisites are in the completedCourses set
        for (CourseSubject prerequisite : prerequisites) {
            if (!completedCourses.contains(prerequisite.getCourseSubjectID())) {
                return false;
            }
        }
        return true;
    }
}

