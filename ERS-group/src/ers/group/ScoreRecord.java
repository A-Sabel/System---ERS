package ers.group;

public class ScoreRecord {

    public String id;
    public String name;
    public String year;
    public String semester;

    public String[] courses = new String[5];
    public String[] grades = new String[5];
    public boolean[] passed = new boolean[5];

    public double gpa;

    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(",")
          .append(name).append(",")
          .append(year).append(",")
          .append(semester);

        for (int i = 0; i < 5; i++) {
            sb.append(",")
              .append(courses[i])
              .append(",")
              .append(grades[i])
              .append(",")
              .append(passed[i]);
        }

        sb.append(",").append(String.format("%.2f", gpa));
        return sb.toString();
    }
}
