package pause.sip.younsukkoh.pause.pojo;

import java.util.HashMap;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class Room {

    String roomId;
    //who
    HashMap<Integer, String> members;
    //what
    String name;
    //when
    long created;
    long updated;
    //where
    // N.A.
    //why
    // N.A.

    public Room() {
    }

    public Room(String roomId, String name, long created, long updated, HashMap<Integer, String> members) {
        this.roomId = roomId;
        this.name = name;
        this.created = created;
        this.updated = updated;
        this.members = members;
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

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public HashMap<Integer, String> getMembers() {
        return members;
    }

    public void setMembers(HashMap<Integer, String> members) {
        this.members = members;
    }
}
