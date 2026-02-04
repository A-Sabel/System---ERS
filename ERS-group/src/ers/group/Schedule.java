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

    @Override
    public String toString() {
        return "Schedule: " + courseID + " in " + room + " on " + day + " from " + startTime + " to " + endTime + ", Teacher: " + teacherName;
    }
}
