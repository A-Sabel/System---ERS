package ers.group;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Centralized Academic Utilities Class
 * Contains all semester/enrollment/graduation logic in one place
 */
public class AcademicUtilities {
    
    // ==================== ACADEMIC CALENDAR METHODS ====================
    
    /**
     * Get current semester based on date or academic_calendar.txt
     * @return "1st Semester", "2nd Semester", or "Summer"
     */
    public static String getCurrentSemester() {
        String activeSemester = getActiveSemesterFromFile();
        if (activeSemester != null) {
            return activeSemester;
        }
        
        // Auto-detect based on date
        LocalDate now = LocalDate.now();
        int month = now.getMonth().getValue();
        
        if (month >= 8 && month <= 12) {
            return "1st Semester";
        } else if (month >= 1 && month <= 5) {
            return "2nd Semester";
        } else { // June-July
            return "Summer";
        }
    }
    
    /**
     * Get academic year (e.g., "2024-2025")
     */
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
    
    /**
     * Get next semester after current one
     */
    public static String getNextSemester() {
        String current = getCurrentSemester();
        switch (current) {
            case "1st Semester": return "2nd Semester";
            case "2nd Semester": return "Summer";
            case "Summer": return "1st Semester";
            default: return "1st Semester";
        }
    }
    
    /**
     * Get maximum units allowed for semester
     */
    public static int getMaxUnitsAllowed() {
        String semester = getCurrentSemester();
        return "Summer".equals(semester) ? 9 : 24;
    }
    
    /**
     * Set active semester manually
     */
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
            e.printStackTrace();
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
    
    // ==================== SEMESTER END PROCESSING ====================
    
    /**
     * Convert abbreviated semester format to full format
     * 1 -> 1st Semester, 2 -> 2nd Semester, 3 -> Summer
     */
    private static String expandSemester(String abbrev) {
        switch (abbrev) {
            case "1": return "1st Semester";
            case "2": return "2nd Semester";
            case "3": return "Summer";
            default: return abbrev; // Already in full format
        }
    }
    
    /**
     * Convert full semester format to abbreviated format
     * 1st Semester -> 1, 2nd Semester -> 2, Summer -> 3
     */
    private static String compressSemester(String full) {
        switch (full) {
            case "1st Semester": return "1";
            case "2nd Semester": return "2";
            case "Summer": return "3";
            default: return full; // Already abbreviated
        }
    }
    
    /**
     * Process end of semester - update enrollment statuses based on grades
     */
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
                        
                        // Build updated course statuses
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
                        
