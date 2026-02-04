package ers.group;

import java.io.*;
import java.util.*;

public class MarksheetFileLoader implements FileLoader {
    private final List<Marksheet> allMarksheets = new ArrayList<>();
    
    @Override
    public void load(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",");
                // Format: ID, StudentID, Semester, Course1, Score1, Course2, Score2, Course3, Score3, Course4, Score4, Course5, Score5, Average
                if (parts.length < 14) continue;
                
                String id = parts[0].trim();
                String studentID = parts[1].trim();
                String semester = parts[2].trim();
                
                // Extract 5 course-score pairs
                String[] subjects = new String[5];
                double[] marks = new double[5];
                
                for (int i = 0; i < 5; i++) {
                    int courseIndex = 3 + (i * 2);
                    int scoreIndex = 4 + (i * 2);
                    
                    subjects[i] = parts[courseIndex].trim();
                    String scoreStr = parts[scoreIndex].trim();
                    marks[i] = scoreStr.isEmpty() ? 0.0 : Double.parseDouble(scoreStr);
                }
                
                // Note: Marksheet constructor needs studentName, but file only has ID
                // We'll use empty string for now or need to look up from student data
                Marksheet marksheet = new Marksheet(studentID, "", "", semester, subjects, marks);
                allMarksheets.add(marksheet);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find " + filePath);
        } catch (Exception e) {
            System.out.println("Error during file processing: " + e.getMessage());
        }
    }
    
    public Collection<Marksheet> getAllMarksheets() {
        return allMarksheets;
    }
    
    public Map<String, Marksheet> getMarksheetMap() {
        Map<String, Marksheet> marksheetMap = new LinkedHashMap<>();
        for (Marksheet marksheet : allMarksheets) {
            marksheetMap.put(marksheet.getStudentID() + "_" + marksheet.getSemester(), marksheet);
        }
        return marksheetMap;
    }
}
