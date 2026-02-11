package ers.group;

public class ScoreTabRecord {
    public String id = "";
    public String name = "";
    public String semester = "";
    public String[] courses = new String[5];
    public String[] grades = new String[5];
    public boolean[] passed = new boolean[5];
    public double gpa = 0;

    // Convert record to a CSV line for saving
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(",").append(id).append(",").append(semester);
        for (int i = 0; i < 5; i++) {
            sb.append(",").append(courses[i]).append(",").append(grades[i]);
        }
        sb.append(",").append(gpa);
        return sb.toString();
    }
}
