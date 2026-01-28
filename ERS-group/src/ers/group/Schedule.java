package ers.group;

public class Schedule {
    private final String day; // e.g., "Mon"
    private final int startHour; // 24-hour integer, e.g., 8
    private final int endHour;   // exclusive end hour

    public Schedule(String day, int startHour, int endHour) {
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public String getDay() { return day; }
    public int getStartHour() { return startHour; }
    public int getEndHour() { return endHour; }
}
