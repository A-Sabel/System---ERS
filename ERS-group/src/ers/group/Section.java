package ers.group;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;




public class Section {
    private final String sectionID;
    private CourseSubject subject;
    private Teachers instructor;
    private Schedule schedule;
   
    // Tracks students currently in this specific section
    private ArrayList<String> enrolledStudentIDs;
    private final int capacityLimit;


    public Section(String sectionID, CourseSubject subject, int sectionCapacity) {
        this.sectionID = sectionID;
        this.subject = subject;
        this.capacityLimit = sectionCapacity;
        this.enrolledStudentIDs = new ArrayList<>();
    }


    // Methods
    public boolean isFull() {
        return enrolledStudentIDs.size() >= capacityLimit;
    }
   
    public boolean addStudent(String studentID) {
        if (!isFull() && !enrolledStudentIDs.contains(studentID)) {
            enrolledStudentIDs.add(studentID);
            return true;
        }
        return false;
    }
   
    public int getCurrentEnrollment() {
        return enrolledStudentIDs.size();
    }


    public static ArrayList<Section> generateSections(CourseSubject subject, int totalStudents, int sectionCapacity) {
        ArrayList<Section> newSections = new ArrayList<>();
        //calculate number of sections needed
        int numRequired = (totalStudents + sectionCapacity - 1) / sectionCapacity;


        for (int i = 1; i <= numRequired; i++) {
            String secID = subject.getCourseSubjectID() + "-SEC" + i;
            newSections.add(new Section(secID, subject, sectionCapacity));
        }
       
        return newSections;
    }


    // Getters and Setters
    public CourseSubject getSubject() { return subject; }
    public void setSchedule(Schedule schedule) { this.schedule = schedule; }
    public void setInstructor(Teachers instructor) { this.instructor = instructor; }
    public String getSectionID() { return sectionID; }
    public Schedule getSchedule() { return schedule; }
    public Teachers getInstructor() { return instructor; }
    public ArrayList<String> getEnrolledStudentIDs() { return enrolledStudentIDs; }
    public int getCapacityLimit() { return capacityLimit; }
   
    @Override
    public String toString() {
        return String.format("%s - %s | Enrolled: %d/%d | Instructor: %s",
            sectionID, subject.getCourseSubjectName(), getCurrentEnrollment(), capacityLimit,
            instructor != null ? instructor.getTeacherName() : "Unassigned");
    }


    public void saveSectionsToFile(ArrayList<Section> sections, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Section sec : sections) {
                ArrayList<String> students = sec.getEnrolledStudentIDs();
                String studentList = String.join(";", students);
                String teacherID = (sec.getInstructor() != null) ? sec.getInstructor().getTeacherID() : "NULL";
                String scheduleID = (sec.getSchedule() != null) ? sec.getSchedule().getScheduleID() : "NULL";
                String roomID = (sec.getSchedule() != null) ? sec.getSchedule().getRoom() : "NULL";
               
                // Format: SectionID|CourseID|TeacherID|ScheduleID|RoomID|StudentCount|StudentList
                writer.printf("%s|%s|%s|%s|%s|%d|%s\n",
                    sec.getSectionID(),
                    sec.getSubject().getCourseSubjectID(),
                    teacherID,
                    scheduleID,
                    roomID,
                    sec.getCurrentEnrollment(),
                    studentList);
            }
            System.out.println("✓ Sections saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving sections to file: " + e.getMessage());
        }
    }
}



