package ers.group;

import java.io.*;
import java.util.*;

public class SubjectLoader {
    private Map<String, CourseSubject> subjectMap = new LinkedHashMap<>();
    
    public void loadFromTextFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                // Format: ID, Name, Units, MaxStudents, isLab, Prerequisite, YearLevel, Semester
                String id = parts[0];
                String name = parts[1];
                int units = Integer.parseInt(parts[2]);
                int count = Integer.parseInt(parts[3]);
                boolean isLab = Boolean.parseBoolean(parts[4]);
                int yearLevel = Integer.parseInt(parts[6]);
                int semester = Integer.parseInt(parts[7]);
                CourseSubject subject = new CourseSubject(id, name, units, count, isLab, yearLevel, semester);
                subjectMap.put(id, subject);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find " + filePath);
        }

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                String id = parts[0];
                String preReqData = parts[5];
                if (!preReqData.equalsIgnoreCase("NONE")) {
                    String[] preReqIDs = preReqData.split(";");
                    CourseSubject currentSub = subjectMap.get(id);
                    for (String prID : preReqIDs) {
                        CourseSubject prObject = subjectMap.get(prID);
                        if (prObject != null) {
                            currentSub.addPrerequisite(prObject);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error during linking: " + e.getMessage());
        }
    }

    public Collection<CourseSubject> getAllSubjects() {
        return subjectMap.values();
    }
    
}