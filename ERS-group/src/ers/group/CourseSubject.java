/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ers.group;

import.java.io.*;
import java.util.*;

/**
 *
 * @author Andrea Ysabela
 */
public class CourseSubject {
    //Identity
    private String courseSubjectID;
    private String courseSubjectName;

    //Academic Constraints
    private int units;
    private int studentCount;
    private boolean isLabRoom;
    private boolean isScheduled = false;

    private ArrayList<CourseSubject> prerequisites;

    //Constructor
    public CourseSubject(String courseSubjectID, String courseSubjectName, 
                            int units, int studentCount, boolean isLabRoom) 
    {
        this.courseSubjectID = courseSubjectID;
        this.courseSubjectName = courseSubjectName;
        this.units = units;
        this.studentCount = studentCount;
        this.isLabRoom = isLabRoom;
    }

    //Getters and Setters
    public String getCourseSubjectID() {
        return courseSubjectID;
    }
    public String getCourseSubjectName() {
        return courseSubjectName;
    }
    public int getUnits() {
        return units;
    }
    public int getStudentCount() {
        return studentCount;
    }
    public boolean isIsLabRoom() {
        return isLabRoom;
    }
    public boolean isIsScheduled() {
        return isScheduled;
    }
    public void addPrerequisite(CourseSubject prerequisite) {
        if (prerequisites == null) {
            prerequisites = new ArrayList<>();
        }
        this.prerequisites.add(prerequisite);
    }

}
