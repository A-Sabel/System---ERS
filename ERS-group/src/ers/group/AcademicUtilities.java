package ers.group;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Locale;

/**
 * Centralized Academic Utilities Class
 * Contains all semester/enrollment/schedule/graduation logic in one place
 */
public class AcademicUtilities { 
    // ACADEMIC CALENDAR METHODS
    public static String getCurrentSemester() {
        String activeSemester = getActiveSemesterFromFile();
        if (activeSemester != null) {
            return activeSemester;
        }
        LocalDate now = LocalDate.now();
        int month = now.getMonth().getValue();
        if (month >= 8 && month <= 12) {
            return "1st Semester";
        } else if (month >= 1 && month <= 5) {
            return "2nd Semester";
        } else { 
            return "Summer";
        }
    }

    public static String getAcademicYear() {
        String activeYear = getActiveYearFromFile();
        if (activeYear != null) {
            return activeYear;
        }
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonth().getValue();
        if (month >= 6) {
            return year + "-" + (year + 1);
        } else {
            return (year - 1) + "-" + year;
        }
    }

    public static void setActiveSemester(String academicYear, String semester, LocalDate startDate, LocalDate endDate) {
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/academic_calendar.txt",
                "src/ers/group/master files/academic_calendar.txt",
                "master files/academic_calendar.txt",
                "academic_calendar.txt"
            };
            File calendarFile = new File(FilePathResolver.resolveFilePath(possiblePaths));
            calendarFile.getParentFile().mkdirs();
            try (PrintWriter pw = new PrintWriter(new FileWriter(calendarFile))) {
                pw.println(academicYear + "," + semester + ",ACTIVE," + 
                        startDate.toString() + "," + endDate.toString());
            }
        } catch (Exception e) {
            ErrorLogger.logError("Failed to set active semester in academic calendar", e);
        }
    }
    
    private static String getActiveSemesterFromFile() {
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/academic_calendar.txt",
                "src/ers/group/master files/academic_calendar.txt",
                "master files/academic_calendar.txt",
                "academic_calendar.txt"
            };
            BufferedReader br = new BufferedReader(new FileReader(
                FilePathResolver.resolveFilePath(possiblePaths)));
            String line = br.readLine();
            br.close();
            if (line != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && "ACTIVE".equals(parts[2])) {
                    return parts[1];
                }
            }
        } catch (Exception e) {
            // File doesn't exist or error - return null to use auto-detection
        }
        return null;
    }
    
    private static String getActiveYearFromFile() {
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/academic_calendar.txt",
                "src/ers/group/master files/academic_calendar.txt",
                "master files/academic_calendar.txt",
                "academic_calendar.txt"
            };
            BufferedReader br = new BufferedReader(new FileReader(
                FilePathResolver.resolveFilePath(possiblePaths)));
            String line = br.readLine();
            br.close();
            if (line != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && "ACTIVE".equals(parts[2])) {
                    return parts[0];
                }
            }
        } catch (Exception e) {
            // File doesn't exist - return null
        }
        return null;
    }
    
    // SEMESTER END PROCESSING
    // 1 -> 1st Semester, 2 -> 2nd Semester, 3 -> Summer
    private static String expandSemester(String abbrev) {
        switch (abbrev) {
            case "1": return "1st Semester";
            case "2": return "2nd Semester";
            case "3": return "Summer";
            default: return abbrev; 
        }
    }

    private static String compressSemester(String full) {
        switch (full) {
            case "1st Semester": return "1";
            case "2nd Semester": return "2";
            case "Summer": return "3";
            default: return full;
        }
    }
    // Process end of semester - update enrollment statuses based on grades
    public static void processEndOfSemester(String semester, String academicYear) {
        Map<String, Map<String, Double>> studentGrades = loadGradesFromMarksheet(semester);
        Set<String> processedStudents = new HashSet<>();
        String enrollmentPath = FilePathResolver.resolveEnrollmentFilePath();
        File inputFile = new File(enrollmentPath);
        File tempFile = new File("temp_enrollment.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String studentID = parts[0];
                    String enrolledSemester = expandSemester(parts[3]);
                    String status = parts[4];
                    // Only process ENROLLED records from specified semester
                    if (enrolledSemester.equals(semester) && "ENROLLED".equals(status)) {
                        processedStudents.add(studentID);
                        String[] courses = parts[1].split(";");
                        boolean allPassed = true;
                        boolean anyFailed = false;
                        boolean hasIncomplete = false;
                        List<String> courseStatusList = new ArrayList<>();
                        for (String courseID : courses) {
                            if (courseID.trim().isEmpty()) continue;
                            Double grade = studentGrades.getOrDefault(studentID, new HashMap<>()).get(courseID);
                            String courseStatus;
                            if (grade == null) {
                                hasIncomplete = true;
                                courseStatus = "INC";
                            } else if (grade == 5.0) {
                                anyFailed = true;
                                allPassed = false;
                                courseStatus = "FAILED";
                            } else if (grade >= 1.0 && grade <= 3.0) {
                                courseStatus = "PASSED";
                            } else {
                                hasIncomplete = true;
                                allPassed = false;
                                courseStatus = "INC";
                            }
                            courseStatusList.add(courseID.trim() + ":" + courseStatus);
                        }
                        // Determine final enrollment status
                        if (hasIncomplete) {
                            parts[4] = "INC";
                        } else if (anyFailed) {
                            parts[4] = "FAILED";
                        } else if (allPassed) {
                            parts[4] = "PASSED";
                        }
                        if (parts.length > 7 && !courseStatusList.isEmpty()) {
                            parts[7] = String.join(";", courseStatusList);
                        }
                    }
                }
                pw.println(String.join(",", parts));
            }
        } catch (Exception e) {
            ErrorLogger.logError("Failed to update student year level in file", e);
        }
        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        }
        for (String studentID : processedStudents) {
            updateStudentCompletedCourses(studentID);
        }
    }
    
    private static Map<String, Map<String, Double>> loadGradesFromMarksheet(String semester) {
        Map<String, Map<String, Double>> result = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(
                FilePathResolver.resolveMarksheetFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 14 && expandSemester(parts[2]).equals(semester)) {
                    String studentID = parts[1];
                    result.putIfAbsent(studentID, new HashMap<>());
                    for (int i = 0; i < 5; i++) {
                        String course = parts[4 + i * 2];
                        String scoreStr = parts[5 + i * 2];
                        if (!scoreStr.equals("PENDING") && !course.equals("NONE") && !scoreStr.isEmpty()) {
                            try {
                                double score = Double.parseDouble(scoreStr);
                                result.get(studentID).put(course, score);
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    // PREREQUISITE CHECKING 
    private static Map<String, List<String>> prerequisitesCache = null;
    
    public static void loadPrerequisites() {
        prerequisitesCache = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FilePathResolver.resolveCourseSubjectFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String courseID = parts[0].trim();
                    String prereqStr = parts[5].trim();
                    if (!prereqStr.equalsIgnoreCase("NONE") && !prereqStr.isEmpty()) {
                        prerequisitesCache.put(courseID, Arrays.asList(prereqStr.split(";")));
                    } else {
                        prerequisitesCache.put(courseID, new ArrayList<>());
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    // Get list of missing prerequisites for a course
    public static List<String> getMissingPrerequisites(String studentID, String courseID) {
        if (prerequisitesCache == null) loadPrerequisites();
        List<String> required = prerequisitesCache.get(courseID);
        if (required == null || required.isEmpty()) return new ArrayList<>(); // CS205 case (NONE)
        
        Set<String> passed = getPassedCourses(studentID);
        List<String> missing = new ArrayList<>();
        
        for (String prereq : required) {
            if (!passed.contains(prereq.trim())) {
                missing.add(prereq.trim());
            }
        }
        return missing;
    }
    
    private static Set<String> getPassedCourses(String studentID) {
        Set<String> passed = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FilePathResolver.resolveEnrollmentFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // We ignore parts[4] ("FAILED") and target parts[7]
                if (parts.length > 7 && parts[0].trim().equals(studentID)) {
                    String detailedStatus = parts[7].trim();
                    String[] entries = detailedStatus.split(";");
                    for (String entry : entries) {
                        if (entry.contains(":")) {
                            String[] pair = entry.split(":");
                            String cID = pair[0].trim();
                            String status = pair[1].trim();
                            if ("PASSED".equalsIgnoreCase(status)) {
                                passed.add(cID);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return passed;
    }

    // RETAKE MANAGEMENT
    public static boolean canRetakeCourse(String studentID, String courseID) {
        int retakeCount = getRetakeCount(studentID, courseID);
        if (retakeCount >= 3) {
            return false;
        }
        String lastStatus = getLastCourseStatus(studentID, courseID);
        return "FAILED".equals(lastStatus) || "INC".equals(lastStatus);
    }
    
    public static int getRetakeCount(String studentID, String courseID) {
        int count = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(
                FilePathResolver.resolveEnrollmentFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(studentID)) {
                    String[] courses = parts[1].split(";");
                    for (String course : courses) {
                        if (course.trim().equals(courseID)) {
                            count++;
                            break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return count > 0 ? count - 1 : 0;
    }
    
    public static String getLastCourseStatus(String studentID, String courseID) {
        String status = null;
        try (BufferedReader br = new BufferedReader(new FileReader(
                FilePathResolver.resolveEnrollmentFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(studentID)) {
                    String[] courses = parts[1].split(";");
                    for (String course : courses) {
                        if (course.trim().equals(courseID)) {
                            status = parts[4];
                            break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return status;
    }

    public static List<String> getRecommendedSummerCourses(String studentID) {
        List<String> recommended = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(
                FilePathResolver.resolveEnrollmentFilePath()))) {
            String line;
            // Track latest status for each course across all enrollments
            Map<String, String> latestCourseStatus = new HashMap<>();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && parts[0].equals(studentID)) {
                    String detailedStatuses = parts[7];
                    if (detailedStatuses != null && !detailedStatuses.isEmpty()) {
                        String[] statusPairs = detailedStatuses.split(";");
                        for (String pair : statusPairs) {
                            String[] courseStat = pair.split(":");
                            if (courseStat.length == 2) {
                                String courseID = courseStat[0].trim();
                                String status = courseStat[1].trim();
                                latestCourseStatus.put(courseID, status);
                            }
                        }
                    }
                }
            }
            // Only recommend courses where LATEST status is FAILED/INC/DROPPED
            for (Map.Entry<String, String> entry : latestCourseStatus.entrySet()) {
                String status = entry.getValue();
                if ("FAILED".equals(status) || "INC".equals(status) || "DROPPED".equals(status)) {
                    recommended.add(entry.getKey());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recommended;
    }
    
    // GRADUATION PROCESSING 
    public static boolean isEligibleForGraduation(String studentID) {
        Map<String, Boolean> reqs = getGraduationRequirements(studentID);
        return reqs.values().stream().allMatch(v -> v);
    }

    public static String getLatinHonors(double gwa) {
        if (gwa >= 1.00 && gwa <= 1.20) {
            return "SUMMA CUM LAUDE";
        } else if (gwa >= 1.21 && gwa <= 1.45) {
            return "MAGNA CUM LAUDE";
        } else if (gwa >= 1.46 && gwa <= 1.75) {
            return "CUM LAUDE";
        }
        return "NONE";
    }
    
    public static boolean processGraduation(String studentID) {
        if (!isEligibleForGraduation(studentID)) {
            return false;
        }
        String studentName = getStudentName(studentID);
        double gwa = getStudentGWA(studentID);
        // Students with any history of FAILED/DROPPED/INC cannot receive Latin Honors
        String honors;
        if (hasEverFailedCourses(studentID)) {
            honors = "NONE";
        } else {
            honors = getLatinHonors(gwa);
        }
        String graduationDate = LocalDate.now().toString();
        String academicYear = getAcademicYear();
        try (PrintWriter pw = new PrintWriter(new FileWriter(
                FilePathResolver.resolveFilePath(new String[] {
                    "ERS-group/src/ers/group/master files/graduates.txt",
                    "src/ers/group/master files/graduates.txt",
                    "master files/graduates.txt",
                    "graduates.txt"
                }), true))) {
            pw.println(studentID + "," + studentName + "," + graduationDate + "," + 
                    String.format("%.2f", gwa) + "," + honors + "," + academicYear + ",Bachelor");
        } catch (Exception e) {
            ErrorLogger.logError("Failed to write to graduates.txt for student: " + studentID, e);
            return false;
        }
        // Update student status to "Graduate" in student.txt
        try {
            String studentPath = FilePathResolver.resolveStudentFilePath();
            StudentFileLoader loader = new StudentFileLoader();
            loader.load(studentPath);
            List<Student> allStudents = new ArrayList<>(loader.getAllStudents());
            // Find and update the graduated student
            boolean studentFound = false;
            for (Student student : allStudents) {
                if (student.getStudentID().equals(studentID)) {
                    student.setStatus("Graduate");
                    studentFound = true;
                    break;
                }
            }
            if (!studentFound) {
                System.err.println("Warning: Student " + studentID + " not found for status update");
                return false;
            }
            StudentFileSaver saver = new StudentFileSaver();
            saver.save(studentPath, allStudents);
        } catch (Exception e) {
            ErrorLogger.logError("Failed to update student status to Graduate for: " + studentID, e);
            return false;
        }
        return true;
    }
    
    private static boolean hasIncompleteRequirements(String studentID) {
        try (BufferedReader br = new BufferedReader(new FileReader(
                FilePathResolver.resolveEnrollmentFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(studentID)) {
                    String status = parts[4];
                    if ("ENROLLED".equals(status) || "FAILED".equals(status) || "INC".equals(status)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private static int getCourseUnits(String courseCode) {
        try {
            String coursePath = FilePathResolver.resolveFilePath(new String[] {
                "ERS-group/src/ers/group/master files/courseSubject.txt",
                "src/ers/group/master files/courseSubject.txt",
                "master files/courseSubject.txt",
                "courseSubject.txt"
            });
            CourseSubjectFileLoader loader = new CourseSubjectFileLoader();
            loader.load(coursePath);
            Map<String, CourseSubject> courseMap = loader.getSubjectMap();
            CourseSubject course = courseMap.get(courseCode);
            if (course != null) {
                return course.getUnits();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 3;
    }
    
    private static int getTotalUnitsEarned(String studentID) {
        int totalUnits = 0;
        Map<String, CourseSubject> courseMap = new HashMap<>();
        try {
            String coursePath = FilePathResolver.resolveFilePath(new String[] {
                "ERS-group/src/ers/group/master files/courseSubject.txt",
                "src/ers/group/master files/courseSubject.txt",
                "master files/courseSubject.txt",
                "courseSubject.txt"
            });
            CourseSubjectFileLoader loader = new CourseSubjectFileLoader();
            loader.load(coursePath);
            courseMap = loader.getSubjectMap();
        } catch (Exception e) {
            ErrorLogger.logError("Failed to load course subjects for unit calculation", e);
        }
        // Count units from individual PASSED courses (not enrollment status)
        Map<String, String> latestCourseStatus = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(
                FilePathResolver.resolveEnrollmentFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && parts[0].equals(studentID)) {
                    String detailedStatuses = parts[7];
                    if (detailedStatuses != null && !detailedStatuses.isEmpty()) {
                        String[] statusPairs = detailedStatuses.split(";");
                        for (String pair : statusPairs) {
                            String[] courseStat = pair.split(":");
                            if (courseStat.length == 2) {
                                String courseID = courseStat[0].trim();
                                String status = courseStat[1].trim();
                                latestCourseStatus.put(courseID, status);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            ErrorLogger.logError("Failed to calculate total units for student: " + studentID, e);
        }
        // Count units only for courses with latest status = PASSED
        for (Map.Entry<String, String> entry : latestCourseStatus.entrySet()) {
            if ("PASSED".equals(entry.getValue())) {
                String courseCode = entry.getKey();
                CourseSubject course = courseMap.get(courseCode);
                if (course != null) {
                    totalUnits += course.getUnits();
                } else {
                    // Fallback to 3 units if course not found in courseSubject.txt
                    totalUnits += 3;
                }
            }
        }
        
        return totalUnits;
    }
    
    private static double getStudentGWA(String studentID) {
        try (BufferedReader br = new BufferedReader(new FileReader(
                FilePathResolver.resolveStudentFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 9 && parts[0].equals(studentID)) {
                    return Double.parseDouble(parts[8]);
                }
            }
        } catch (Exception e) {
            ErrorLogger.logError("Failed to get student GWA for: " + studentID, e);
        }
        return 5.0;
    }
    
    private static String getStudentName(String studentID) {
        try (BufferedReader br = new BufferedReader(new FileReader(
                FilePathResolver.resolveStudentFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(studentID)) {
                    return parts[1];
                }
            }
        } catch (Exception ignored) {}
        return "Unknown";
    }
    
    // STUDENT PROMOTION SYSTEM (2-YEAR PROGRAM)
    // Promote student to next year level (2-year program: 1st → 2nd only)
    public static boolean promoteToNextYearLevel(String studentID) {
        try {
            String studentPath = FilePathResolver.resolveStudentFilePath();
            File inputFile = new File(studentPath);
            File tempFile = new File("temp_student_promote.txt");
            boolean promoted = false;
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 16 && parts[0].equals(studentID)) {
                    String currentYearLevel = parts[4];
                    String nextYearLevel = getNextYearLevel(currentYearLevel);
                    if (nextYearLevel != null) {
                        parts[4] = nextYearLevel;
                        pw.println(String.join(",", parts));
                        promoted = true;
                        System.out.println("Promoted " + studentID + " from " + currentYearLevel + " to " + nextYearLevel);
                    } else {
                        pw.println(line);
                        System.out.println("Student " + studentID + " is at maximum year level: " + currentYearLevel);
                    }
                } else {
                    pw.println(line);
                }
            }
            br.close();
            pw.close();
            if (promoted) {
                inputFile.delete();
                tempFile.renameTo(inputFile);
            } else {
                tempFile.delete();
            }
            return promoted;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static String getNextYearLevel(String currentYearLevel) {
        switch (currentYearLevel) {
            case "1st Year":
                return "2nd Year";
            case "2nd Year":
                return null;
            default:
                return null;
        }
    }
    
    private static String convertSemesterToNumber(String semester) {
        if (semester == null) return "1";
        switch (semester) {
            case "1st Semester": return "1";
            case "2nd Semester": return "2";
            case "Summer": return "3";
            default: return "1";
        }
    }
    
    public static List<String> bulkPromoteYearLevel(String currentYearLevel) {
        List<String> promotedStudents = new ArrayList<>();
        try {
            String studentPath = FilePathResolver.resolveStudentFilePath();
            BufferedReader br = new BufferedReader(new FileReader(studentPath));
            String line;
            
            List<String> eligibleStudents = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[4].equals(currentYearLevel)) {
                    eligibleStudents.add(parts[0]);
                }
            }
            br.close();
            for (String studentID : eligibleStudents) {
                if (hasCompletedAllSemestersInYear(studentID, currentYearLevel)) {
                    if (promoteToNextYearLevel(studentID)) {
                        promotedStudents.add(studentID);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return promotedStudents;
    }
    
    /**
     * Check if student completed all semesters in year (1st & 2nd semester)
     */
    private static boolean hasCompletedAllSemestersInYear(String studentID, String yearLevel) {
        try {
            String enrollmentPath = FilePathResolver.resolveEnrollmentFilePath();
            // Collect all required courses for this year level (semester 1 & 2)
            Set<String> requiredCourses = new HashSet<>();
            BufferedReader br = new BufferedReader(new FileReader(enrollmentPath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String sid = parts[0];
                    String courseList = parts[1];
                    String yr = parts[2];
                    String sem = parts[3];
                    // Collect courses from semester 1 and 2 only (not summer)
                    if (sid.equals(studentID) && yr.equals(yearLevel) && (sem.equals("1") || sem.equals("2"))) {
                        String[] courses = courseList.split(";");
                        for (String course : courses) {
                            requiredCourses.add(course.trim());
                        }
                    }
                }
            }
            br.close();
            if (requiredCourses.isEmpty()) {
                return false;
            }
            // Check if each required course has been passed (in any enrollment)
            Map<String, String> courseStatusMap = new HashMap<>();
            br = new BufferedReader(new FileReader(enrollmentPath));
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && parts[0].equals(studentID)) {
                    String detailedStatuses = parts[7];
                    if (detailedStatuses != null && !detailedStatuses.isEmpty()) {
                        String[] statusPairs = detailedStatuses.split(";");
                        for (String pair : statusPairs) {
                            String[] courseStat = pair.split(":");
                            if (courseStat.length == 2) {
                                String courseID = courseStat[0].trim();
                                String status = courseStat[1].trim();
                                courseStatusMap.put(courseID, status);
                            }
                        }
                    }
                }
            }
            br.close();
            // Verify all required courses are PASSED
            for (String course : requiredCourses) {
                String status = courseStatusMap.get(course);
                if (status == null || !"PASSED".equals(status)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Check if student has no incomplete requirements (all courses PASSED)
    private static boolean hasNoIncompleteRequirements(String studentID) {
        try {
            String enrollmentPath = FilePathResolver.resolveEnrollmentFilePath();
            BufferedReader br = new BufferedReader(new FileReader(enrollmentPath));
            String line;
            Map<String, String> courseStatusMap = new HashMap<>();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && parts[0].equals(studentID)) {
                    String detailedStatuses = parts[7];
                    if (detailedStatuses != null && !detailedStatuses.isEmpty()) {
                        String[] statusPairs = detailedStatuses.split(";");
                        for (String pair : statusPairs) {
                            String[] courseStat = pair.split(":");
                            if (courseStat.length == 2) {
                                String courseID = courseStat[0].trim();
                                String status = courseStat[1].trim();
                                courseStatusMap.put(courseID, status);
                            }
                        }
                    }
                }
            }
            br.close();
            // Check if any course's LATEST status is not PASSED
            for (String status : courseStatusMap.values()) {
                if (!"PASSED".equals(status)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if student EVER had FAILED/DROPPED/INC courses (even if retaken)
     * Used to disqualify from Latin Honors while still allowing graduation
     */
    private static boolean hasEverFailedCourses(String studentID) {
        try {
            String enrollmentPath = FilePathResolver.resolveEnrollmentFilePath();
            BufferedReader br = new BufferedReader(new FileReader(enrollmentPath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && parts[0].equals(studentID)) {
                    String detailedStatuses = parts[7];
                    if (detailedStatuses != null && !detailedStatuses.isEmpty()) {
                        String[] statusPairs = detailedStatuses.split(";");
                        for (String pair : statusPairs) {
                            String[] courseStat = pair.split(":");
                            if (courseStat.length == 2) {
                                String status = courseStat[1].trim();
                                if ("FAILED".equals(status) || "DROPPED".equals(status) || "INC".equals(status)) {
                                    br.close();
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            br.close();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get graduation requirements for 2-year program
    public static Map<String, Boolean> getGraduationRequirements(String studentID) {
        Map<String, Boolean> requirements = new LinkedHashMap<>();
        requirements.put("Is 2nd Year Student", is2ndYearStudent(studentID));
        requirements.put("All Courses Passed", hasNoIncompleteRequirements(studentID));
        int totalUnits = getTotalUnitsEarned(studentID);
        requirements.put("Total Units (≥60)", totalUnits >= 60); // 2-year program ~60 units
        double gwa = getStudentGWA(studentID);
        requirements.put("GWA Requirement (≤3.0)", gwa > 0 && gwa <= 3.0);
        return requirements;
    }
    
    // Check if student is in 2nd year
    private static boolean is2ndYearStudent(String studentID) {
        try {
            String studentPath = FilePathResolver.resolveStudentFilePath();
            BufferedReader br = new BufferedReader(new FileReader(studentPath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(studentID)) {
                    br.close();
                    return "2nd Year".equals(parts[4]);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Generate promotion report for 2-year program
    public static String generatePromotionReport(String yearLevel) {
        StringBuilder report = new StringBuilder();
        report.append("===== PROMOTION REPORT FOR ").append(yearLevel).append(" =====\n\n");
        List<String> eligible = new ArrayList<>();
        List<String> ineligible = new ArrayList<>();
        try {
            String studentPath = FilePathResolver.resolveStudentFilePath();
            BufferedReader br = new BufferedReader(new FileReader(studentPath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[4].equals(yearLevel)) {
                    String studentID = parts[0];
                    String studentName = parts[1];
                    if (hasCompletedAllSemestersInYear(studentID, yearLevel)) {
                        eligible.add(studentID + " - " + studentName);
                    } else {
                        ineligible.add(studentID + " - " + studentName);
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        report.append("✓ ELIGIBLE FOR PROMOTION (").append(eligible.size()).append("):\n");
        for (String student : eligible) {
            report.append("  ").append(student).append("\n");
        }
        report.append("\n✗ NOT ELIGIBLE (").append(ineligible.size()).append("):\n");
        for (String student : ineligible) {
            report.append("  ").append(student).append("\n");
        }
        return report.toString();
    }
    
    // Generate graduation candidates report (2nd Year only)
    public static String generateGraduationReport() {
        StringBuilder report = new StringBuilder();
        report.append("===== GRADUATION CANDIDATES REPORT =====\n\n");
        List<String> summa = new ArrayList<>();
        List<String> magna = new ArrayList<>();
        List<String> cumLaude = new ArrayList<>();
        List<String> regular = new ArrayList<>();
        List<String> notEligible = new ArrayList<>();
        try {
            String studentPath = FilePathResolver.resolveStudentFilePath();
            BufferedReader br = new BufferedReader(new FileReader(studentPath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 9) {
                    String studentID = parts[0];
                    String studentName = parts[1];
                    String yearLevel = parts[4];
                    if ("2nd Year".equals(yearLevel)) {
                        Map<String, Boolean> reqs = getGraduationRequirements(studentID);
                        boolean eligible = reqs.values().stream().allMatch(v -> v);
                        if (eligible) {
                            double gwa = getStudentGWA(studentID);
                            String entry = studentID + " - " + studentName + " (GWA: " + String.format("%.2f", gwa) + ")"; 
                            // Students with any history of FAILED/DROPPED/INC cannot get Latin Honors
                            if (hasEverFailedCourses(studentID)) {
                                regular.add(entry + " [Retake]");
                            } else {
                                String honors = getLatinHonors(gwa);
                                switch (honors) {
                                    case "SUMMA CUM LAUDE":
                                        summa.add(entry);
                                        break;
                                    case "MAGNA CUM LAUDE":
                                        magna.add(entry);
                                        break;
                                    case "CUM LAUDE":
                                        cumLaude.add(entry);
                                        break;
                                    default:
                                        regular.add(entry);
                                }
                            }
                        } else {
                            notEligible.add(studentID + " - " + studentName);
                        }
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        report.append("🏆 SUMMA CUM LAUDE (").append(summa.size()).append("):\n");
        for (String s : summa) report.append("  ").append(s).append("\n");
        
        report.append("\n🥈 MAGNA CUM LAUDE (").append(magna.size()).append("):\n");
        for (String s : magna) report.append("  ").append(s).append("\n");
        
        report.append("\n🥉 CUM LAUDE (").append(cumLaude.size()).append("):\n");
        for (String s : cumLaude) report.append("  ").append(s).append("\n");
        
        report.append("\n✓ REGULAR GRADUATES (").append(regular.size()).append("):\n");
        for (String s : regular) report.append("  ").append(s).append("\n");
        
        report.append("\n✗ NOT ELIGIBLE (").append(notEligible.size()).append("):\n");
        for (String s : notEligible) report.append("  ").append(s).append("\n");
        
        report.append("\nTOTAL ELIGIBLE: ").append(summa.size() + magna.size() + cumLaude.size() + regular.size());
        
        return report.toString();
    }
    
    // Process graduation for all eligible 2nd year students
    public static List<String> bulkProcessGraduation() {
        List<String> graduatedStudents = new ArrayList<>();
        try {
            String studentPath = FilePathResolver.resolveStudentFilePath();
            BufferedReader br = new BufferedReader(new FileReader(studentPath));
            String line;
            List<String> eligibleStudents = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 9) {
                    String studentID = parts[0];
                    String studentName = parts[1];
                    String yearLevel = parts[4];
                    if ("2nd Year".equals(yearLevel)) {
                        Map<String, Boolean> reqs = getGraduationRequirements(studentID);
                        boolean eligible = reqs.values().stream().allMatch(v -> v);
                        if (eligible) {
                            eligibleStudents.add(studentID);
                            graduatedStudents.add(studentID + " - " + studentName);
                        }
                    }
                }
            }
            br.close();
            // Process graduation for each eligible student
            for (String studentID : eligibleStudents) {
                processGraduation(studentID.split(" - ")[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return graduatedStudents;
    }
    
    // GWA CALCULATION METHODS
    /**
     * Calculate cumulative GWA for a student across all completed semesters
     * Uses weighted average based on number of courses per semester
     * @param studentID The student's ID
     * @return Cumulative GWA or 0.0 if no marksheets found
     */
    public static double calculateCumulativeGWA(String studentID) {
        List<Marksheet> allMarksheets = new ArrayList<>();
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/marksheet.txt",
                "src/ers/group/master files/marksheet.txt",
                "master files/marksheet.txt",
                "marksheet.txt"
            };
            String resolvedPath = FilePathResolver.resolveFilePath(possiblePaths);
            MarksheetFileLoader loader = new MarksheetFileLoader();
            loader.load(resolvedPath);
            allMarksheets.addAll(loader.getAllMarksheets());
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
        // Filter marksheets for this student
        List<Marksheet> studentMarksheets = new ArrayList<>();
        for (Marksheet m : allMarksheets) {
            if (m.getStudentID().equals(studentID)) {
                studentMarksheets.add(m);
            }
        }
        if (studentMarksheets.isEmpty()) {
            return 0.0;
        }
        // Calculate weighted average
        double totalWeightedGWA = 0.0;
        int totalCourses = 0;
        for (Marksheet m : studentMarksheets) {
            // Calculate GWA for this marksheet
            double[] marks = m.getMarks();
            double sum = 0.0;
            int validMarks = 0;
            for (double mark : marks) {
                if (mark > 0.0) {
                    sum += mark;
                    validMarks++;
                }
            }
            if (validMarks > 0) {
                double semesterGWA = sum / validMarks;
                totalWeightedGWA += semesterGWA * validMarks;
                totalCourses += validMarks;
            }
        }
        if (totalCourses == 0) {
            return 0.0;
        }
        double cumulativeGWA = totalWeightedGWA / totalCourses;
        return Math.round(cumulativeGWA * 100.0) / 100.0;
    }
    
    /**
     * Determine Latin honor based on cumulative GWA
     * Summa Cum Laude: 1.00 - 1.20
     * Magna Cum Laude: 1.21 - 1.45
     * Cum Laude: 1.46 - 1.75
     * @param cumulativeGWA The student's cumulative GWA
     * @return Latin honor designation or empty string
     */
    public static String determineLatinHonor(double cumulativeGWA) {
        if (cumulativeGWA >= 1.00 && cumulativeGWA <= 1.20) {
            return "Summa Cum Laude";
        } else if (cumulativeGWA >= 1.21 && cumulativeGWA <= 1.45) {
            return "Magna Cum Laude";
        } else if (cumulativeGWA >= 1.46 && cumulativeGWA <= 1.75) {
            return "Cum Laude";
        }
        return "";
    }
    
    /**
     * Update course statuses for a student's enrollment based on marksheet grades
     * Rules:
     *  - Grade 1.0-3.0: PASSED
     *  - Grade 5.0: FAILED
     *  - No grade or pending: INC
     * @param studentID The student's ID
     * @param semester The semester (e.g., "1st Semester")
     * @param yearLevel The year level (e.g., "1st Year")
     */
    public static void updateCourseStatuses(String studentID, String semester, String yearLevel) {
        try {
            // Load marksheet for this student/semester/year
            List<Marksheet> allMarksheets = new ArrayList<>();
            String[] marksheetPaths = {
                "ERS-group/src/ers/group/master files/marksheet.txt",
                "src/ers/group/master files/marksheet.txt",
                "master files/marksheet.txt",
                "marksheet.txt"
            };
            String resolvedMarksheetPath = FilePathResolver.resolveFilePath(marksheetPaths);
            MarksheetFileLoader marksheetLoader = new MarksheetFileLoader();
            marksheetLoader.load(resolvedMarksheetPath);
            allMarksheets.addAll(marksheetLoader.getAllMarksheets());
            // Find matching marksheet
            Marksheet matchingMarksheet = null;
            for (Marksheet m : allMarksheets) {
                if (m.getStudentID().equals(studentID) && 
                    m.getSemester().equals(semester) && 
                    m.getSchoolYear().equals(yearLevel)) {
                    matchingMarksheet = m;
                    break;
                }
            }
            if (matchingMarksheet == null) {
                return;
            }
            List<Enrollment> allEnrollments = new ArrayList<>();
            String[] enrollmentPaths = {
                "ERS-group/src/ers/group/master files/enrollment.txt",
                "src/ers/group/master files/enrollment.txt",
                "master files/enrollment.txt",
                "enrollment.txt"
            };
            String resolvedEnrollmentPath = FilePathResolver.resolveFilePath(enrollmentPaths);
            EnrollmentFileLoader enrollmentLoader = new EnrollmentFileLoader();
            enrollmentLoader.load(resolvedEnrollmentPath);
            allEnrollments.addAll(enrollmentLoader.getAllEnrollments());
            // Create a map from the marksheet's subjects and marks arrays
            String[] subjects = matchingMarksheet.getSubjects();
            double[] marks = matchingMarksheet.getMarks();
            Map<String, Double> courseGrades = new HashMap<>();
            for (int i = 0; i < subjects.length && i < marks.length; i++) {
                if (subjects[i] != null && !subjects[i].isEmpty() && marks[i] > 0.0) {
                    courseGrades.put(subjects[i], marks[i]);
                }
            }
            boolean updated = false;
            for (Enrollment e : allEnrollments) {
                if (e.getStudentID().equals(studentID) && 
                    e.getSemester().equals(semester) && 
                    e.getYearLevel().equals(yearLevel)) {
                    String courseID = e.getCourseID();
                    if (courseGrades.containsKey(courseID)) {
                        double grade = courseGrades.get(courseID);
                        e.updateCourseStatusFromGrade(courseID, grade);
                        updated = true;
                    }
                }
            }
            if (updated) {
                EnrollmentFileSaver saver = new EnrollmentFileSaver();
                saver.saveEnrollmentsByStudent(resolvedEnrollmentPath, allEnrollments);
            }
            updateStudentCompletedCourses(studentID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Update a student's completed courses set and cumulative GWA
     * @param studentID The student's ID
     */
    public static void updateStudentCompletedCourses(String studentID) {
        try {
            List<Enrollment> allEnrollments = new ArrayList<>();
            String[] enrollmentPaths = {
                "ERS-group/src/ers/group/master files/enrollment.txt",
                "src/ers/group/master files/enrollment.txt",
                "master files/enrollment.txt",
                "enrollment.txt"
            };
            String resolvedEnrollmentPath = FilePathResolver.resolveFilePath(enrollmentPaths);
            EnrollmentFileLoader enrollmentLoader = new EnrollmentFileLoader();
            enrollmentLoader.load(resolvedEnrollmentPath);
            allEnrollments.addAll(enrollmentLoader.getAllEnrollments());
            List<Student> allStudents = new ArrayList<>();
            String[] studentPaths = {
                "ERS-group/src/ers/group/master files/student.txt",
                "src/ers/group/master files/student.txt",
                "master files/student.txt",
                "student.txt"
            };
            String resolvedStudentPath = FilePathResolver.resolveFilePath(studentPaths);
            StudentFileLoader studentLoader = new StudentFileLoader();
            studentLoader.load(resolvedStudentPath);
            allStudents.addAll(studentLoader.getAllStudents());
            // Find student
            Student student = null;
            for (Student s : allStudents) {
                if (s.getStudentID().equals(studentID)) {
                    student = s;
                    break;
                }
            }
            if (student == null) {
                return;
            }
            // Collect all PASSED courses
            Set<String> completedCourses = new HashSet<>();
            for (Enrollment e : allEnrollments) {
                if (e.getStudentID().equals(studentID)) {
                    String courseStatus = e.getCourseStatus(e.getCourseID());
                    if ("PASSED".equals(courseStatus)) {
                        completedCourses.add(e.getCourseID());
                    }
                }
            }
            // Update student
            student.setCompletedCourses(completedCourses);
            // Calculate and set cumulative GWA
            double cumulativeGWA = calculateCumulativeGWA(studentID);
            student.setCumulativeGWA(cumulativeGWA);
            // Determine and set Latin honor
            String latinHonor = determineLatinHonor(cumulativeGWA);
            student.setLatinHonor(latinHonor);
            // Save updated students
            StudentFileSaver saver = new StudentFileSaver();
            saver.save(resolvedStudentPath, allStudents);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ─── CONFLICT DETECTION REPORTING ───────────────────────────────────────────

    /**
     * Generates a slot-specific conflict report for a proposed time slot.
     * Identifies exactly which currently-assigned class is blocking the teacher or room.
     *
     * @param existingSchedules All already-assigned schedules.
     * @param day               Proposed day (e.g. "Monday").
     * @param startTime         Proposed start time (e.g. "8:00 AM").
     * @param endTime           Proposed end time.
     * @param proposedTeacher   Teacher to check ("TBA" to skip).
     * @param proposedRoom      Room to check ("TBA" to skip).
     * @param proposedSection   Section being scheduled (used in the report header).
     * @return A formatted conflict detail string, or null if no overlap was found.
     */
    public static String buildConflictReport(
            List<Schedule> existingSchedules,
            String day, String startTime, String endTime,
            String proposedTeacher, String proposedRoom, String proposedSection) {
        if (existingSchedules == null || day == null) return null;
        List<String> lines = new ArrayList<>();
        for (Schedule s : existingSchedules) {
            if (!s.getDay().equals(day)) continue;
            if (!timesOverlap(s.getStartTime(), s.getEndTime(), startTime, endTime)) continue;
            boolean teacherMatch = proposedTeacher != null && !proposedTeacher.equals("TBA")
                    && proposedTeacher.equals(s.getTeacherName());
            boolean roomMatch = proposedRoom != null && !proposedRoom.equals("TBA")
                    && proposedRoom.equals(s.getRoom());
            if (teacherMatch) {
                lines.add("  ► TEACHER: \"" + proposedTeacher + "\" is already teaching \""
                        + s.getCourseID() + "\" on " + day
                        + " " + s.getStartTime() + "–" + s.getEndTime()
                        + " (Room: " + s.getRoom() + ")");
            }
            if (roomMatch) {
                lines.add("  ► ROOM: \"" + proposedRoom + "\" is occupied by \""
                        + s.getCourseID() + "\" on " + day
                        + " " + s.getStartTime() + "–" + s.getEndTime()
                        + " (Teacher: " + s.getTeacherName() + ")");
            }
        }
        if (lines.isEmpty()) return null;
        StringBuilder sb = new StringBuilder();
        sb.append("Conflict for \"").append(proposedSection).append("\" on ")
          .append(day).append(" ").append(startTime).append("–").append(endTime).append(":\n");
        for (String line : lines) sb.append(line).append("\n");
        sb.append("  → Please assign a different teacher, room, or time slot.");
        return sb.toString();
    }

    /**
     * Generates a summary conflict report listing all teachers and rooms already
     * booked in the given schedule list. Useful when no specific slot is known.
     *
     * @param existingSchedules Schedules already assigned to the section or context.
     * @param proposedSection   Section name for the report header.
     * @return A formatted multi-line summary string.
     */
    public static String buildConflictReport(List<Schedule> existingSchedules, String proposedSection) {
        if (existingSchedules == null || existingSchedules.isEmpty()) return null;
        Map<String, List<String>> teacherSlots = new LinkedHashMap<>();
        Map<String, List<String>> roomSlots    = new LinkedHashMap<>();
        for (Schedule s : existingSchedules) {
            String slot = s.getDay() + " " + s.getStartTime() + "–" + s.getEndTime()
                        + " [" + s.getCourseID() + "]";
            if (s.getTeacherName() != null && !s.getTeacherName().equals("TBA"))
                teacherSlots.computeIfAbsent(s.getTeacherName(), k -> new ArrayList<>()).add(slot);
            if (s.getRoom() != null && !s.getRoom().equals("TBA"))
                roomSlots.computeIfAbsent(s.getRoom(), k -> new ArrayList<>()).add(slot);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Conflict Report for \"").append(proposedSection).append("\":\n");
        if (!teacherSlots.isEmpty()) {
            sb.append("  Currently Booked Teachers:\n");
            for (Map.Entry<String, List<String>> e : teacherSlots.entrySet())
                sb.append("    ► ").append(e.getKey()).append(": ")
                  .append(String.join(" | ", e.getValue())).append("\n");
        }
        if (!roomSlots.isEmpty()) {
            sb.append("  Currently Occupied Rooms:\n");
            for (Map.Entry<String, List<String>> e : roomSlots.entrySet())
                sb.append("    ► ").append(e.getKey()).append(": ")
                  .append(String.join(" | ", e.getValue())).append("\n");
        }
        sb.append("  → Assign a different teacher, room, or contact the admin.");
        return sb.toString();
    }

    /** Time overlap check: two intervals overlap when start1 < end2 AND start2 < end1. */
    private static boolean timesOverlap(String start1, String end1, String start2, String end2) {
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("h:mm a", Locale.US);
            LocalTime s1 = LocalTime.parse(start1, fmt);
            LocalTime e1 = LocalTime.parse(end1, fmt);
            LocalTime s2 = LocalTime.parse(start2, fmt);
            LocalTime e2 = LocalTime.parse(end2, fmt);
            return s1.isBefore(e2) && s2.isBefore(e1);
        } catch (Exception ignored) {
            return false;
        }
    }
}
