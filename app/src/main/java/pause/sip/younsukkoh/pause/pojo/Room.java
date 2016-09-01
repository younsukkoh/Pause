package pause.sip.younsukkoh.pause.pojo;

import java.util.HashMap;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class Room {

    String roomId;
    //who
    HashMap<String, Object> members;
    int numberOfMembers;
    //what
    String name;
    //when
    long timeCreated;
    long timeUpdated;
    //where
    // N.A.
    //why
    // N.A.

    public Room() {
    }

    public Room(String roomId, long timeCreated, HashMap<String, Object> members, int numberOfMembers) {
        this.roomId = roomId;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeCreated;
        this.members = members;
        this.numberOfMembers = numberOfMembers;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(long timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public HashMap<String, Object> getMembers() {
        return members;
    }

    public void setMembers(HashMap<String, Object> members) {
        this.members = members;
    }

    public int getNumberOfMembers() {
        return numberOfMembers;
    }

    public void setNumberOfMembers(int numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
    }
}
