package ers.group;

import java.io.*;
import java.util.*;

public class StudentFileLoader implements FileLoader {
    private final List<Student> allStudents = new ArrayList<>();
    
    @Override
    public void load(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",");
                // Format: ID, Name, Age, DOB, YearLevel, StudentType, SubjectsEnrolled, GWA, Email, PhoneNumber, Gender, Address, FathersName, MothersName, GuardiansPhoneNumber
                if (parts.length < 16) continue;
                
                String id = parts[0].trim();
                String name = parts[1].trim();
                int age = Integer.parseInt(parts[2].trim());
                String dob = parts[3].trim();
                String yearLevel = parts[4].trim();
                String section = parts[5].trim();
                String studentType = parts[6].trim();
                ArrayList<String> subjects = new ArrayList<>();
                if (!parts[7].trim().isEmpty()) {
                    String[] subjectList = parts[7].split(";");
                    for (String subject : subjectList) {
                        subjects.add(subject.trim());
                    }
                }
                double gwa = Double.parseDouble(parts[8].trim());
                String email = parts[9].trim();
                String phoneNumber = parts[10].trim();
                String gender = parts[11].trim();
                String address = parts[12].trim();
                String fathersName = parts[13].trim();
                String mothersName = parts[14].trim();
                String guardiansPhoneNumber = parts[15].trim();
                
                Student student = new Student(id, name, age, dob, yearLevel, section, 
                        studentType, subjects, gwa, email, phoneNumber, gender, address, 
                        fathersName, mothersName, guardiansPhoneNumber);
                allStudents.add(student);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find " + filePath);
        } catch (Exception e) {
            System.out.println("Error during file processing: " + e.getMessage());
        }
    }
    
    public Collection<Student> getAllStudents() {
        return allStudents;
    }
    
    public Map<String, Student> getStudentMap() {
        Map<String, Student> studentMap = new LinkedHashMap<>();
        for (Student student : allStudents) {
            studentMap.put(student.getStudentID(), student);
        }
        return studentMap;
    }
}
