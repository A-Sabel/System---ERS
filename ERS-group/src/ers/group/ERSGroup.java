/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ers.group;

/**
 *
 * @author Andrea Ysabela
 */
public class ERSGroup {
 

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileLoader loader = new CourseSubjectFileLoader();
        loader.load("ERS-group/src/ers/group/master files/courseSubject.txt");
        
        CourseSubjectFileLoader courseLoader = (CourseSubjectFileLoader) loader;
        for (CourseSubject s : courseLoader.getAllSubjects()) {
            System.out.println("Subject: " + s.getCourseSubjectID() + " " + s.getCourseSubjectName() + "| Units: " + s.getUnits() + 
                "\nPrerequisites: " + s.getPrerequisitesString() + "| SY: " + s.getYearLevel() + " - " + s.getSemester() + "\n");
        }
    }

}