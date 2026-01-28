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
    SubjectLoader loader = new SubjectLoader();
    loader.loadFromTextFile("ERS-group/src/ers/group/master files/courseSubject.txt");
    for (CourseSubject s : loader.getAllSubjects()) {
        System.out.println("Subject: " + s.getCourseSubjectName() + "| Units: " + s.getUnits() );
        }
    }

}