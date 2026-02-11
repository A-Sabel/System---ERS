package ers.group;
public class Marksheet {


    private String studentID;
    private String studentName;
    private String schoolYear;
    private String semester;
    private String[] subjects; // size = 5
    private double[] marks;    // size = 5


    // CONSTRUCTOR
    public Marksheet(String studentID, String studentName,
                    String schoolYear, String semester,
                    String[] subjects, double[] marks) {


        this.studentID = studentID;
        this.studentName = studentName;
        this.schoolYear = schoolYear;
        this.semester = semester;
        this.subjects = subjects;
        this.marks = marks;
    }


    // GETTERS
    public String getStudentID() { return studentID; }
    public String getStudentName() { return studentName; }
    public String getSchoolYear() { return schoolYear; }
    public String getSemester() { return semester; }
    public String[] getSubjects() { return subjects; }
    public double[] getMarks() { return marks; }
}



