package pause.sip.younsukkoh.pause.pojo;

import java.util.HashMap;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class Memory {

    HashMap<Integer, String> episodes;
    String description;
    HashMap<Integer, String> tags;
    long timeCreated;
    long longitude;
    long latitude;
    String location;

    public Memory() {
    }

    public Memory(HashMap<Integer, String> episodes, String description, HashMap<Integer, String> tags, long timeCreated, long longitude, long latitude, String location) {
        this.episodes = episodes;
        this.description = description;
        this.tags = tags;
        this.timeCreated = timeCreated;
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
    }

    public HashMap<Integer, String> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(HashMap<Integer, String> episodes) {
        this.episodes = episodes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<Integer, String> getTags() {
        return tags;
    }

    public void setTags(HashMap<Integer, String> tags) {
        this.tags = tags;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
