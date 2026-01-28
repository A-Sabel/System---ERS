package ers.group;

public class Rooms {
    //Identity
    private final String roomID;
    private final String roomName;
    private boolean isLabRoom;

    private int roomCapacity;
    private boolean isAvailable = true;

    //Constructor
    public Rooms(String roomID, String roomName, boolean isLabRoom, int roomCapacity) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.isLabRoom = isLabRoom;
        this.roomCapacity = roomCapacity;
    }

    //Methods
    public void setAvailability(boolean availability) {
        this.isAvailable = availability;
    }
    
    public boolean isSpecialRoomFor(String courseSubjectID) {
        if (courseSubjectID.equals("CS304") && this.roomID.equals("NET301")) {
            return true;
        }
        else if ((courseSubjectID.equals("CS401") || courseSubjectID.equals("CS404")) 
            && this.roomID.equals("SYS401")) {
            return true;
        }
        return false;
    }

    //Getters and Setters
    public String getRoomID() { return this.roomID; }
    public String getRoomName() { return this.roomName; }   
    public boolean isLabRoom() { return this.isLabRoom; }
    public int getCapacity() { return this.roomCapacity; }
    public boolean isAvailable() { return this.isAvailable; }
}