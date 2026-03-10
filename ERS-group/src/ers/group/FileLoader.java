package ers.group;


/**
 *
 * @author Andrea Ysabela
 */


import java.io.*;
import java.nio.file.Files;
import java.util.*;
public interface FileLoader {
    void load(String filePath);
}


abstract class BaseFileLoader implements FileLoader {
    protected void readFile(String filePath, LineProcessor processor) {
        try {
            // Resolve to absolute path first
            java.io.File file = new java.io.File(filePath);
            if (!file.exists()) {
                System.out.println("DEBUG: File not found at: " + file.getAbsolutePath());
                return;
            }

            String encrypted = new String(Files.readAllBytes(file.toPath()));

            if (encrypted.trim().isEmpty()) {
                System.out.println("DEBUG: File is empty: " + filePath);
                return;
            }

            // Decrypt per-line so each line's keyIndex starts at 0,
            // matching how FileSaver/SignUp encrypt (one line at a time).
            String[] encLines = encrypted.split("\\r?\\n");
            for (String encLine : encLines) {
                if (encLine.trim().isEmpty()) continue;
                String line = Encryption.decrypt(encLine);
                if (line == null || line.trim().isEmpty()) continue;
                processor.processLine(line);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find " + filePath);
        } catch (Exception e) {
            System.out.println("Error during file processing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FunctionalInterface
    protected interface LineProcessor {
        void processLine(String line);
    }
}


class CourseSubjectFileLoader extends BaseFileLoader {

    private final Map<String, CourseSubject> subjectMap = new LinkedHashMap<>();

    @Override
    public void load(String filePath) {
        subjectMap.clear();

        readFile(filePath, line -> {
            try {
                if (line == null || line.trim().isEmpty()) return;
                String[] parts = line.split(",", -1);
                if (parts.length < 8) return;

                String id = parts[0].trim();
                String name = parts[1].trim();
                int units = Integer.parseInt(parts[2].trim());
                int count = Integer.parseInt(parts[3].trim());
                boolean isLab = Boolean.parseBoolean(parts[4].trim());
                int yearLevel = Integer.parseInt(parts[6].trim());
                int semester = Integer.parseInt(parts[7].trim());

                CourseSubject subject = new CourseSubject(id, name, units, count, isLab, yearLevel, semester);
                subjectMap.put(id, subject);

            } catch (Exception e) {
                System.err.println("DEBUG: Skipping invalid course line: " + e.getMessage());
            }
        });

        readFile(filePath, line -> {
            try {
                if (line == null || line.trim().isEmpty()) return;
                String[] parts = line.split(",", -1);
                if (parts.length < 6) return;

                String id = parts[0].trim();
                String preReqData = parts[5].trim();
                if (preReqData.equalsIgnoreCase("NONE") || preReqData.isEmpty()) return;

                CourseSubject currentSub = subjectMap.get(id);
                if (currentSub == null) return;

                String[] preReqIDs = preReqData.split(";");
                for (String prID : preReqIDs) {
                    CourseSubject prObject = subjectMap.get(prID.trim());
                    if (prObject != null) {
                        currentSub.addPrerequisite(prObject);
                    }
                }
            } catch (Exception e) {
                System.err.println("DEBUG: Error processing prerequisites: " + e.getMessage());
            }
        });
    }

    public Collection<CourseSubject> getAllSubjects() {
        return subjectMap.values();
    }

    public Map<String, CourseSubject> getSubjectMap() {
        return subjectMap;
    }
}

class TeacherFileLoader extends BaseFileLoader {
    private final Map<String, Teachers> teacherMap = new LinkedHashMap<>();
    
    @Override
    public void load(String filePath) {
        readFile(filePath, line -> {
            String[] parts = line.split(",");
            // Format: ID, Name, Subjects
            String id = parts[0];
            String name = parts[1];
            Teachers teacher = new Teachers(id, name);
            if (parts.length > 2) {
                String[] qualifiedSubjects = parts[2].split(";");
                for (String subjectID : qualifiedSubjects) {
                    teacher.addQualifiedSubject(subjectID);
                }
            }
            teacherMap.put(id, teacher);
        });
    }
    
    public Collection<Teachers> getAllTeachers() {
        return teacherMap.values();
    }
    
    public Map<String, Teachers> getTeacherMap() {
        return teacherMap;
    }
}


class RoomFileLoader extends BaseFileLoader {
    private final List<Rooms> allRooms = new ArrayList<>();
    
    @Override
    public void load(String filePath) {
        readFile(filePath, line -> {
            String[] parts = line.split(",");
            // Format: RoomID, RoomName, isLabRoom, Capacity
            String roomID = parts[0];
            String roomName = parts[1];
            boolean isLabRoom = Boolean.parseBoolean(parts[2]);
            int capacity = Integer.parseInt(parts[3]);
            Rooms room = new Rooms(roomID, roomName, isLabRoom, capacity);
            allRooms.add(room);
        });
    }


    public Collection<Rooms> getAllRooms() {
        return allRooms;
    }


    public Map<String, Rooms> getRoomMap() {
        Map<String, Rooms> roomMap = new LinkedHashMap<>();
        for (Rooms room : allRooms) {
            roomMap.put(room.getRoomID(), room);
        }
        return roomMap;
    }
}


class SectionFileLoader extends BaseFileLoader {
    private final List<Section> allSections = new ArrayList<>();
    private final Map<String, CourseSubject> courseMap;
    
    public SectionFileLoader(Map<String, CourseSubject> courseMap) {
        this.courseMap = courseMap;
    }
    
    @Override
    public void load(String filePath) {
        readFile(filePath, line -> {
            String[] parts = line.split("\\|");
            // Format: SectionID|CourseID|TeacherID|ScheduleID|RoomID|StudentCount|StudentList
            if (parts.length < 7) return;
            String sectionID = parts[0].trim();
            String courseID = parts[1].trim();
            String studentList = parts[6].trim();
            CourseSubject course = courseMap.get(courseID);
            if (course == null) {
                System.out.println("Warning: Course " + courseID + " not found");
                return;
            }
            Section section = new Section(sectionID, course, course.getStudentCount());
            if (!studentList.isEmpty() && !studentList.equals("NULL")) {
                String[] studentIDs = studentList.split(";");
                for (String studentID : studentIDs) {
                    section.addStudent(studentID.trim());
                }
            }
            allSections.add(section);
        });
    }
    
    public Collection<Section> getAllSections() {
        return allSections;
    }
    
    public Map<String, Section> getSectionMap() {
        Map<String, Section> sectionMap = new LinkedHashMap<>();
        for (Section section : allSections) {
            sectionMap.put(section.getSectionID(), section);
        }
        return sectionMap;
    }
}


class ScheduleFileLoader extends BaseFileLoader {
    private final List<Schedule> allSchedules = new ArrayList<>();
    
    @Override
    public void load(String filePath) {
        readFile(filePath, line -> {
            String[] parts = line.split(",");
            // Format: scheduleID,courseID,room,day,startTime,endTime,teacherName
            if (parts.length < 7) return;
            String scheduleID = parts[0].trim();
            String courseID = parts[1].trim();
            String room = parts[2].trim();
            String day = parts[3].trim();
            String startTime = parts[4].trim();
            String endTime = parts[5].trim();
            String teacherName = parts[6].trim();
            
            Schedule schedule = new Schedule(scheduleID, courseID, room, day, startTime, endTime, teacherName);
            allSchedules.add(schedule);
        });
    }
    
    public Collection<Schedule> getAllSchedules() {
        return allSchedules;
    }
    
    public Map<String, Schedule> getScheduleMap() {
        Map<String, Schedule> scheduleMap = new LinkedHashMap<>();
        for (Schedule schedule : allSchedules) {
            scheduleMap.put(schedule.getScheduleID(), schedule);
        }
        return scheduleMap;
    }
}


class EnrollmentFileLoader extends BaseFileLoader {
    private final List<Enrollment> allEnrollments = new ArrayList<>();
    
    private String expandSemester(String abbrev) {
        switch (abbrev) {
            case "1": return "1st Semester";
            case "2": return "2nd Semester";
            case "3": return "Summer";
            default: return abbrev; 
        }
    }
    
    @Override
    public void load(String filePath) {
        allEnrollments.clear();
        // readFile now automatically decrypts the line!
        readFile(filePath, line -> {
            String[] parts = line.split(",");
            if (parts.length < 5) return; // Basic validation
            
            String studentID = parts[0].trim();
            String courseList = parts[1].trim();
            String yearLevel = parts[2].trim();
            String semester = expandSemester(parts[3].trim());
            String status = parts[4].trim();
            
            // Handle optional columns safely
            String sectionList = parts.length > 5 ? parts[5].trim() : "";
            String academicYear = parts.length > 6 ? parts[6].trim() : "";
            String courseStatusesStr = parts.length > 7 ? parts[7].trim() : "";
            
            String[] courses = courseList.split(";");
            String[] sections = sectionList.split(";");

            Map<String, String> courseStatuses = new HashMap<>();
            if (!courseStatusesStr.isEmpty()) {
                String[] statusPairs = courseStatusesStr.split(";");
                for (String pair : statusPairs) {
                    if (pair.contains(":")) {
                        String[] kv = pair.split(":");
                        courseStatuses.put(kv[0].trim(), kv[1].trim());
                    }
                }
            }

            for (int i = 0; i < courses.length; i++) {
                String courseID = courses[i].trim();
                if (courseID.isEmpty() || courseID.equalsIgnoreCase("NONE")) continue;
                
                String enrollmentID = "ENR-" + studentID + "-" + courseID;
                Enrollment enrollment = new Enrollment(enrollmentID, studentID, courseID, yearLevel, semester, status, academicYear);
                
                if (i < sections.length && !sections[i].trim().isEmpty()) {
                    enrollment.setSectionID(sections[i].trim());
                }
                
                // Sync specific course statuses
                enrollment.getCourseStatuses().clear();
                if (courseStatuses.containsKey(courseID)) {
                    enrollment.setCourseStatus(courseID, courseStatuses.get(courseID));
                } else {
                    enrollment.setCourseStatus(courseID, "PENDING");
                }
                
                allEnrollments.add(enrollment);
            }
        });
    }
    
    public Collection<Enrollment> getAllEnrollments() { return allEnrollments; }
}


class StudentFileLoader extends BaseFileLoader {
    private final List<Student> allStudents = new ArrayList<>();
    private final Map<String, Student> studentMap = new LinkedHashMap<>(); 

    public Map<String, Student> getStudentMap() {
        return studentMap;
    }

    private String expandSemester(String abbrev) {
        if (abbrev == null || abbrev.trim().isEmpty()) return "1st Semester";
        switch (abbrev.trim()) {
            case "1": return "1st Semester";
            case "2": return "2nd Semester";
            case "3": return "Summer";
            default: return abbrev;
        }
    }
    
    @Override
    public void load(String filePath) {
        studentMap.clear(); 
        readFile(filePath, line -> {
            // line is already decrypted here thanks to BaseFileLoader.readFile
            String[] parts = line.split(",", -1); 
            try {
                String id = parts.length > 0 ? parts[0].trim() : "";
                if (id.isEmpty() || !id.startsWith("STU-")) return;

                String name = parts.length > 1 ? parts[1].trim() : "";
                int age = 0;
                if (parts.length > 2 && !parts[2].trim().isEmpty()) {
                    try { age = Integer.parseInt(parts[2].trim()); } catch (NumberFormatException ignored) {}
                }
                String dob = parts.length > 3 ? parts[3].trim() : "";
                String yearLevel = parts.length > 4 ? parts[4].trim() : "";
                String currentSemester = parts.length > 5 ? expandSemester(parts[5].trim()) : "";
                String section = parts.length > 6 ? parts[6].trim() : "";
                String studentType = parts.length > 7 ? parts[7].trim() : "Regular";
                String status = parts.length > 8 ? parts[8].trim() : "Active";

                ArrayList<String> subjects = new ArrayList<>();
                if (parts.length > 9 && !parts[9].trim().isEmpty()) {
                    for (String s : parts[9].split(";")) subjects.add(s.trim());
                }

                double gwa = 0.0;
                if (parts.length > 10 && !parts[10].trim().isEmpty()) {
                    try { gwa = Double.parseDouble(parts[10].trim()); } catch (NumberFormatException ignored) {}
                }

                // ... keep the rest of your address/parent mapping the same ...
                String email = parts.length > 11 ? parts[11].trim() : "";
                // (Omitted for brevity, but keep your original index 12-17 logic)

                Student student = new Student(id, name, age, dob, yearLevel, currentSemester, section,
                    studentType, status, subjects, gwa, email, "", "", "", "", "", "");

                studentMap.put(id, student);
            } catch (Exception e) {
                System.err.println("Error parsing decrypted student record: " + e.getMessage());
            }
        });

        allStudents.clear();
        allStudents.addAll(studentMap.values());
    }
    
    public Collection<Student> getAllStudents() { return allStudents; }
}


class MarksheetFileLoader extends BaseFileLoader {
    private final List<Marksheet> allMarksheets = new ArrayList<>();
    
    /**
     * Convert abbreviated semester format to full format for memory representation
     * 1 -> 1st Semester, 2 -> 2nd Semester, 3 -> Summer
     */
    private String expandSemester(String abbrev) {
        switch (abbrev) {
            case "1": return "1st Semester";
            case "2": return "2nd Semester";
            case "3": return "Summer";
            default: return abbrev; // Already in full format or unknown
        }
    }
    
    @Override
    public void load(String filePath) {
        allMarksheets.clear(); // Clear before re-loading to prevent duplicates
        readFile(filePath, line -> {
            try {
                String[] parts = line.split(",");
                // Format: ID, StudentID, Semester, YearLevel, Course1, Score1, Course2, Score2, Course3, Score3, Course4, Score4, Course5, Score5, Average
                if (parts.length < 14) return;

                String id = parts[0].trim();
                String studentID = parts[1].trim();
                String semester = expandSemester(parts[2].trim());
                String yearLevel = parts.length > 3 ? parts[3].trim() : ""; // Read year level from field 3

                // Extract 5 course-score pairs (starting from index 4 after yearLevel)
                String[] subjects = new String[5];
                double[] marks = new double[5];

                for (int i = 0; i < 5; i++) {
                    int courseIndex = 4 + (i * 2); // Adjusted index after adding yearLevel field
                    int scoreIndex = 5 + (i * 2);

                    if (courseIndex < parts.length) {
                        subjects[i] = parts[courseIndex].trim();
                    } else {
                        subjects[i] = "";
                    }

                    if (scoreIndex < parts.length) {
                        String scoreStr = parts[scoreIndex].trim();
                        if (scoreStr.equals("PENDING") || scoreStr.isEmpty()) {
                            marks[i] = 0.0;
                        } else {
                            try {
                                marks[i] = Double.parseDouble(scoreStr);
                            } catch (NumberFormatException e) {
                                marks[i] = 0.0;
                            }
                        }
                    } else {
                        marks[i] = 0.0;
                    }
                }

                // Pass yearLevel as schoolYear parameter
                Marksheet marksheet = new Marksheet(studentID, "", yearLevel, semester, subjects, marks);
                allMarksheets.add(marksheet);
            } catch (Exception e) {
                System.out.println("Error processing marksheet line: " + line);
                e.printStackTrace();
            }
        });
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
