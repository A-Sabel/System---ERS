package ers.group;

public interface FileSaver {
    void save(String filePath, java.util.List<?> data) throws java.io.IOException;
}

abstract class BaseFileSaver<T> implements FileSaver {
    
    protected abstract String formatLine(T item);
    
    @Override
    public void save(String filePath, java.util.List<?> data) throws java.io.IOException {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(filePath))) {
            for (Object obj : data) {
                @SuppressWarnings("unchecked")
                T item = (T) obj;
                String line = formatLine(item);
                if (line != null && !line.isEmpty()) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }
    
    protected void update(String filePath, java.util.List<T> data, java.util.function.Predicate<T> predicate, T updatedItem) throws java.io.IOException {
        for (int i = 0; i < data.size(); i++) {
            if (predicate.test(data.get(i))) {
                data.set(i, updatedItem);
                break;
            }
        }
        save(filePath, data);
    }
    
    protected void delete(String filePath, java.util.List<T> data, java.util.function.Predicate<T> predicate) throws java.io.IOException {
        data.removeIf(predicate);
        save(filePath, data);
    }
}

class StudentFileSaver extends BaseFileSaver<Student> {
    @Override
    protected String formatLine(Student s) {
        String subjects = String.join(";", s.getSubjectsEnrolled());
        return String.join(",",
            s.getStudentID(),
            s.getStudentName(),
            String.valueOf(s.getAge()),
            s.getDateOfBirth(),
            s.getYearLevel(),
            s.getSection(),
            s.getStudentType(),
            subjects,
            String.valueOf(s.getGwa()),
            s.getEmail(),
            s.getPhoneNumber(),
            s.getGender(),
            s.getAddress(),
            s.getFathersName(),
            s.getMothersName(),
            s.getGuardiansPhoneNumber()
        );
    }
}

class ScheduleFileSaver extends BaseFileSaver<Schedule> {
    @Override
    protected String formatLine(Schedule s) {
        return String.join(",",
            s.getScheduleID(),
            s.getCourseID(),
            s.getRoom(),
            s.getDay(),
            s.getStartTime(),
            s.getEndTime(),
            s.getTeacherName()
        );
    }
}

class SectionFileSaver extends BaseFileSaver<Section> {
    @Override
    protected String formatLine(Section sec) {
        String studentList = String.join(";", sec.getEnrolledStudentIDs());
        return String.join(",",
            sec.getSectionID(),
            sec.getSubject().getCourseSubjectID(),
            sec.getSchedule().getScheduleID(),
            sec.getInstructor().getTeacherName(),
            String.valueOf(sec.getCapacityLimit()),
            String.valueOf(sec.getCurrentEnrollment()),
            studentList
        );
    }
}

class CourseSubjectFileSaver extends BaseFileSaver<CourseSubject> {
    @Override
    protected String formatLine(CourseSubject course) {
        String prereqs = course.getPrerequisitesString();
        return String.join(",",
            course.getCourseSubjectID(),
            course.getCourseSubjectName(),
            String.valueOf(course.getUnits()),
            String.valueOf(course.getStudentCount()),
            String.valueOf(course.isLabRoom()),
            prereqs,
            String.valueOf(course.getYearLevel()),
            String.valueOf(course.getSemester())
        );
    }
}

class TeachersFileSaver extends BaseFileSaver<Teachers> {
    @Override
    protected String formatLine(Teachers teacher) {
        String subjects = String.join(";", teacher.getQualifiedSubjectIDs());
        String schedules = String.join(";", teacher.getAssignedSchedules());
        return String.join(",",
            teacher.getTeacherID(),
            teacher.getTeacherName(),
            subjects,
            schedules
        );
    }
}

class RoomsFileSaver extends BaseFileSaver<Rooms> {
    @Override
    protected String formatLine(Rooms room) {
        return String.join(",",
            room.getRoomID(),
            room.getRoomName(),
            String.valueOf(room.getCapacity())
        );
    }
}

class EnrollmentFileSaver extends BaseFileSaver<Enrollment> {
    @Override
    protected String formatLine(Enrollment enrollment) {
        // Format: enrollmentID,studentID,courseID,yearLevel,semester,status,sectionID
        return String.join(",",
            enrollment.getEnrollmentID(),
            enrollment.getStudentID(),
            enrollment.getCourseID(),
            enrollment.getYearLevel(),
            enrollment.getSemester(),
            enrollment.getStatus(),
            enrollment.getSectionID() != null ? enrollment.getSectionID() : ""
        );
    }
}

class MarksheetFileSaver extends BaseFileSaver<Marksheet> {
    @Override
    protected String formatLine(Marksheet m) {
        // Format: ID, StudentID, Semester, Course1, Score1, Course2, Score2, Course3, Score3, Course4, Score4, Course5, Score5, Average
        StringBuilder sb = new StringBuilder();
        
        // Generate ID (e.g., MRK-001)
        sb.append("MRK-").append(String.format("%03d", System.currentTimeMillis() % 1000)).append(",");
        sb.append(m.getStudentID()).append(",");
        sb.append(m.getSemester()).append(",");
        
        // Add 5 course-score pairs
        String[] subjects = m.getSubjects();
        double[] marks = m.getMarks();
        for (int i = 0; i < 5; i++) {
            sb.append(subjects[i] != null ? subjects[i] : "").append(",");
            sb.append(marks[i]).append(",");
        }
        
        // Calculate and add average
        double average = 0.0;
        int count = 0;
        for (double mark : marks) {
            if (mark > 0) {
                average += mark;
                count++;
            }
        }
        average = count > 0 ? average / count : 0.0;
        sb.append(String.format("%.2f", average));
        
        return sb.toString();
    }
}
