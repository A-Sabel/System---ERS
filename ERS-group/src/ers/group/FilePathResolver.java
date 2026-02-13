package ers.group;

import java.io.File;

/**
 * Utility class to resolve file paths across different possible locations.
 * Centralizes path resolution logic used throughout the ERS application.
 */
public class FilePathResolver {

    /**
     * Resolve the path to student.txt
     * @return The first existing path, or the default path if none exist
     */
    public static String resolveStudentFilePath() {
        String[] possiblePaths = {
            "ERS-group/src/ers/group/master files/student.txt",
            "src/ers/group/master files/student.txt",
            "master files/student.txt",
            "student.txt",
            "ERS-group/student.txt",
            "../student.txt"
        };
        return resolveFilePath(possiblePaths);
    }

    /**
     * Resolve the path to enrollment.txt
     * @return The first existing path, or the default path if none exist
     */
    public static String resolveEnrollmentFilePath() {
        String[] possiblePaths = {
            "ERS-group/src/ers/group/master files/enrollment.txt",
            "src/ers/group/master files/enrollment.txt",
            "master files/enrollment.txt",
            "enrollment.txt",
            "ERS-group/enrollment.txt",
            "../enrollment.txt"
        };
        return resolveFilePath(possiblePaths);
    }

    /**
     * Resolve the path to courseSubject.txt
     * @return The first existing path, or the default path if none exist
     */
    public static String resolveCourseSubjectFilePath() {
        String[] possiblePaths = {
            "ERS-group/src/ers/group/master files/courseSubject.txt",
            "src/ers/group/master files/courseSubject.txt",
            "master files/courseSubject.txt",
            "courseSubject.txt",
            "ERS-group/courseSubject.txt",
            "../courseSubject.txt"
        };
        return resolveFilePath(possiblePaths);
    }

    /**
     * Resolve the path to Schedule.txt
     * @return The first existing path, or the default path if none exist
     */
    public static String resolveScheduleFilePath() {
        String[] possiblePaths = {
            "ERS-group/src/ers/group/master files/Schedule.txt",
            "src/ers/group/master files/Schedule.txt",
            "master files/Schedule.txt",
            "Schedule.txt",
            "ERS-group/Schedule.txt",
            "../Schedule.txt"
        };
        return resolveFilePath(possiblePaths);
    }

    /**
     * Resolve the path to marksheet.txt
     * @return The first existing path, or the default path if none exist
     */
    public static String resolveMarksheetFilePath() {
        String[] possiblePaths = {
            "ERS-group/src/ers/group/master files/marksheet.txt",
            "src/ers/group/master files/marksheet.txt",
            "master files/marksheet.txt",
            "marksheet.txt",
            "ERS-group/marksheet.txt",
            "../marksheet.txt"
        };
        return resolveFilePath(possiblePaths);
    }

    /**
     * Resolve the path to Teachers.txt
     * @return The first existing path, or the default path if none exist
     */
    public static String resolveTeachersFilePath() {
        String[] possiblePaths = {
            "ERS-group/src/ers/group/master files/Teachers.txt",
            "src/ers/group/master files/Teachers.txt",
            "master files/Teachers.txt",
            "Teachers.txt",
            "ERS-group/Teachers.txt",
            "../Teachers.txt"
        };
        return resolveFilePath(possiblePaths);
    }

    /**
     * Resolve the path to Rooms.txt
     * @return The first existing path, or the default path if none exist
     */
    public static String resolveRoomsFilePath() {
        String[] possiblePaths = {
            "ERS-group/src/ers/group/master files/Rooms.txt",
            "src/ers/group/master files/Rooms.txt",
            "master files/Rooms.txt",
            "Rooms.txt",
            "ERS-group/Rooms.txt",
            "../Rooms.txt"
        };
        return resolveFilePath(possiblePaths);
    }

    /**
     * Generic method to resolve a file path from an array of possible paths
     * Tries each path in order and returns the first one that exists
     * 
     * @param possiblePaths Array of possible file paths to try
     * @return The first existing path, or the first path if none exist
     */
    public static String resolveFilePath(String[] possiblePaths) {
        if (possiblePaths == null || possiblePaths.length == 0) {
            throw new IllegalArgumentException("possiblePaths cannot be null or empty");
        }
        
        for (String path : possiblePaths) {
            File f = new File(path);
            if (f.exists()) {
                return path;
            }
        }
        
        // If no path exists, return the first one as the default
        return possiblePaths[0];
    }

    /**
     * Resolve path with writable directory check
     * Finds the first path whose parent directory exists or can be created
     * 
     * @param possiblePaths Array of possible file paths to try
     * @return The first writable path, or the first path if none are writable
     */
    public static String resolveWritablePath(String[] possiblePaths) {
        if (possiblePaths == null || possiblePaths.length == 0) {
            throw new IllegalArgumentException("possiblePaths cannot be null or empty");
        }
        
        // First, check if any file already exists
        for (String path : possiblePaths) {
            File f = new File(path);
            if (f.exists()) {
                return path;
            }
        }
        
        // If no file exists, find a path where we can create the file
        for (String path : possiblePaths) {
            File f = new File(path);
            File dir = f.getParentFile();
            if (dir != null && (dir.exists() || dir.mkdirs())) {
                return path;
            }
        }
        
        // If all else fails, return the first path
        return possiblePaths[0];
    }
}
