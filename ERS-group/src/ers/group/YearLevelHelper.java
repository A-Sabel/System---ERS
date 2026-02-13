package ers.group;

import java.util.HashMap;
import java.util.Map;

public class YearLevelHelper {
    private Map<String, Student> studentMap;

    public YearLevelHelper(Map<String, Student> studentMap) {
        this.studentMap = studentMap;
    }

    public String getYearLevel(String studentID) {
        Student student = studentMap.get(studentID);
        if (student != null) {
            return student.getYearLevel();
        }
        return "";
    }
}
