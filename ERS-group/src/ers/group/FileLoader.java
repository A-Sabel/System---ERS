package ers.group;


/**
 *
 * @author Andrea Ysabela
 */


import java.io.*;
import java.util.*;
public interface FileLoader {
    void load(String filePath);
}


abstract class BaseFileLoader implements FileLoader {
    protected void readFile(String filePath, LineProcessor processor) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) continue;
                processor.processLine(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find " + filePath);
        } catch (Exception e) {
            System.out.println("Error during file processing: " + e.getMessage());
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
        readFile(filePath, line -> {
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
        });
       
        // prerequisites
        readFile(filePath, line -> {
            String[] parts = line.split(",");
            String id = parts[0];
            String preReqData = parts[5];
            if (!preReqData.equalsIgnoreCase("NONE")) {
                String[] preReqIDs = preReqData.split(";");
                CourseSubject currentSub = subjectMap.get(id);
                if (currentSub != null) {
                    for (String prID : preReqIDs) {
                        CourseSubject prObject = subjectMap.get(prID);
                        if (prObject != null) {
                            currentSub.addPrerequisite(prObject);
                        }
                    }
                }
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
        allEnrollments.clear();
        readFile(filePath, line -> {
            String[] parts = line.split(",");
            // New format: studentID,courseList,yearLevel,semester,status,sectionList,academicYear,courseStatuses
            if (parts.length < 6) return;
            String studentID = parts[0].trim();
            String courseList = parts[1].trim();
            String yearLevel = parts[2].trim();
            String semester = expandSemester(parts[3].trim());
            String status = parts[4].trim();
            String sectionList = parts.length > 5 ? parts[5].trim() : "";
            String academicYear = parts.length > 6 ? parts[6].trim() : AcademicUtilities.getAcademicYear();
            String courseStatusList = parts.length > 7 ? parts[7].trim() : "";
            
            // Parse courses and sections (using basic Java only, no APIs)
            String[] courses = courseList.split(";");
            String[] sections = sectionList.split(";");
            
            // Parse course statuses (format: "CS101:PASSED;CS102:FAILED")
            java.util.Map<String, String> courseStatuses = new java.util.HashMap<>();
            if (!courseStatusList.isEmpty()) {
                String[] statusPairs = courseStatusList.split(";");
                for (String pair : statusPairs) {
                    String[] parts2 = pair.split(":");
                    if (parts2.length == 2) {
                        courseStatuses.put(parts2[0].trim(), parts2[1].trim());
                    }
                }
            }
            
            // Create individual enrollments for each course (for compatibility with existing system)
            for (int i = 0; i < courses.length; i++) {
                String courseID = courses[i].trim();
                if (courseID.isEmpty()) continue;
                
                String enrollmentID = "ENR-" + studentID + "-" + courseID;
                
                Enrollment enrollment = new Enrollment(enrollmentID, studentID, courseID, yearLevel, semester, status, academicYear);
                
                // Set section if available
                if (i < sections.length && !sections[i].trim().isEmpty()) {
                    enrollment.setSectionID(sections[i].trim());
                }
                
                // IMPORTANT: Clear the default PENDING status before setting actual status
                // This prevents constructor-initialized PENDING from persisting
                enrollment.getCourseStatuses().clear();
                
                // Set course status from file, or default to PENDING if not found
                if (courseStatuses.containsKey(courseID)) {
                    enrollment.setCourseStatus(courseID, courseStatuses.get(courseID));
                } else {
                    enrollment.setCourseStatus(courseID, "PENDING");
                }
                
                allEnrollments.add(enrollment);
            }
        });
    }
   
    public Collection<Enrollment> getAllEnrollments() {
        return allEnrollments;
    }
   
    public Map<String, Enrollment> getEnrollmentMap() {
        Map<String, Enrollment> enrollmentMap = new LinkedHashMap<>();
        for (Enrollment enrollment : allEnrollments) {
            String key = enrollment.getStudentID() + "_" + enrollment.getCourseID();
            enrollmentMap.put(key, enrollment);
        }
        return enrollmentMap;
    }
}


class StudentFileLoader extends BaseFileLoader {
    private final List<Student> allStudents = new ArrayList<>();
    private final Map<String, Student> studentMap = new LinkedHashMap<>(); // Deduplicate by ID
   
    /**
     * Convert abbreviated semester format to full format for memory representation
     * 1 -> 1st Semester, 2 -> 2nd Semester, 3 -> Summer
     */
    private String expandSemester(String abbrev) {
        if (abbrev == null || abbrev.trim().isEmpty()) return "1st Semester";
        switch (abbrev.trim()) {
            case "1": return "1st Semester";
            case "2": return "2nd Semester";
            case "3": return "Summer";
            default: return abbrev; // Already in full format or unknown
        }
    }
   
    @Override
    public void load(String filePath) {
        studentMap.clear(); // Clear the map before loading
        readFile(filePath, line -> {
            String[] parts = line.split(",", -1); // -1 to preserve trailing empty strings
            // Format: ID, Name, Age, DOB, YearLevel, CurrentSemester, Section, StudentType, SubjectsEnrolled, GWA, Email, PhoneNumber, Gender, Address, FathersName, MothersName, GuardiansPhoneNumber
            if (parts.length < 17) return;
           
            try {
                String id = parts[0].trim();
                String name = parts[1].trim();
                int age = Integer.parseInt(parts[2].trim());
                String dob = parts[3].trim();
                String yearLevel = parts[4].trim();
                String currentSemester = expandSemester(parts[5].trim()); // Expand compressed format
                String section = parts[6].trim();
                String studentType = parts[7].trim();
                ArrayList<String> subjects = new ArrayList<>();
                if (!parts[8].trim().isEmpty()) {
                    String[] subjectList = parts[8].split(";");
                    for (String subject : subjectList) {
                        subjects.add(subject.trim());
                    }
                }
               
                // Handle empty GWA field
                double gwa = 0.0;
                if (!parts[9].trim().isEmpty()) {
                    try {
                        gwa = Double.parseDouble(parts[9].trim());
                    } catch (NumberFormatException e) {
                        gwa = 0.0;
                    }
                }
               
                String email = parts[10].trim();
                String phoneNumber = parts[11].trim();
                String gender = parts[12].trim();
                String address = parts[13].trim();
                String fathersName = parts[14].trim();
                String mothersName = parts[15].trim();
                String guardiansPhoneNumber = parts[16].trim();
               
                Student student = new Student(id, name, age, dob, yearLevel, currentSemester, section,
                        studentType, subjects, gwa, email, phoneNumber, gender, address,
                        fathersName, mothersName, guardiansPhoneNumber);
                
                // Deduplicate: only keep the last occurrence of each student ID
                studentMap.put(id, student);
            } catch (Exception e) {
                // Log parsing error but continue with other records
                System.err.println("Error parsing student record: " + line);
                e.printStackTrace();
            }
        });
        
        // Rebuild allStudents list from deduplicated map
        allStudents.clear();
        allStudents.addAll(studentMap.values());
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
        readFile(filePath, line -> {
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
