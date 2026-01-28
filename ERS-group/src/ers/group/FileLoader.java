package ers.group;

/**
 *
 * @author Andrea Ysabela
 */

import java.io.*;
import java.util.*;
public interface FileLoader {
    void load(String filePath);
}

abstract class BaseFileLoader implements FileLoader {
    protected void readFile(String filePath, LineProcessor processor) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) continue;
                processor.processLine(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find " + filePath);
        } catch (Exception e) {
            System.out.println("Error during file processing: " + e.getMessage());
        }
    }
    
    @FunctionalInterface
    protected interface LineProcessor {
        void processLine(String line);
    }
}

class CourseSubjectFileLoader extends BaseFileLoader {
    private final Map<String, CourseSubject> subjectMap = new LinkedHashMap<>();
    
    @Override
    public void load(String filePath) {
        readFile(filePath, line -> {
            String[] parts = line.split(",");
            // Format: ID, Name, Units, MaxStudents, isLab, Prerequisite, YearLevel, Semester
            String id = parts[0];
            String name = parts[1];
            int units = Integer.parseInt(parts[2]);
            int count = Integer.parseInt(parts[3]);
            boolean isLab = Boolean.parseBoolean(parts[4]);
            int yearLevel = Integer.parseInt(parts[6]);
            int semester = Integer.parseInt(parts[7]);
            CourseSubject subject = new CourseSubject(id, name, units, count, isLab, yearLevel, semester);
            subjectMap.put(id, subject);
        });
        
        // prerequisites
        readFile(filePath, line -> {
            String[] parts = line.split(",");
            String id = parts[0];
            String preReqData = parts[5];
            if (!preReqData.equalsIgnoreCase("NONE")) {
                String[] preReqIDs = preReqData.split(";");
                CourseSubject currentSub = subjectMap.get(id);
                if (currentSub != null) {
                    for (String prID : preReqIDs) {
                        CourseSubject prObject = subjectMap.get(prID);
                        if (prObject != null) {
                            currentSub.addPrerequisite(prObject);
                        }
                    }
                }
            }
        });
    }
    
    public Collection<CourseSubject> getAllSubjects() {
        return subjectMap.values();
    }
    
    public Map<String, CourseSubject> getSubjectMap() {
        return subjectMap;
    }
}

class TeacherFileLoader extends BaseFileLoader {
    private final Map<String, Teachers> teacherMap = new LinkedHashMap<>();
    
    @Override
    public void load(String filePath) {
        readFile(filePath, line -> {
            String[] parts = line.split(",");
            // Format: ID, Name, Subjects
            String id = parts[0];
            String name = parts[1];
            Teachers teacher = new Teachers(id, name);
            if (parts.length > 2) {
                String[] qualifiedSubjects = parts[2].split(";");
                for (String subjectID : qualifiedSubjects) {
                    teacher.addQualifiedSubject(subjectID);
                }
            }
            teacherMap.put(id, teacher);
        });
    }
    
    public Collection<Teachers> getAllTeachers() {
        return teacherMap.values();
    }
    
    public Map<String, Teachers> getTeacherMap() {
        return teacherMap;
    }
}

class RoomFileLoader extends BaseFileLoader {
    private final List<Rooms> allRooms = new ArrayList<>();
    
    @Override
    public void load(String filePath) {
        readFile(filePath, line -> {
            String[] parts = line.split(",");
            // Format: RoomID, RoomName, isLabRoom, Capacity
            String roomID = parts[0];
            String roomName = parts[1];
            boolean isLabRoom = Boolean.parseBoolean(parts[2]);
            int capacity = Integer.parseInt(parts[3]);
            Rooms room = new Rooms(roomID, roomName, isLabRoom, capacity);
            allRooms.add(room);
        });
    }

    public Collection<Rooms> getAllRooms() {
        return allRooms;
    }

    public Map<String, Rooms> getRoomMap() {
        Map<String, Rooms> roomMap = new LinkedHashMap<>();
        for (Rooms room : allRooms) {
            roomMap.put(room.getRoomID(), room);
        }
        return roomMap;
    }
}