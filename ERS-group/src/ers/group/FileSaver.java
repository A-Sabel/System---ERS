package ers.group;


public interface FileSaver {
    void save(String filePath, java.util.List<?> data) throws java.io.IOException;
}


abstract class BaseFileSaver<T> implements FileSaver {
    protected abstract String formatLine(T item);
    
    @Override
    public void save(String filePath, java.util.List<?> data) throws java.io.IOException {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(filePath))) {
            for (Object obj : data) {
                @SuppressWarnings("unchecked")
                T item = (T) obj;
                String line = formatLine(item);
                if (line != null && !line.isEmpty()) {
                    // ENCRYPT before writing
                    writer.write(Encryption.encrypt(line));
                    writer.newLine();
                }
            }
        }
    }
    protected void update(String filePath, java.util.List<T> data, java.util.function.Predicate<T> predicate, T updatedItem) throws java.io.IOException {
        for (int i = 0; i < data.size(); i++) {
            if (predicate.test(data.get(i))) {
                data.set(i, updatedItem);
                break;
            }
        }
        save(filePath, data);
    }

    protected void delete(String filePath, java.util.List<T> data, java.util.function.Predicate<T> predicate) throws java.io.IOException {
        data.removeIf(predicate);
        save(filePath, data);
    }
}


class StudentFileSaver extends BaseFileSaver<Student> {
    
    /**
     * Convert full semester format to abbreviated format for file storage
     * 1st Semester -> 1, 2nd Semester -> 2, Summer -> 3
     */
    private String compressSemester(String full) {
        if (full == null) return "1"; // Default to 1 if null
        switch (full) {
            case "1st Semester": return "1";
            case "2nd Semester": return "2";
            case "Summer": return "3";
            default: return full; // Already abbreviated or unknown
        }
    }
    
    @Override
    public void save(String filePath, java.util.List<?> data) throws java.io.IOException {
        // Deduplicate students by ID before saving
        java.util.Map<String, Student> uniqueStudents = new java.util.LinkedHashMap<>();
        
        for (Object obj : data) {
            @SuppressWarnings("unchecked")
            Student student = (Student) obj;
            // Keep the last occurrence of each student ID (most up-to-date)
            uniqueStudents.put(student.getStudentID(), student);
        }
        
        // Write deduplicated students
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(filePath))) {
            for (Student student : uniqueStudents.values()) {
                String line = formatLine(student);
                if (line != null && !line.isEmpty()) {
                    writer.write(Encryption.encrypt(line));
                    writer.newLine();
                }
            }
        }
    }
    
    @Override
    protected String formatLine(Student s) {
        String subjects = String.join(";", s.getSubjectsEnrolled());
        String compressedSemester = compressSemester(s.getCurrentSemester());
        return String.join(",",
        
            s.getStudentID(),
            s.getStudentName(),
            String.valueOf(s.getAge()),
            s.getDateOfBirth(),
            s.getYearLevel(),
            compressedSemester, // Save compressed format (1, 2, or 3)
            s.getSection(),
            s.getStudentType(),
            s.getStatus(),
            subjects,
            String.valueOf(s.getGwa()),
            s.getEmail(),
            s.getPhoneNumber(),
            s.getGender(),
            s.getAddress(),
            s.getFathersName(),
            s.getMothersName(),
            s.getGuardiansPhoneNumber()
        );
    }
}


class ScheduleFileSaver extends BaseFileSaver<Schedule> {
    @Override
    protected String formatLine(Schedule s) {
        return String.join(",",
            s.getScheduleID(),
            s.getCourseID(),
            s.getRoom(),
            s.getDay(),
            s.getStartTime(),
            s.getEndTime(),
            s.getTeacherName()
        );
    }
}


class SectionFileSaver extends BaseFileSaver<Section> {
    @Override
    protected String formatLine(Section sec) {
        String studentList = String.join(";", sec.getEnrolledStudentIDs());
        return String.join(",",
            sec.getSectionID(),
            sec.getSubject().getCourseSubjectID(),
            sec.getSchedule().getScheduleID(),
            sec.getInstructor().getTeacherName(),
            String.valueOf(sec.getCapacityLimit()),
            String.valueOf(sec.getCurrentEnrollment()),
            studentList
        );
    }
}


