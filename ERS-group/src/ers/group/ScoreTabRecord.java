package ers.group;

public class ScoreTabRecord {
    // New field for the auto-numbering (MRK-001, etc.)
    public String recordId = ""; 
    
    public String id = "";       // Student ID (STU-XXX)
    public String name = "";     // Student Name
    public String semester = "";
    public String[] courses = new String[5];
    public String[] grades = new String[5];
    public boolean[] passed = new boolean[5];
    public double gpa = 0;

    // Convert record to a CSV line for saving
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        
        // Change: Start with recordId (the MRK-XXX number)
        sb.append(recordId).append(",") 
          .append(id).append(",")       // Student ID
          .append(semester);            // Semester
          
        for (int i = 0; i < 5; i++) {
            sb.append(",").append(courses[i]).append(",").append(grades[i]);
        }
        sb.append(",").append(String.format("%.2f", gpa)); // Format GPA to 2 decimal places
        return sb.toString();
    }
}