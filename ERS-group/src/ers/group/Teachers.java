/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ers.group;

import java.util.ArrayList;

/**
 *
 * @author Andrea Ysabela
 */
public class Teachers {
    private final String teacherID;
    private final String teacherName;

    private final ArrayList<CourseSubject> qualifiedSubjects;
    // The "Window" of time the teacher is actually at the school
    private final ArrayList<Schedule> availableSchedules;
    // The classes ALREADY given to this teacher by the auto-scheduler
    private final ArrayList<Schedule> assignedSchedules; 

    // Constructor
    public Teachers(String teacherID, String teacherName) {
        this.teacherID = teacherID;
        this.teacherName = teacherName;
        this.qualifiedSubjects = new ArrayList<>();
        this.availableSchedules = new ArrayList<>();
        this.assignedSchedules = new ArrayList<>();
    }
    
    // Methods
    public boolean isFitFor(CourseSubject subject, Schedule proposedSched) {
        if (!canTeachSubject(subject)) return false; // can teach?
        if (!isAvailableForSchedule(proposedSched)) return false; // is available?
        // no schedule conflicts?
        for (Schedule assigned : assignedSchedules) {
            if (assigned.conflictsWith(proposedSched)) {
                return false;
            }
        }
        return true;
    }

    public boolean canTeachSubject(CourseSubject subject) {
        return this.qualifiedSubjects.contains(subject);
    }

    public boolean isAvailableForSchedule(Schedule sched) {
        return this.availableSchedules.contains(sched);
    }

    // Actions
    public void assignSchedule(Schedule sched) {
        if (sched != null && !this.assignedSchedules.contains(sched)) {
            this.assignedSchedules.add(sched);
        }
    }
    public void addQualifiedSubject(CourseSubject subject) {
        if (subject != null && !this.qualifiedSubjects.contains(subject)) {
            this.qualifiedSubjects.add(subject);
        }
    }
    public void addAvailableSchedule(Schedule sched) {
        if (sched != null && !this.availableSchedules.contains(sched)) {
            this.availableSchedules.add(sched);
        }
    }
    // Getters
    public String getTeacherID() { return teacherID; }
    public String getTeacherName() { return teacherName; }
    public ArrayList<Schedule> getAssignedSchedules() { return assignedSchedules; }
    

}