class CourseSubjectFileSaver extends BaseFileSaver<CourseSubject> {
    @Override
    protected String formatLine(CourseSubject course) {
        String prereqs = course.getPrerequisitesString();
        return String.join(",",
            course.getCourseSubjectID(),
            course.getCourseSubjectName(),
            String.valueOf(course.getUnits()),
            String.valueOf(course.getStudentCount()),
            String.valueOf(course.isLabRoom()),
            prereqs,
            String.valueOf(course.getYearLevel()),
            String.valueOf(course.getSemester())
        );
    }
}


class TeachersFileSaver extends BaseFileSaver<Teachers> {
    @Override
    protected String formatLine(Teachers teacher) {
        String subjects = String.join(";", teacher.getQualifiedSubjectIDs());
        String schedules = String.join(";", teacher.getAssignedSchedules());
        return String.join(",",
            teacher.getTeacherID(),
            teacher.getTeacherName(),
            subjects,
            schedules
        );
    }
}


class RoomsFileSaver extends BaseFileSaver<Rooms> {
    @Override
    protected String formatLine(Rooms room) {
        return String.join(",",
            room.getRoomID(),
            room.getRoomName(),
            String.valueOf(room.getCapacity())
        );
    }
}


class EnrollmentFileSaver extends BaseFileSaver<Enrollment> {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EnrollmentFileSaver.class.getName());
    
    /**
     * Convert full semester format to abbreviated format for file storage
     * 1st Semester -> 1, 2nd Semester -> 2, Summer -> 3
     */
    private String compressSemester(String full) {
        switch (full) {
            case "1st Semester": return "1";
            case "2nd Semester": return "2";
            case "Summer": return "3";
            default: return full; // Already abbreviated or unknown
        }
    }

    @Override
    protected String formatLine(Enrollment enrollment) {
        return enrollment.getStudentID() + "," + enrollment.getCourseID() + "," + enrollment.getYearLevel() + "," + compressSemester(enrollment.getSemester()) + "," + enrollment.getStatus() + "," + (enrollment.getSectionID() != null ? enrollment.getSectionID() : "");
    }
        public void save(String filePath, java.util.List<?> data) throws java.io.IOException {
        // Cast and validate before saving
        @SuppressWarnings("unchecked")
        java.util.List<Enrollment> enrollments = (java.util.List<Enrollment>) data;
        saveEnrollmentsByStudent(filePath, enrollments);
    }
    
    /**
     * Validates that a student's enrolled courses span at least 3 different days.
     * @param studentID The student ID to validate
     * @param courseIDs List of course IDs the student is enrolled in
     * @return The set of unique days scheduled, or empty set if none found
     */
    private java.util.Set<String> getScheduledDays(String studentID, java.util.List<String> courseIDs) {
        // Load schedules from Schedule.txt
        java.util.Set<String> scheduledDays = new java.util.HashSet<>();
        String scheduleFilePath = FilePathResolver.resolveScheduleFilePath();
        
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(scheduleFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) continue;
                
                String courseID = parts[1].trim();
                String day = parts[3].trim();
                
                // Check if this schedule matches any of the student's courses
                for (String enrolledCourse : courseIDs) {
                    if (courseID.startsWith(enrolledCourse)) {
                        scheduledDays.add(day);
                        break;
                    }
                }
            }
        } catch (java.io.IOException e) {
            // If can't read schedule file, return empty set
            logger.warning("Could not read schedule file for validation: " + e.getMessage());
        }
        
        return scheduledDays;
    }
    
    public void saveEnrollmentsByStudent(String filePath, java.util.List<Enrollment> enrollments) throws java.io.IOException {
        // 1. PRE-PROCESS: Read existing file to preserve PASSED/FAILED/INC statuses
        java.util.Map<String, String> existingStatuses = new java.util.LinkedHashMap<>();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Decrypt the existing line to read its contents
                String decryptedLine = Encryption.decrypt(line);
                String[] parts = decryptedLine.split(",");
                if (parts.length >= 7) {
                    String studentID = parts[0].trim();
                    String yearLevel = parts[2].trim();
                    String semester = parts[3].trim(); 
                    String status = parts[4].trim();
                    String academicYear = parts[6].trim();
                    String periodKey = studentID + "|" + yearLevel + "|" + semester + "|" + academicYear;
                    existingStatuses.put(periodKey, status);
                }
            }
        } catch (java.io.IOException e) {
            logger.info("No existing enrollment file found or file is empty.");
        }

        // 2. GROUPING: Group by enrollment period (student + year + sem + ay)
        java.util.Map<String, java.util.List<Enrollment>> enrollmentPeriods = new java.util.LinkedHashMap<>();
        for (Enrollment e : enrollments) {
            String ay = e.getAcademicYear() != null ? e.getAcademicYear() : AcademicUtilities.getAcademicYear();
            String periodKey = e.getStudentID() + "|" + e.getYearLevel() + "|" + e.getSemester() + "|" + ay;
            
            enrollmentPeriods.computeIfAbsent(periodKey, k -> new java.util.ArrayList<>()).add(e);
        }

        // 3. WRITING: Encrypt and save to file
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filePath))) {
            for (java.util.Map.Entry<String, java.util.List<Enrollment>> entry : enrollmentPeriods.entrySet()) {
                java.util.List<Enrollment> periodCourses = entry.getValue();
                if (periodCourses.isEmpty()) continue;

                Enrollment first = periodCourses.get(0);
                String studentID = first.getStudentID();
                String yearLevel = first.getYearLevel();
                String semester = first.getSemester();
                String status = first.getStatus();
                String academicYear = first.getAcademicYear() != null ? first.getAcademicYear() : AcademicUtilities.getAcademicYear();

                // Status Priority Logic
                String lookupKey = studentID + "|" + yearLevel + "|" + compressSemester(semester) + "|" + academicYear;
                if (existingStatuses.containsKey(lookupKey)) {
                    String fileStatus = existingStatuses.get(lookupKey);
                    if (java.util.Arrays.asList("PASSED", "FAILED", "INC").contains(fileStatus)) {
                        status = fileStatus;
                    }
                }
                if ("ENROLLED".equals(status)) {
                    for (Enrollment e : periodCourses) {
                        if (java.util.Arrays.asList("PASSED", "FAILED", "INC").contains(e.getStatus())) {
                            status = e.getStatus();
                            break;
                        }
                    }
                }

                // Build lists for the CSV columns
                java.util.List<String> courseIDs = new java.util.ArrayList<>();
                java.util.List<String> sectionIDs = new java.util.ArrayList<>();
                java.util.List<String> courseStatusPairs = new java.util.ArrayList<>();

                for (Enrollment e : periodCourses) {
                    courseIDs.add(e.getCourseID());
                    sectionIDs.add(e.getSectionID() != null ? e.getSectionID() : "");
                    if (e.getCourseStatuses() != null && !e.getCourseStatuses().isEmpty()) {
                        courseStatusPairs.add(e.getCourseID() + ":" + e.getCourseStatus(e.getCourseID()));
                    }
                }

                // Construct the final plain-text line
                String plainLine = String.join(",",
                    studentID,
                    String.join(";", courseIDs),
                    yearLevel,
                    compressSemester(semester),
                    status,
                    String.join(";", sectionIDs),
                    academicYear,
                    String.join(";", courseStatusPairs)
                );

                // ENCRYPT and WRITE
                writer.println(Encryption.encrypt(plainLine));
            }
        }
    }
}


