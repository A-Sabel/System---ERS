package ers.group;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Schedule {
    // Fields
    private String scheduleID;
    private String courseID;
    private String room;
    private String day;
    private String startTime;
    private String endTime;
    private String teacherName;
    public static final int DEFAULT_MAX_CAPACITY = 5;

    // Constructor
    public Schedule(String scheduleID, String courseID, String room, String day, String startTime, String endTime, String teacherName) {
        this.scheduleID = scheduleID;
        this.courseID = courseID;
        this.room = room;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.teacherName = teacherName;
    }

    // Getters
    public String getScheduleID() { return scheduleID; }
    public String getCourseID() { return courseID; }
    public String getRoom() { return room; }
    public String getDay() { return day; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getTeacherName() { return teacherName; }

    // Setters / Methods to update schedule
    public void reschedule(String newDay, String newStartTime, String newEndTime) {
        this.day = newDay;
        this.startTime = newStartTime;
        this.endTime = newEndTime;
    }

    public void assignRoom(String newRoom) {
        this.room = newRoom;
    }

    @Override
    public String toString() {
        return "Schedule: " + courseID + " in " + room + " on " + day + " from " + startTime + " to " + endTime + ", Teacher: " + teacherName;
    }
    
    
    private static final int MAX_SECTIONS_PER_COURSE = 3;
    public static class SectionFullException extends Exception {
        public SectionFullException(String message) {
            super(message);
        }
        
        public SectionFullException(String courseID, int maxSections, int capacity) {
            super(String.format("Course %s is full - all %d sections at capacity (%d students each)", 
                courseID, maxSections, capacity));
        }
    }
    
    /**
     * Assigns a student to a section for the given course.
     * Auto-creates new sections (up to 3) if existing sections are full.
     * 
     * @param courseID The course ID (e.g., "CS101")
     * @param studentID The student 
     * @param maxCapacity Maximum students per section
     * @return The assigned section ID (e.g., "CS101-SEC1")
     * @throws SectionFullException if all 3 sections are at capacity
     */
    public static String assignSection(String courseID, String studentID, int maxCapacity) throws SectionFullException {
        try {
            // Load existing sections for this course
            Map<String, Integer> sectionEnrollmentCounts = getSectionEnrollmentCounts(courseID);
            // Find first section under capacity
            for (int i = 1; i <= MAX_SECTIONS_PER_COURSE; i++) {
                String sectionID = courseID + "-SEC" + i;
                int currentEnrollment = sectionEnrollmentCounts.getOrDefault(sectionID, 0);
                
                if (currentEnrollment < maxCapacity) {
                    return sectionID;
                }
            }
            // All sections full or don't exist - check if we can create a new one
            int existingSectionCount = sectionEnrollmentCounts.size();
            if (existingSectionCount < MAX_SECTIONS_PER_COURSE) {
                // Create new section
                String newSectionID = courseID + "-SEC" + (existingSectionCount + 1);
                // Assign schedule to new section
                assignScheduleToSection(newSectionID, courseID, isLabCourse(courseID));
                return newSectionID;
            }
            // All 3 sections exist and are full
            throw new SectionFullException(courseID, MAX_SECTIONS_PER_COURSE, maxCapacity);
        } catch (Exception e) {
            if (e instanceof SectionFullException) {
                throw (SectionFullException) e;
            }
            throw new RuntimeException("Error assigning section: " + e.getMessage(), e);
        }
    }
    
    private static Map<String, Integer> getSectionEnrollmentCounts(String courseID) {
        Map<String, Integer> counts = new HashMap<>();
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/enrollment.txt",
                "src/ers/group/master files/enrollment.txt",
                "master files/enrollment.txt",
                "enrollment.txt"
            };
            File enrollmentFile = null;
            for (String path : possiblePaths) {
                File f = new File(path);
                if (f.exists()) {
                    enrollmentFile = f;
                    break;
                }
            }
            if (enrollmentFile == null || !enrollmentFile.exists()) {
                return counts;
            }
            EnrollmentFileLoader loader = new EnrollmentFileLoader();
            loader.load(enrollmentFile.getPath());
            for (Enrollment e : loader.getAllEnrollments()) {
                if (e.getCourseID().equals(courseID) && "ENROLLED".equals(e.getStatus())) {
                    String sectionID = e.getSectionID();
                    counts.put(sectionID, counts.getOrDefault(sectionID, 0) + 1);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading enrollment counts: " + e.getMessage());
        }
        return counts;
    }
    
    private static boolean isLabCourse(String courseID) {
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/coursesubject.txt",
                "src/ers/group/master files/coursesubject.txt",
                "master files/coursesubject.txt",
                "coursesubject.txt"
            };
            for (String path : possiblePaths) {
                File f = new File(path);
                if (f.exists()) {
                    CourseSubjectFileLoader loader = new CourseSubjectFileLoader();
                    loader.load(path);
                    for (CourseSubject course : loader.getAllSubjects()) {
                        if (course.getCourseSubjectID().equals(courseID)) {
                            return course.isLabRoom();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking if course is lab: " + e.getMessage());
        }
        return false;
    }
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a", Locale.US);
    private static final String[][] TIME_SLOTS = {
        {"8:00 AM", "9:30 AM"},
        {"10:00 AM", "11:30 AM"},
        {"1:00 PM", "2:30 PM"},
        {"3:00 PM", "4:30 PM"}
    };
    
    private static final String[] DAYS = {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
    };
    
    /**
     * Assigns a schedule to a new section, avoiding conflicts with existing sections.
     */
    public static void assignScheduleToSection(String sectionID, String courseID, boolean isLab) {
        try {
            List<Schedule> existingSchedules = getExistingSchedules(courseID);
            String[] assignedSlot = findAvailableTimeSlot(existingSchedules);
            
            if (assignedSlot == null) {
                assignedSlot = new String[]{DAYS[0], TIME_SLOTS[0][0], TIME_SLOTS[0][1]};
            }
            String scheduleID = generateScheduleID();
            Schedule newSchedule = new Schedule(
                scheduleID,
                sectionID,
                "TBA",
                assignedSlot[0],
                assignedSlot[1],
                assignedSlot[2],
                "TBA"
            );
            saveSchedule(newSchedule);
        } catch (Exception e) {
            System.err.println("Error assigning schedule: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static String[] findAvailableTimeSlot(List<Schedule> existingSchedules) {
        for (String day : DAYS) {
            for (String[] timeSlot : TIME_SLOTS) {
                String startTime = timeSlot[0];
                String endTime = timeSlot[1];
                boolean hasConflict = false;
                for (Schedule existing : existingSchedules) {
                    if (conflictsWithSchedule(existing, day, startTime, endTime)) {
                        hasConflict = true;
                        break;
                    }
                }
                if (!hasConflict) {
                    return new String[]{day, startTime, endTime};
                }
            }
        }
        return null;
    }
    
    private static boolean conflictsWithSchedule(Schedule existing, String day, String startTime, String endTime) {
        if (!existing.getDay().equals(day)) {
            return false;
        }
        try {
            LocalTime existingStart = LocalTime.parse(existing.getStartTime(), TIME_FORMATTER);
            LocalTime existingEnd = LocalTime.parse(existing.getEndTime(), TIME_FORMATTER);
            LocalTime proposedStart = LocalTime.parse(startTime, TIME_FORMATTER);
            LocalTime proposedEnd = LocalTime.parse(endTime, TIME_FORMATTER);
            return !(proposedEnd.isBefore(existingStart) || proposedEnd.equals(existingStart) || 
                    proposedStart.isAfter(existingEnd) || proposedStart.equals(existingEnd));
        } catch (Exception e) {
            System.err.println("Error parsing times: " + e.getMessage());
            return false;
        }
    }
    
    private static List<Schedule> getExistingSchedules(String courseID) {
        List<Schedule> schedules = new ArrayList<>();
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/schedule.txt",
                "src/ers/group/master files/schedule.txt",
                "master files/schedule.txt",
                "schedule.txt"
            };
            File scheduleFile = null;
            for (String path : possiblePaths) {
                File f = new File(path);
                if (f.exists()) {
                    scheduleFile = f;
                    break;
                }
            }
            if (scheduleFile != null && scheduleFile.exists()) {
                ScheduleFileLoader loader = new ScheduleFileLoader();
                loader.load(scheduleFile.getPath());
                
                for (Schedule s : loader.getAllSchedules()) {
                    if (s.getCourseID().startsWith(courseID)) {
                        schedules.add(s);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading schedules: " + e.getMessage());
        }
        return schedules;
    }
    
    private static String generateScheduleID() {
        int maxId = 0;
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/schedule.txt",
                "src/ers/group/master files/schedule.txt",
                "master files/schedule.txt",
                "schedule.txt"
            };
            for (String path : possiblePaths) {
                File f = new File(path);
                if (f.exists()) {
                    ScheduleFileLoader loader = new ScheduleFileLoader();
                    loader.load(path);
                    
                    for (Schedule s : loader.getAllSchedules()) {
                        String id = s.getScheduleID();
                        if (id.startsWith("SCH-")) {
                            int num = Integer.parseInt(id.substring(4));
                            if (num > maxId) maxId = num;
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error generating schedule ID: " + e.getMessage());
        }
        return String.format("SCH-%03d", maxId + 1);
    }
    
    private static void saveSchedule(Schedule schedule) {
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/schedule.txt",
                "src/ers/group/master files/schedule.txt",
                "master files/schedule.txt",
                "schedule.txt"
            };
            String filePath = null;
            for (String path : possiblePaths) {
                File f = new File(path);
                if (f.exists()) {
                    filePath = path;
                    break;
                }
            }
            if (filePath != null) {
                ScheduleFileSaver saver = new ScheduleFileSaver();
                ScheduleFileLoader loader = new ScheduleFileLoader();
                loader.load(filePath);
                List<Schedule> allSchedules = new ArrayList<>(loader.getAllSchedules());
                allSchedules.add(schedule);
                saver.save(filePath, allSchedules);
            }
        } catch (Exception e) {
            System.err.println("Error saving schedule: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

