package ers.group;


import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
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
    
    /**
     * Exception thrown when a course cannot be scheduled due to resource shortages.
     */
    public static class SchedulingConflictException extends Exception {
        private final String courseID;
        private final String sectionID;
        private final int unitsRequested;
        private final double unitsScheduled;
        private final String reason;
        
        public SchedulingConflictException(String sectionID, String courseID, int unitsRequested, double unitsScheduled, String reason) {
            super(String.format("Cannot schedule %s (%s): %s. Only %.1f/%.0f hours scheduled.",
                sectionID, courseID, reason, unitsScheduled, (double)unitsRequested));
            this.sectionID = sectionID;
            this.courseID = courseID;
            this.unitsRequested = unitsRequested;
            this.unitsScheduled = unitsScheduled;
            this.reason = reason;
        }
        
        public String getCourseID() { return courseID; }
        public String getSectionID() { return sectionID; }
        public int getUnitsRequested() { return unitsRequested; }
        public double getUnitsScheduled() { return unitsScheduled; }
        public String getReason() { return reason; }
    }


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
     * @param isLab Whether the course requires a lab room
     * @param units Number of units for the course (for schedule duration calculation)
     * @return The assigned section ID (e.g., "CS101-SEC1")
     * @throws SectionFullException if all 3 sections are at capacity
     */
    public static String assignSection(String courseID, String studentID, int maxCapacity, boolean isLab, int units) throws SectionFullException {
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
                // Assign schedule to new section with course units for duration calculation
                // Pass null for studentID since we don't have it yet in assignSection
                assignScheduleToSection(newSectionID, courseID, isLab, units, null);
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
            String enrollmentFilePath = FilePathResolver.resolveEnrollmentFilePath();
            File enrollmentFile = new File(enrollmentFilePath);
            if (!enrollmentFile.exists()) {
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
            String courseFilePath = FilePathResolver.resolveCourseSubjectFilePath();
            File f = new File(courseFilePath);
            if (f.exists()) {
                CourseSubjectFileLoader loader = new CourseSubjectFileLoader();
                loader.load(courseFilePath);
                for (CourseSubject course : loader.getAllSubjects()) {
                    if (course.getCourseSubjectID().equals(courseID)) {
                        return course.isLabRoom();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking if course is lab: " + e.getMessage());
        }
        return false;
    }
   
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a", Locale.US);
    
    // Enhanced start times for more flexible scheduling (30-minute intervals)
    private static final String[] START_TIME_SLOTS = {
        "8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM", 
        "11:00 AM", "11:30 AM", "1:00 PM", "1:30 PM", "2:00 PM", "2:30 PM", 
        "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM", "5:00 PM"
    };
   
    private static final String[] DAYS = {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
    };
   
    /**
     * Assigns a schedule to a new section using intelligent unit splicing and mandatory 30-minute breaks.
     * Courses are automatically split across multiple days with optimal distribution.
     * @param sectionID The section identifier (e.g., "CS101-SEC1")
     * @param courseID The course identifier
     * @param isLab Whether the course requires a lab room
     * @param units Number of units for the course (will be spliced across multiple sessions)
     * @param studentID The student being enrolled (used for conflict detection)
     * @throws SchedulingConflictException if the course cannot be fully scheduled due to resource shortages
     */
    public static void assignScheduleToSection(String sectionID, String courseID, boolean isLab, int units, String studentID) throws SchedulingConflictException {
        try {
            // Check if this section already has schedules assigned
            if (sectionHasSchedule(sectionID)) {
                System.out.println("Section " + sectionID + " already has schedules assigned, skipping creation");
                return;
            }
            
            // Get ALL existing schedules to avoid conflicts across all courses
            List<Schedule> existingSchedules = getAllSchedules();
            
            SchedulingResult result = calculateOptimalSplices(units, existingSchedules, sectionID, courseID, isLab, studentID);
            List<ScheduleSplice> assignedSplices = result.splices;
            
            if (assignedSplices.isEmpty()) {
                String reason = result.failureReason != null ? result.failureReason : "No available time slots due to room/teacher shortages";
                throw new SchedulingConflictException(sectionID, courseID, units, 0, reason);
            }
            
            // Check if we couldn't schedule all required units
            double totalScheduled = 0;
            for (ScheduleSplice splice : assignedSplices) {
                totalScheduled += splice.durationHours;
            }
            
            if (totalScheduled < units) {
                String reason = result.failureReason != null ? result.failureReason : 
                    "Insufficient room/teacher availability for all " + units + " hours";
                throw new SchedulingConflictException(sectionID, courseID, units, totalScheduled, reason);
            }
            
            // Try to maintain same teacher across all splices for consistency
            String preferredTeacher = null;
            
            // Create schedule entries for each splice
            for (ScheduleSplice splice : assignedSplices) {
                // Assign teacher (prefer same teacher for all splices)
                String assignedTeacher;
                if (preferredTeacher != null) {
                    // Check if preferred teacher is still available for this splice
                    boolean teacherAvailable = isTeacherAvailable(preferredTeacher, splice.day, splice.startTime, splice.endTime, existingSchedules);
                    if (teacherAvailable) {
                        assignedTeacher = preferredTeacher;
                    } else {
                        assignedTeacher = assignTeacherToCourse(courseID, splice.day, splice.startTime, splice.endTime);
                    }
                } else {
                    assignedTeacher = assignTeacherToCourse(courseID, splice.day, splice.startTime, splice.endTime);
                    preferredTeacher = assignedTeacher; // Set preferred teacher for subsequent splices
                }
                
                // Assign room for this specific splice
                String assignedRoom = assignRoomToCourse(courseID, isLab, splice.day, splice.startTime, splice.endTime);
                
                // Create unique schedule ID for this splice
                String scheduleID = generateScheduleID();
                
                Schedule newSchedule = new Schedule(
                    scheduleID,
                    sectionID,         // Store sectionID for section-specific scheduling
                    assignedRoom,
                    splice.day,
                    splice.startTime,
                    splice.endTime,
                    assignedTeacher
                );
                
                // Log the splice creation
                System.out.println("Created schedule splice " + scheduleID + " for section " + sectionID + 
                                  " - Teacher: " + assignedTeacher + 
                                  ", Room: " + assignedRoom + 
                                  ", Time: " + splice.day + " " + splice.startTime + "-" + splice.endTime +
                                  " (" + splice.durationHours + "h)");
                
                saveSchedule(newSchedule);
                
                // Add this schedule to existing schedules for next splice conflict checking
                existingSchedules.add(newSchedule);
            }
            
            System.out.println("Successfully created " + assignedSplices.size() + " schedule splices for " + 
                              units + "-unit course " + sectionID);
            
        } catch (Exception e) {
            System.err.println("Error assigning schedule with splicing: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Assigns a qualified teacher to the course who is available at the given time slot.
     * @param courseID The course ID
     * @param day The day of the week
     * @param startTime The start time
     * @param endTime The end time
     * @return The teacher name, or "TBA" if no qualified teacher is available
     */
    private static String assignTeacherToCourse(String courseID, String day, String startTime, String endTime) {
        try {
            String teacherFilePath = FilePathResolver.resolveTeachersFilePath();
            File teacherFile = new File(teacherFilePath);
            if (!teacherFile.exists()) {
                return "TBA";
            }
            
            TeacherFileLoader teacherLoader = new TeacherFileLoader();
            teacherLoader.load(teacherFilePath);
            
            List<Schedule> allSchedules = getAllSchedules();
            
            // Find a teacher qualified for this course
            for (Teachers teacher : teacherLoader.getAllTeachers()) {
                if (teacher.canTeachSubject(courseID)) {
                    // Check if teacher is available at this time slot
                    boolean hasConflict = false;
                    for (Schedule schedule : allSchedules) {
                        if (schedule.getTeacherName().equals(teacher.getTeacherName()) &&
                            conflictsWithSchedule(schedule, day, startTime, endTime)) {
                            hasConflict = true;
                            break;
                        }
                    }
                    if (!hasConflict) {
                        return teacher.getTeacherName();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error assigning teacher: " + e.getMessage());
        }
        return "TBA";
    }
    
    /**
     * Assigns an appropriate room for the course that is available at the given time slot.
     * @param courseID The course ID
     * @param isLab Whether the course requires a lab
     * @param day The day of the week
     * @param startTime The start time
     * @param endTime The end time
     * @return The room name, or "TBA" if no room is available
     */
    private static String assignRoomToCourse(String courseID, boolean isLab, String day, String startTime, String endTime) {
        try {
            String roomFilePath = FilePathResolver.resolveRoomsFilePath();
            File roomFile = new File(roomFilePath);
            if (!roomFile.exists()) {
                return "TBA";
            }
            
            RoomFileLoader roomLoader = new RoomFileLoader();
            roomLoader.load(roomFilePath);
            
            List<Schedule> allSchedules = getAllSchedules();
            
            // First, check for special room requirements
            for (Rooms room : roomLoader.getAllRooms()) {
                if (room.isSpecialRoomFor(courseID)) {
                    // Check room availability at this time slot
                    boolean hasConflict = false;
                    for (Schedule schedule : allSchedules) {
                        if (schedule.getRoom().equals(room.getRoomID()) &&
                            conflictsWithSchedule(schedule, day, startTime, endTime)) {
                            hasConflict = true;
                            break;
                        }
                    }
                    if (!hasConflict) {
                        return room.getRoomID();
                    }
                }
            }
            
            // Otherwise, find an appropriate room (lab or regular)
            for (Rooms room : roomLoader.getAllRooms()) {
                if (room.isLabRoom() == isLab) {
                    // Check room availability at this time slot
                    boolean hasConflict = false;
                    for (Schedule schedule : allSchedules) {
                        if (schedule.getRoom().equals(room.getRoomID()) &&
                            conflictsWithSchedule(schedule, day, startTime, endTime)) {
                            hasConflict = true;
                            break;
                        }
                    }
                    if (!hasConflict && room.getCapacity() >= DEFAULT_MAX_CAPACITY) {
                        return room.getRoomID();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error assigning room: " + e.getMessage());
        }
        return "TBA";
    }
    
    /**
     * Load all existing schedules from the schedule file.
     * @return List of all schedules
     */
    private static List<Schedule> getAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        try {
            String scheduleFilePath = FilePathResolver.resolveScheduleFilePath();
            File scheduleFile = new File(scheduleFilePath);
            if (scheduleFile.exists()) {
                ScheduleFileLoader loader = new ScheduleFileLoader();
                loader.load(scheduleFilePath);
                schedules = new ArrayList<>(loader.getAllSchedules());
            }
        } catch (Exception e) {
            System.err.println("Error loading all schedules: " + e.getMessage());
        }
        return schedules;
    }
    /**
     * Helper class to represent a schedule splice with duration information.
     */
    private static class ScheduleSplice {
        final String day;
        final String startTime;
        final String endTime;
        final double durationHours;
        
        ScheduleSplice(String day, String startTime, String endTime, double durationHours) {
            this.day = day;
            this.startTime = startTime;
            this.endTime = endTime;
            this.durationHours = durationHours;
        }
    }
    
    /**
     * Result of schedule calculation with success/failure tracking.
     */
    private static class SchedulingResult {
        final List<ScheduleSplice> splices;
        final String failureReason;
        final int roomConflicts;
        final int teacherConflicts;
        final int studentConflicts;
        
        SchedulingResult(List<ScheduleSplice> splices, String failureReason, int roomConflicts, int teacherConflicts, int studentConflicts) {
            this.splices = splices;
            this.failureReason = failureReason;
            this.roomConflicts = roomConflicts;
            this.teacherConflicts = teacherConflicts;
            this.studentConflicts = studentConflicts;
        }
    }
    
    /**
     * Calculates optimal splices for a course with UNIVERSITY-STYLE SCHEDULING.
     * NOW PRIORITIZES MONDAY ACROSS ALL ROOMS before moving to other days.
     * INCLUDES STUDENT CONFLICT CHECKING to prevent double-booking.
     * @param totalUnits Total course units to distribute
     * @param existingSchedules All existing schedules for conflict checking
     * @param sectionID The section being scheduled (e.g., "CS101-SEC1")
     * @param courseID The course being scheduled (e.g., "CS101")
     * @param isLab Whether this is a lab course
     * @param studentID The student being enrolled (for conflict detection)
     * @return SchedulingResult containing splices and failure tracking
     */
    private static SchedulingResult calculateOptimalSplices(int totalUnits, List<Schedule> existingSchedules, String sectionID, String courseID, boolean isLab, String studentID) {
        List<ScheduleSplice> assignedSplices = new ArrayList<>();
        double remainingHours = totalUnits * 1.0;
        int roomConflicts = 0;
        int teacherConflicts = 0;
        int studentConflicts = 0;
        String failureReason = null;

        final double MAX_HOURS_PER_DAY = 7.0;
        
        // Track hours scheduled per day to enforce limit
        Map<String, Double> hoursPerDay = new HashMap<>();
        for (String day : DAYS) {
            hoursPerDay.put(day, 0.0);
        }
        // Calculate hours already scheduled for this student on each day
        if (studentID != null && !studentID.isEmpty()) {
            // Get sections this student is enrolled in
            Set<String> studentSections = new HashSet<>();
            try {
                String enrollmentFilePath = FilePathResolver.resolveEnrollmentFilePath();
                File enrollmentFile = new File(enrollmentFilePath);
                if (enrollmentFile.exists()) {
                    EnrollmentFileLoader enrollmentLoader = new EnrollmentFileLoader();
                    enrollmentLoader.load(enrollmentFilePath);
                    
                    for (Enrollment enrollment : enrollmentLoader.getAllEnrollments()) {
                        if (enrollment.getStudentID().equals(studentID) && "ENROLLED".equals(enrollment.getStatus())) {
                            String secID = enrollment.getSectionID();
                            if (secID != null && !secID.isEmpty()) {
                                studentSections.add(secID);
                            }
                        }
                    }
                }
                // Add cached sections
                Set<String> cachedSections = getStudentSectionsFromCache(studentID);
                if (cachedSections != null) {
                    studentSections.addAll(cachedSections);
                }
                // Sum up hours for each day from student's existing schedules
                for (Schedule existing : existingSchedules) {
                    String scheduledSection = existing.getCourseID();
                    // Check if this schedule is for one of the student's sections
                    boolean isStudentSection = false;
                    for (String enrolledSection : studentSections) {
                        if (scheduledSection.equals(enrolledSection) || 
                            scheduledSection.startsWith(enrolledSection + "-") ||
                            (enrolledSection != null && enrolledSection.startsWith(scheduledSection))) {
                            isStudentSection = true;
                            break;
                        }
                    }
                    if (isStudentSection) {
                        String scheduleDay = existing.getDay();
                        double existingHours = calculateDurationHours(existing.getStartTime(), existing.getEndTime());
                        hoursPerDay.put(scheduleDay, hoursPerDay.get(scheduleDay) + existingHours);
                    }
                }
            } catch (Exception e) {
                System.err.println("Warning: Could not load student's existing schedules: " + e.getMessage());
            }
        }
        System.out.println("Calculating UNIVERSITY-STYLE splices for " + totalUnits + " units (" + remainingHours + " hours total)");
        System.out.println("Policy: Maximum " + MAX_HOURS_PER_DAY + " hours per day to ensure 3+ day distribution");
        // Log student's existing daily hours
        if (studentID != null) {
            System.out.println("Student " + studentID + " existing hours per day:");
            for (String day : DAYS) {
                double hours = hoursPerDay.get(day);
                if (hours > 0) {
                    System.out.println("  " + day + ": " + hours + " hours already scheduled");
                }
            }
        }

        for (String day : DAYS) {
            if (remainingHours <= 0) break;
            double dayHoursUsed = hoursPerDay.get(day);
            double dayHoursAvailable = MAX_HOURS_PER_DAY - dayHoursUsed;
            if (dayHoursAvailable <= 0) {
                System.out.println("Skipping " + day + " (max " + MAX_HOURS_PER_DAY + "h limit reached)");
                continue; // Skip this day if limit reached
            }
            System.out.println("Trying to schedule on " + day + " (" + remainingHours + "h remaining, " + dayHoursAvailable + "h available today)");
            // For each time slot on this day, try to find ANY available room and teacher
            for (String startTime : START_TIME_SLOTS) {
                if (remainingHours <= 0) break;
                dayHoursUsed = hoursPerDay.get(day);
                dayHoursAvailable = MAX_HOURS_PER_DAY - dayHoursUsed;
                if (dayHoursAvailable <= 0) break; // No more capacity today
                // Determine optimal splice duration (limited by remaining hours, daily limit, and max 3h blocks)
                double dayDuration = Math.min(Math.min(remainingHours, dayHoursAvailable), 3.0);
                if (dayDuration < 1.0 && remainingHours >= 1.0) {
                    dayDuration = 1.0; // Minimum 1 hour per splice
                }
                String endTime = calculateEndTime(startTime, dayDuration);
                // VALIDATE TIME SLOT: Ensure it doesn't exceed school hours
                if (!isTimeSlotValid(startTime, endTime)) {
                    continue; // Skip invalid time slots
                }
                // Find ANY room and ANY teacher available at THIS specific time
                String availableRoom = findAvailableRoom(day, startTime, endTime, isLab, existingSchedules);
                String availableTeacher = findAvailableTeacher(courseID, day, startTime, endTime, existingSchedules);
                // Check if STUDENT is already busy at this time
                boolean studentIsBusy = isStudentBusyAtTime(studentID, day, startTime, endTime, existingSchedules);
                // Track conflicts for reporting
                if (availableRoom == null) roomConflicts++;
                if (availableTeacher == null) teacherConflicts++;
                if (studentIsBusy) studentConflicts++;
                if (availableRoom != null && availableTeacher != null && !studentIsBusy) {
                    // Schedule the splice and update daily hour tracking
                    assignedSplices.add(new ScheduleSplice(day, startTime, endTime, dayDuration));
                    remainingHours -= dayDuration;
                    hoursPerDay.put(day, hoursPerDay.get(day) + dayDuration);
                    System.out.println("  / Scheduled " + dayDuration + "h splice: " + day + " " + startTime + "-" + endTime + 
                                    " (Room: " + availableRoom + ", " + remainingHours + "h remaining, " + 
                                    hoursPerDay.get(day) + "/" + MAX_HOURS_PER_DAY + "h used today)");
                    break;
                }
            }
        }
        // If we still have remaining hours, try smaller splices
        if (remainingHours > 0) {
            System.out.println("Attempting to fit remaining " + remainingHours + " hours with smaller splices...");
            for (String day : DAYS) {
                if (remainingHours <= 0) break;
                double dayHoursUsed = hoursPerDay.get(day);
                double dayHoursAvailable = MAX_HOURS_PER_DAY - dayHoursUsed;
                if (dayHoursAvailable <= 0) {
                    System.out.println("  Skipping " + day + " in fallback (daily limit reached)");
                    continue;
                }
                // Try 1-hour increments for remaining time, limited by daily availability
                double dayDuration = Math.min(Math.min(remainingHours, dayHoursAvailable), 1.0);
                for (String startTime : START_TIME_SLOTS) {
                    String endTime = calculateEndTime(startTime, dayDuration);
                    // VALIDATE TIME SLOT: Ensure it doesn't exceed school hours
                    if (!isTimeSlotValid(startTime, endTime)) {
                        continue; // Skip invalid time slots
                    }
                    String availableRoom = findAvailableRoom(day, startTime, endTime, isLab, existingSchedules);
                    String availableTeacher = findAvailableTeacher(courseID, day, startTime, endTime, existingSchedules);
                    // Check if STUDENT is already busy at this time
                    boolean studentIsBusy = isStudentBusyAtTime(studentID, day, startTime, endTime, existingSchedules);
                    // Track conflicts for reporting
                    if (availableRoom == null) roomConflicts++;
                    if (availableTeacher == null) teacherConflicts++;
                    if (studentIsBusy) studentConflicts++;
                    if (availableRoom != null && availableTeacher != null && !studentIsBusy) {
                        // Check if adding this splice would exceed daily limit
                        double currentDayHours = hoursPerDay.get(day);
                        if (currentDayHours + dayDuration <= MAX_HOURS_PER_DAY) {
                            assignedSplices.add(new ScheduleSplice(day, startTime, endTime, dayDuration));
                            remainingHours -= dayDuration;
                            hoursPerDay.put(day, currentDayHours + dayDuration);
                            System.out.println("  / Added small splice: " + day + " " + startTime + "-" + endTime + 
                                            " (" + remainingHours + "h remaining, " + hoursPerDay.get(day) + "/" + MAX_HOURS_PER_DAY + "h today)");
                            break;
                        }
                    }
                }
            }
        }
        if (remainingHours > 0) {
            System.err.println("Warning: Could not schedule all " + totalUnits + " units. " + 
                            remainingHours + " hours remain unscheduled.");
            // Build detailed failure reason
            StringBuilder reason = new StringBuilder();
            if (roomConflicts > 0) {
                reason.append("No available ").append(isLab ? "lab" : "standard").append(" rooms (")
                    .append(roomConflicts).append(" conflicts). ");
            }
            if (teacherConflicts > 0) {
                reason.append("No available qualified teachers (").append(teacherConflicts).append(" conflicts). ");
            }
            if (studentConflicts > 0) {
                reason.append("Student schedule conflicts (").append(studentConflicts).append(" times). ");
            }
            if (reason.length() == 0) {
                reason.append("All time slots exhausted");
            }
            failureReason = reason.toString().trim();
        }
        return new SchedulingResult(assignedSplices, failureReason, roomConflicts, teacherConflicts, studentConflicts);
    }
    /**
     * Validates if a time slot is within acceptable school hours and logically valid.
     * @param start Start time in "h:mm a" format
     * @param end End time in "h:mm a" format
     * @return true if the time slot is valid, false otherwise
     */
    private static boolean isTimeSlotValid(String start, String end) {
        try {
            LocalTime s = LocalTime.parse(start, TIME_FORMATTER);
            LocalTime e = LocalTime.parse(end, TIME_FORMATTER);
            // Ensure start is before end, and end doesn't exceed 7:00 PM (19:00)
            return s.isBefore(e) && e.isBefore(LocalTime.of(19, 0));
        } catch (Exception ex) {
            System.err.println("Error validating time slot: " + ex.getMessage());
            return false;
        }
    }
    
    /**
     * Calculates end time based on start time and duration (supports fractional hours).
     * @param startTime Start time in "h:mm a" format (e.g., "8:00 AM")
     * @param durationHours Duration in hours (supports 0.5 for 30 minutes)
     * @return End time in "h:mm a" format
     */
    private static String calculateEndTime(String startTime, double durationHours) {
        try {
            LocalTime start = LocalTime.parse(startTime, TIME_FORMATTER);
            int hours = (int) durationHours;
            int minutes = (int) ((durationHours - hours) * 60);
            LocalTime end = start.plusHours(hours).plusMinutes(minutes);
            return end.format(TIME_FORMATTER);
        } catch (Exception e) {
            System.err.println("Error calculating end time: " + e.getMessage());
            return startTime; // Fallback
        }
    }
    
    /**
     * Calculate the duration in hours between start time and end time.
     * @param startTime Start time (e.g., "08:00 AM")
     * @param endTime End time (e.g., "11:00 AM")
     * @return Duration in hours (fractional for minutes)
     */
    private static double calculateDurationHours(String startTime, String endTime) {
        try {
            LocalTime start = LocalTime.parse(startTime, TIME_FORMATTER);
            LocalTime end = LocalTime.parse(endTime, TIME_FORMATTER);
            long minutes = java.time.Duration.between(start, end).toMinutes();
            return minutes / 60.0;
        } catch (Exception e) {
            System.err.println("Error calculating duration: " + e.getMessage());
            return 0.0; // Fallback
        }
    }
    
    /**
     * Checks if a specific student is already busy (has a class) at the proposed time.
     * This works by tracking student schedule assignments within an enrollment session.
     * @param studentID The student to check
     * @param day Day of the week
     * @param startTime Start time
     * @param endTime End time
     * @param existingSchedules All existing schedules (including ones just created in this session)
     * @return true if student is busy (has conflict), false if student is free
     */
    private static boolean isStudentBusyAtTime(String studentID, String day, String startTime, String endTime, List<Schedule> existingSchedules) {
        if (studentID == null || studentID.isEmpty()) {
            return false; // No student specified = not busy
        }
        
        try {
            // Build a complete picture of which sections this student is in
            // This includes BOTH saved enrollments AND in-memory student assignments
            java.util.Set<String> studentSections = new java.util.HashSet<>();
            
            // 1. Load from enrollment file (for previously saved enrollments)
            String enrollmentFilePath = FilePathResolver.resolveEnrollmentFilePath();
            File enrollmentFile = new File(enrollmentFilePath);
            if (enrollmentFile.exists()) {
                EnrollmentFileLoader enrollmentLoader = new EnrollmentFileLoader();
                enrollmentLoader.load(enrollmentFilePath);
                
                for (Enrollment enrollment : enrollmentLoader.getAllEnrollments()) {
                    if (enrollment.getStudentID().equals(studentID) && "ENROLLED".equals(enrollment.getStatus())) {
                        String sectionID = enrollment.getSectionID();
                        if (sectionID != null && !sectionID.isEmpty()) {
                            studentSections.add(sectionID);
                        }
                    }
                }
            }
            
            // 2. Check enrollment cache for current session assignments
            java.util.Set<String> cachedSections = getStudentSectionsFromCache(studentID);
            if (cachedSections != null) {
                studentSections.addAll(cachedSections);
            }
            
            // 3. Check if ANY of those sections have schedules that conflict with the proposed time
            for (Schedule existingSchedule : existingSchedules) {
                String scheduledTarget = existingSchedule.getCourseID(); // Usually the Section ID (e.g., CS101-SEC1)
                
                // Check if student is in this specific section
                boolean studentIsInThisClass = false;
                for (String enrolledSection : studentSections) {
                    // Match if the schedule is for this exact section OR for the base course
                    if (scheduledTarget.equals(enrolledSection) || 
                        scheduledTarget.startsWith(enrolledSection + "-") ||
                        (enrolledSection != null && enrolledSection.startsWith(scheduledTarget))) {
                        studentIsInThisClass = true;
                        break;
                    }
                }
                
                // If student is in this class, check for time conflicts
                if (studentIsInThisClass && existingSchedule.getDay().equals(day) &&
                    conflictsWithScheduleAndBreak(existingSchedule, day, startTime, endTime)) {
                    System.out.println("  X STUDENT BUSY: " + studentID + " already has " + 
                                     existingSchedule.getCourseID() + " at " + day + " " + startTime);
                    return true; // Student is busy!
                }
            }
            
            // Student is free at this time
            return false;
            
        } catch (Exception e) {
            System.err.println("Error checking if student is busy: " + e.getMessage());
            e.printStackTrace();
            return false; // Default to not busy
        }
    }
    
    // Cache to track student->section assignments during an enrollment session
    private static final java.util.Map<String, java.util.Set<String>> studentSectionCache = new java.util.HashMap<>();
    
    /**
     * Add a student-section assignment to the cache (for current enrollment session).
     * This allows detecting conflicts before enrollments are saved to file.
     */
    public static void cacheStudentSectionAssignment(String studentID, String sectionID) {
        if (studentID == null || sectionID == null) return;
        studentSectionCache.computeIfAbsent(studentID, k -> new java.util.HashSet<>()).add(sectionID);
        System.out.println("Cached assignment: " + studentID + " -> " + sectionID);
    }
    
    /**
     * Get cached section assignments for a student.
     */
    private static java.util.Set<String> getStudentSectionsFromCache(String studentID) {
        return studentSectionCache.get(studentID);
    }
    
    /**
     * Clear the student-section cache. Call this after enrollment session completes.
     */
    public static void clearStudentSectionCache() {
        studentSectionCache.clear();
        System.out.println("Cleared student-section cache");
    }
    
    /**
     * Clear all schedules. Call this when starting a new semester.
     */
    public static void clearAllSchedules() {
        try {
            String scheduleFilePath = FilePathResolver.resolveScheduleFilePath();
            File scheduleFile = new File(scheduleFilePath);
            
            // Create empty file (or overwrite with empty content)
            PrintWriter writer = new PrintWriter(new FileWriter(scheduleFile, false));
            writer.close();
            
            // Also clear the cache
            clearStudentSectionCache();
            
            System.out.println("Cleared all schedules from: " + scheduleFilePath);
        } catch (Exception e) {
            System.err.println("Error clearing schedules: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Checks if all students enrolled in a section are available (not busy) at the given time.
     * This prevents students from being double-booked in multiple classes.
     * @param sectionID The section being scheduled (e.g., "CS101-SEC1")
     * @param day Day of the week
     * @param startTime Start time
     * @param endTime End time
     * @param existingSchedules All existing schedules
     * @return true if ALL students are free, false if any student has a conflict
     */
    private static boolean areStudentsAvailable(String sectionID, String day, String startTime, String endTime, List<Schedule> existingSchedules) {
        try {
            // Load enrollment data to get students in this section
            String enrollmentFilePath = FilePathResolver.resolveEnrollmentFilePath();
            File enrollmentFile = new File(enrollmentFilePath);
            if (!enrollmentFile.exists()) {
                return true; // No enrollments yet, all students are "available"
            }
            
            EnrollmentFileLoader enrollmentLoader = new EnrollmentFileLoader();
            enrollmentLoader.load(enrollmentFilePath);
            
            // Get all students enrolled in this section
            List<String> studentsInSection = new ArrayList<>();
            for (Enrollment enrollment : enrollmentLoader.getAllEnrollments()) {
                if (enrollment.getSectionID().equals(sectionID) && 
                    "ENROLLED".equals(enrollment.getStatus())) {
                    studentsInSection.add(enrollment.getStudentID());
                }
            }
            
            // If no students enrolled yet, section is free to schedule
            if (studentsInSection.isEmpty()) {
                return true;
            }
            
            // Check if ANY of these students are already busy at this time
            for (String studentID : studentsInSection) {
                // Check all existing schedules to see if this student is already in a class
                for (Schedule existingSchedule : existingSchedules) {
                    // Check if this student is enrolled in the existing schedule's section
                    boolean studentInThisSchedule = false;
                    for (Enrollment enrollment : enrollmentLoader.getAllEnrollments()) {
                        if (enrollment.getStudentID().equals(studentID) &&
                            enrollment.getSectionID().equals(existingSchedule.getCourseID()) &&
                            "ENROLLED".equals(enrollment.getStatus())) {
                            studentInThisSchedule = true;
                            break;
                        }
                    }
                    
                    // If student is in this schedule and it conflicts with our proposed time
                    if (studentInThisSchedule && existingSchedule.getDay().equals(day) &&
                        conflictsWithScheduleAndBreak(existingSchedule, day, startTime, endTime)) {
                        System.out.println("  X Student " + studentID + " is busy at " + day + " " + startTime + 
                                         " (enrolled in " + existingSchedule.getCourseID() + ")");
                        return false; // Student conflict detected
                    }
                }
            }
            
            // All students are free at this time
            return true;
            
        } catch (Exception e) {
            System.err.println("Error checking student availability: " + e.getMessage());
            return true; // Default to available if we can't check
        }
    }
    
    /**
     * Simplified student busy check for schedule generation.
     * Checks if students enrolled in a section are already busy in OTHER classes at the proposed time.
     * @param sectionID The section being scheduled (e.g., "CS102-SEC1")
     * @param day Day of the week
     * @param startTime Start time
     * @param endTime End time
     * @param existingSchedules All existing schedules
     * @return true if students are busy (conflict), false if students are free
     */
    private static boolean areStudentsInSectionBusy(String sectionID, String day, String startTime, String endTime, List<Schedule> existingSchedules) {
        try {
            // Load enrollment data
            String enrollmentFilePath = FilePathResolver.resolveEnrollmentFilePath();
            File enrollmentFile = new File(enrollmentFilePath);
            if (!enrollmentFile.exists()) {
                return false; // No enrollments = no students = not busy
            }
            
            EnrollmentFileLoader enrollmentLoader = new EnrollmentFileLoader();
            enrollmentLoader.load(enrollmentFilePath);
            
            // Get students enrolled in THIS section
            List<String> studentsInSection = new ArrayList<>();
            for (Enrollment enrollment : enrollmentLoader.getAllEnrollments()) {
                if (enrollment.getSectionID().equals(sectionID) && "ENROLLED".equals(enrollment.getStatus())) {
                    studentsInSection.add(enrollment.getStudentID());
                }
            }
            
            if (studentsInSection.isEmpty()) {
                return false; // No students enrolled yet = not busy
            }
            
            // Check if ANY student is already scheduled in ANOTHER class at this time
            for (String studentID : studentsInSection) {
                for (Schedule existingSchedule : existingSchedules) {
                    // Skip if this is the same section (we're not conflicting with ourselves)
                    if (existingSchedule.getCourseID().equals(sectionID)) {
                        continue;
                    }
                    
                    // Check if this student is enrolled in the existing schedule's section
                    boolean studentEnrolledInExistingSchedule = false;
                    for (Enrollment enrollment : enrollmentLoader.getAllEnrollments()) {
                        if (enrollment.getStudentID().equals(studentID) &&
                            enrollment.getSectionID().equals(existingSchedule.getCourseID()) &&
                            "ENROLLED".equals(enrollment.getStatus())) {
                            studentEnrolledInExistingSchedule = true;
                            break;
                        }
                    }
                    
                    // If student is in that schedule AND it conflicts with our time
                    if (studentEnrolledInExistingSchedule && existingSchedule.getDay().equals(day) &&
                        conflictsWithScheduleAndBreak(existingSchedule, day, startTime, endTime)) {
                        System.out.println("  X STUDENT CONFLICT: " + studentID + " already has " + 
                                         existingSchedule.getCourseID() + " at " + day + " " + startTime + 
                                         ", cannot schedule " + sectionID);
                        return true; // Student is busy - CONFLICT!
                    }
                }
            }
            
            // All students are free
            return false;
            
        } catch (Exception e) {
            System.err.println("Error checking if students are busy: " + e.getMessage());
            return false; // Default to not busy if we can't check
        }
    }
    
    /**
     * Enhanced conflict detection with mandatory 30-minute break enforcement.
     * NOW CHECKS SPECIFIC ROOM AND TEACHER AVAILABILITY (not just any conflict)
     * @param existingSchedules All existing schedules
     * @param day Proposed day
     * @param startTime Proposed start time
     * @param endTime Proposed end time
     * @param roomID The specific room to check
     * @param teacherName The specific teacher to check
     * @return true if conflict exists for this specific room OR teacher
     */
    private static boolean hasConflictWithMandatoryBreak(List<Schedule> existingSchedules, String day, String startTime, String endTime, String roomID, String teacherName) {
        for (Schedule existing : existingSchedules) {
            // ONLY trigger a conflict if the DAY matches AND (the ROOM is the same OR the TEACHER is the same)
            if (existing.getDay().equals(day) && 
               (existing.getRoom().equals(roomID) || existing.getTeacherName().equals(teacherName))) {
                
                if (conflictsWithScheduleAndBreak(existing, day, startTime, endTime)) {
                    return true; // Conflict found for this specific room or teacher
                }
            }
        }
        return false; // Time is busy for others, but THIS room and teacher are free!
    }
    
    /**
     * Helper method to find an available room for a course at a specific time
     * @param day Day of the week
     * @param startTime Start time
     * @param endTime End time
     * @param isLab Whether lab room is needed
     * @param existingSchedules All existing schedules
     * @return Available room ID or null if none available
     */
    private static String findAvailableRoom(String day, String startTime, String endTime, boolean isLab, List<Schedule> existingSchedules) {
        try {
            RoomFileLoader roomLoader = new RoomFileLoader();
            roomLoader.load(FilePathResolver.resolveRoomsFilePath());
            
            for (Rooms room : roomLoader.getAllRooms()) {
                if (room.isLabRoom() == isLab && room.getCapacity() >= DEFAULT_MAX_CAPACITY) {
                    // Check if this specific room is available
                    boolean roomBusy = false;
                    for (Schedule schedule : existingSchedules) {
                        if (schedule.getRoom().equals(room.getRoomID()) && 
                            schedule.getDay().equals(day) &&
                            conflictsWithScheduleAndBreak(schedule, day, startTime, endTime)) {
                            roomBusy = true;
                            break;
                        }
                    }
                    if (!roomBusy) {
                        return room.getRoomID();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding available room: " + e.getMessage());
        }
        return null; // No room available
    }
    
    /**
     * Helper method to find an available teacher for a course at a specific time
     * @param courseID Course to teach
     * @param day Day of the week
     * @param startTime Start time
     * @param endTime End time
     * @param existingSchedules All existing schedules
     * @return Available teacher name or null if none available
     */
    private static String findAvailableTeacher(String courseID, String day, String startTime, String endTime, List<Schedule> existingSchedules) {
        try {
            TeacherFileLoader teacherLoader = new TeacherFileLoader();
            teacherLoader.load(FilePathResolver.resolveTeachersFilePath());
            
            for (Teachers teacher : teacherLoader.getAllTeachers()) {
                if (teacher.canTeachSubject(courseID)) {
                    // Check if this specific teacher is available
                    boolean teacherBusy = false;
                    for (Schedule schedule : existingSchedules) {
                        if (schedule.getTeacherName().equals(teacher.getTeacherName()) && 
                            schedule.getDay().equals(day) &&
                            conflictsWithScheduleAndBreak(schedule, day, startTime, endTime)) {
                            teacherBusy = true;
                            break;
                        }
                    }
                    if (!teacherBusy) {
                        return teacher.getTeacherName();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding available teacher: " + e.getMessage());
        }
        return null; // No teacher available
    }
    
    /**
     * Checks if teacher is available for a specific time slot.
     * @param teacherName Teacher to check
     * @param day Day of the week
     * @param startTime Start time
     * @param endTime End time
     * @param existingSchedules All existing schedules
     * @return true if teacher is available
     */
    private static boolean isTeacherAvailable(String teacherName, String day, String startTime, String endTime, List<Schedule> existingSchedules) {
        for (Schedule schedule : existingSchedules) {
            if (schedule.getTeacherName().equals(teacherName) &&
                conflictsWithScheduleAndBreak(schedule, day, startTime, endTime)) {
                return false;
            }
        }
        return true;
    }
   
    /**
     * Enhanced conflict detection that includes mandatory 30-minute break enforcement.
     * @param existing Existing schedule to check against
     * @param day Proposed day
     * @param startTime Proposed start time
     * @param endTime Proposed end time
     * @return true if there's a conflict or break violation
     */
    private static boolean conflictsWithScheduleAndBreak(Schedule existing, String day, String startTime, String endTime) {
        if (!existing.getDay().equals(day)) {
            return false;
        }
        try {
            LocalTime existingStart = LocalTime.parse(existing.getStartTime(), TIME_FORMATTER);
            LocalTime existingEnd = LocalTime.parse(existing.getEndTime(), TIME_FORMATTER);
            LocalTime proposedStart = LocalTime.parse(startTime, TIME_FORMATTER);
            LocalTime proposedEnd = LocalTime.parse(endTime, TIME_FORMATTER);
            
            // MANDATORY 30-MINUTE BREAK ENFORCEMENT
            // Check if proposed class starts too soon after existing class ends
            if (proposedStart.isAfter(existingStart) && proposedStart.isBefore(existingEnd.plusMinutes(30))) {
                return true; // Violates 30-minute break requirement
            }
            
            // Check if existing class starts too soon after proposed class ends
            if (existingStart.isAfter(proposedStart) && existingStart.isBefore(proposedEnd.plusMinutes(30))) {
                return true; // Violates 30-minute break requirement
            }
            
            // Standard overlap detection
            return !(proposedEnd.isBefore(existingStart) || proposedEnd.equals(existingStart) ||
                    proposedStart.isAfter(existingEnd) || proposedStart.equals(existingEnd));
        } catch (Exception e) {
            System.err.println("Error parsing times in break-enforced conflict check: " + e.getMessage());
            return false;
        }
    }
   
    private static List<Schedule> getExistingSchedules(String courseID) {
        List<Schedule> schedules = new ArrayList<>();
        try {
            String scheduleFilePath = FilePathResolver.resolveScheduleFilePath();
            File scheduleFile = new File(scheduleFilePath);
            if (scheduleFile.exists()) {
                ScheduleFileLoader loader = new ScheduleFileLoader();
                loader.load(scheduleFilePath);
               
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
            String scheduleFilePath = FilePathResolver.resolveScheduleFilePath();
            File f = new File(scheduleFilePath);
            if (f.exists()) {
                ScheduleFileLoader loader = new ScheduleFileLoader();
                loader.load(scheduleFilePath);
                for (Schedule s : loader.getAllSchedules()) {
                    String id = s.getScheduleID();
                    if (id.startsWith("SCH-")) {
                        int num = Integer.parseInt(id.substring(4));
                        if (num > maxId) maxId = num;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error generating schedule ID: " + e.getMessage());
        }
        return String.format("SCH-%03d", maxId + 1);
    }
    
    /**
     * Check if a section already has a schedule assigned
     */
    private static boolean sectionHasSchedule(String sectionID) {
        try {
            String scheduleFilePath = FilePathResolver.resolveScheduleFilePath();
            File scheduleFile = new File(scheduleFilePath);
            if (scheduleFile.exists()) {
                ScheduleFileLoader loader = new ScheduleFileLoader();
                loader.load(scheduleFilePath);
                for (Schedule s : loader.getAllSchedules()) {
                    if (s.getCourseID().equals(sectionID)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking section schedule: " + e.getMessage());
        }
        return false;
    }
   
    private static void saveSchedule(Schedule schedule) {
        try {
            String filePath = FilePathResolver.resolveScheduleFilePath();
            ScheduleFileSaver saver = new ScheduleFileSaver();
            ScheduleFileLoader loader = new ScheduleFileLoader();
            loader.load(filePath);
            List<Schedule> allSchedules = new ArrayList<>(loader.getAllSchedules());
            allSchedules.add(schedule);
            saver.save(filePath, allSchedules);
        } catch (Exception e) {
            System.err.println("Error saving schedule: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Legacy conflict detection method (kept for compatibility).
     * Now uses enhanced break-enforced conflict detection.
     */
    private static boolean conflictsWithSchedule(Schedule existing, String day, String startTime, String endTime) {
        return conflictsWithScheduleAndBreak(existing, day, startTime, endTime);
    }
}