                        // Update course statuses field (field 7)
                        if (parts.length > 7 && !courseStatusList.isEmpty()) {
                            parts[7] = String.join(";", courseStatusList);
                        }
                    }
                }
                
                pw.println(String.join(",", parts));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        }
        
        // Update all processed students' completed courses and cumulative GWA
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
    
    // ==================== PREREQUISITE CHECKING ====================
    
    private static Map<String, List<String>> prerequisitesCache = null;
    
    /**
     * Load prerequisites from courseSubject.txt
     */
    public static void loadPrerequisites() {
        prerequisitesCache = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(
                FilePathResolver.resolveCourseSubjectFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String courseID = parts[0];
                    String prereqStr = parts[5];
                    
                    if (!prereqStr.equals("NONE") && !prereqStr.isEmpty()) {
                        prerequisitesCache.put(courseID, Arrays.asList(prereqStr.split(";")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Check if student has passed all prerequisites for a course
     */
    public static boolean hasPassedPrerequisites(String studentID, String courseID) {
        if (prerequisitesCache == null) {
            loadPrerequisites();
        }
        
        List<String> requiredCourses = prerequisitesCache.get(courseID);
        if (requiredCourses == null || requiredCourses.isEmpty()) {
            return true;
        }
        
        Set<String> passedCourses = getPassedCourses(studentID);
        
        for (String prereq : requiredCourses) {
            if (!passedCourses.contains(prereq)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Get list of missing prerequisites for a course
     */
    public static List<String> getMissingPrerequisites(String studentID, String courseID) {
        if (prerequisitesCache == null) {
            loadPrerequisites();
        }
        
        List<String> required = prerequisitesCache.get(courseID);
        if (required == null) return new ArrayList<>();
        
        Set<String> passed = getPassedCourses(studentID);
        List<String> missing = new ArrayList<>();
        
        for (String prereq : required) {
            if (!passed.contains(prereq)) {
                missing.add(prereq);
            }
        }
        
        return missing;
    }
    
    private static Set<String> getPassedCourses(String studentID) {
        Set<String> passed = new HashSet<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(
                FilePathResolver.resolveEnrollmentFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                
                if (parts.length >= 5 && parts[0].equals(studentID) && "PASSED".equals(parts[4])) {
                    String[] courses = parts[1].split(";");
                    for (String course : courses) {
                        if (!course.trim().isEmpty()) {
                            passed.add(course.trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return passed;
    }
    
    // ==================== RETAKE MANAGEMENT ====================
    
    /**
     * Check if student can retake a course
     */
    public static boolean canRetakeCourse(String studentID, String courseID) {
        int retakeCount = getRetakeCount(studentID, courseID);
        
        if (retakeCount >= 3) {
            return false;
        }
        
        String lastStatus = getLastCourseStatus(studentID, courseID);
        return "FAILED".equals(lastStatus) || "INC".equals(lastStatus);
    }
    
    /**
     * Get number of times student has taken a course
     */
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
    
    /**
     * Get last enrollment status for a course
     */
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
    
    /**
     * Get recommended summer courses for a student
     */
    public static List<String> getRecommendedSummerCourses(String studentID) {
        List<String> recommended = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(
                FilePathResolver.resolveEnrollmentFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                
                if (parts.length >= 5 && parts[0].equals(studentID)) {
                    String status = parts[4];
                    
                    if ("FAILED".equals(status) || "INC".equals(status)) {
                        String[] courses = parts[1].split(";");
                        for (String courseID : courses) {
                            if (!courseID.trim().isEmpty() && !recommended.contains(courseID.trim())) {
                                recommended.add(courseID.trim());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return recommended;
    }
    
    // ==================== GRADUATION PROCESSING ====================
    
    /**
     * Check if student is eligible for graduation
     */
    public static boolean isEligibleForGraduation(String studentID) {
        if (hasIncompleteRequirements(studentID)) {
            return false;
        }
        
        int totalUnits = getTotalUnitsEarned(studentID);
        if (totalUnits < 120) {
            return false;
        }
        
        double gwa = getStudentGWA(studentID);
        if (gwa > 3.0 || gwa == 0.0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Calculate Latin Honors based on GWA
     */
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
    
    /**
     * Process graduation for a student
     */
    public static boolean processGraduation(String studentID) {
        if (!isEligibleForGraduation(studentID)) {
            return false;
        }
        
        String studentName = getStudentName(studentID);
        double gwa = getStudentGWA(studentID);
        String honors = getLatinHonors(gwa);
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
            e.printStackTrace();
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
    
    private static int getTotalUnitsEarned(String studentID) {
        // Would need to count units from PASSED courses
        // For now, estimate based on passed enrollment records
        int passedRecords = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(
                FilePathResolver.resolveEnrollmentFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(studentID) && "PASSED".equals(parts[4])) {
                    String[] courses = parts[1].split(";");
                    passedRecords += courses.length;
                }
            }
        } catch (Exception ignored) {}
        
        // Assuming average 3 units per course
        return passedRecords * 3;
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
            e.printStackTrace();
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
    
    // ==================== STUDENT PROMOTION SYSTEM (2-YEAR PROGRAM) ====================
    
    /**
     * Check if student has completed all requirements for current semester
     */
    public static boolean hasCompletedSemester(String studentID, String semester, String yearLevel) {
        try {
            String enrollmentPath = FilePathResolver.resolveEnrollmentFilePath();
            BufferedReader br = new BufferedReader(new FileReader(enrollmentPath));
            String line;
            String semNum = convertSemesterToNumber(semester);
            
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    if (parts[0].equals(studentID) && 
                        parts[2].equals(yearLevel) && 
                        parts[3].equals(semNum)) {
                        
                        String status = parts[4];
                        if (!"PASSED".equals(status)) {
                            br.close();
                            return false;
                        }
                    }
                }
            }
            br.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Promote student to next year level (2-year program: 1st → 2nd only)
     */
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
    
    /**
     * Get next year level (2-year program only)
     */
    private static String getNextYearLevel(String currentYearLevel) {
        switch (currentYearLevel) {
            case "1st Year":
                return "2nd Year";
            case "2nd Year":
                return null; // Ready for graduation
            default:
                return null;
        }
    }
    
    /**
     * Convert semester name to number
     */
    private static String convertSemesterToNumber(String semester) {
        if (semester == null) return "1";
        switch (semester) {
            case "1st Semester": return "1";
            case "2nd Semester": return "2";
            case "Summer": return "3";
            default: return "1";
        }
    }
    
    /**
     * Promote all eligible students to next year level
     */
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
        boolean completed1stSem = hasCompletedSemester(studentID, "1st Semester", yearLevel);
        boolean completed2ndSem = hasCompletedSemester(studentID, "2nd Semester", yearLevel);
        
        return completed1stSem && completed2ndSem;
    }
    
    /**
     * Check if student has no incomplete requirements (all courses PASSED)
     */
    private static boolean hasNoIncompleteRequirements(String studentID) {
        try {
            String enrollmentPath = FilePathResolver.resolveEnrollmentFilePath();
            BufferedReader br = new BufferedReader(new FileReader(enrollmentPath));
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(studentID)) {
                    String status = parts[4];
                    // Check for any incomplete/failed statuses
                    if ("FAILED".equals(status) || "INC".equals(status) || "DROPPED".equals(status) || "ENROLLED".equals(status)) {
                        br.close();
                        return false;
                    }
                }
            }
            br.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get graduation requirements for 2-year program
     */
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
    
    /**
     * Check if student is in 2nd year
     */
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
    
    /**
     * Generate promotion report for 2-year program
     */
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
    
    /**
     * Generate graduation candidates report (2nd Year only)
     */
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
                            String honors = getLatinHonors(gwa);
                            String entry = studentID + " - " + studentName + " (GWA: " + String.format("%.2f", gwa) + ")";
                            
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
    
    /**
     * Process graduation for all eligible 2nd year students
     */
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
    
    // ==================== GWA CALCULATION METHODS ====================
    
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
        
        // Calculate weighted average (each semester's GWA weighted by its course count)
        double totalWeightedGWA = 0.0;
        int totalCourses = 0;
        
        for (Marksheet m : studentMarksheets) {
            // Calculate GWA for this marksheet
            double[] marks = m.getMarks();
            double sum = 0.0;
            int validMarks = 0;
            
            for (double mark : marks) {
                if (mark > 0.0) { // Only count valid marks
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
        return Math.round(cumulativeGWA * 100.0) / 100.0; // Round to 2 decimal places
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
                return; // No marksheet found
            }
            
            // Load enrollments
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
            
            // Save updated enrollments if any were changed
            if (updated) {
                EnrollmentFileSaver saver = new EnrollmentFileSaver();
                saver.saveEnrollmentsByStudent(resolvedEnrollmentPath, allEnrollments);
            }
            
            // Update student's completed courses and cumulative GWA
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
            // Load all enrollments
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
            
            // Load all students
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
}