class MarksheetFileSaver extends BaseFileSaver<Marksheet> {
    
    /**
     * Convert full semester format to abbreviated format for file storage
     * 1st Semester -> 1, 2nd Semester -> 2, Summer -> 3
     */
    private String compressSemester(String full) {
        switch (full) {
            case "1st Semester": return "1";
            case "2nd Semester": return "2";
            case "Summer": return "3";
            default: return full; // Already abbreviated or unknown
        }
    }
    
    @Override
    protected String formatLine(Marksheet m) {
        // Format: ID, StudentID, Semester, YearLevel, Course1, Score1, Course2, Score2, Course3, Score3, Course4, Score4, Course5, Score5, Average
        StringBuilder sb = new StringBuilder();
        
        // Generate ID (e.g., MRK-001)
        sb.append("MRK-").append(String.format("%03d", System.currentTimeMillis() % 1000)).append(",");
        sb.append(m.getStudentID()).append(",");
        sb.append(compressSemester(m.getSemester())).append(",");
        sb.append(m.getSchoolYear() != null ? m.getSchoolYear() : "").append(","); // Add YearLevel field
        
        // Add 5 course-score pairs
        String[] subjects = m.getSubjects();
        double[] marks = m.getMarks();
        for (int i = 0; i < 5; i++) {
            sb.append(subjects[i] != null ? subjects[i] : "").append(",");
            sb.append(marks[i]).append(",");
        }
        
        // Calculate and add average
        double average = 0.0;
        int count = 0;
        for (double mark : marks) {
            if (mark > 0) {
                average += mark;
                count++;
            }
        }
        average = count > 0 ? average / count : 0.0;
        sb.append(String.format("%.2f", average));
        
        return sb.toString();
    }
}