package ers.group;

public class Schedule {
    // Fields
    private String scheduleID;
    private String courseID;
    private String room;
    private String day;
    private String startTime;
    private String endTime;
    private String teacherName;

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

    // Static method to assign a section to a student
    public static String assignSection(String courseID, String studentID, int maxCapacity) throws SectionFullException {
        int sectionNumber = (Math.abs(studentID.hashCode()) % 3) + 1;
        return courseID + "-SEC" + sectionNumber;
    }

    // Static method to assign schedule to section
    public static void assignScheduleToSection(String sectionID, String courseID, boolean isLabRoom) {
        // Placeholder for schedule assignment logic
    }

    // Exception for full sections
    public static class SectionFullException extends Exception {
        public SectionFullException(String message) {
            super(message);
        }
    }

    @Override
    public String toString() {
        return "Schedule: " + courseID + " in " + room + " on " + day + " from " + startTime + " to " + endTime + ", Teacher: " + teacherName;
    }
}
