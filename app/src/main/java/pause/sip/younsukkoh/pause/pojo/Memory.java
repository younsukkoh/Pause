package pause.sip.younsukkoh.pause.pojo;

import java.util.HashMap;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class Memory {

    String memoryId;
    //who
//    HashMap people;
//    int numberOfPeople;
    //what
    String title;
//    HashMap episodes;
//    int numberOfEpisodes;
    //when
    long timeCreated;
    //where
    String location;
    long longitude;
    long latitude;
    //why
    String description;

    public Memory() {
    }

    public Memory(String memoryId, String title, long timeCreated, String location, long longitude, long latitude, String description) {
        this.memoryId = memoryId;
//        this.people = people;
//        this.numberOfPeople = numberOfPeople;
//        this.episodes = episodes;
//        this.numberOfEpisodes = numberOfEpisodes;
        this.title = title;
        this.timeCreated = timeCreated;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
    }

    public String getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(String memoryId) {
        this.memoryId = memoryId;
    }

//    public HashMap<String, String> getPeople() {
//        return people;
//    }
//
//    public void setPeople(HashMap<String, String> people) {
//        this.people = people;
//    }
//
//    public int getNumberOfPeople() {
//        return numberOfPeople;
//    }
//
//    public void setNumberOfPeople(int numberOfPeople) {
//        this.numberOfPeople = numberOfPeople;
//    }
//
//    public HashMap<String, String> getEpisodes() {
//        return episodes;
//    }
//
//    public void setEpisodes(HashMap<String, String> episodes) {
//        this.episodes = episodes;
//    }
//
//    public int getNumberOfEpisodes() {
//        return numberOfEpisodes;
//    }
//
//    public void setNumberOfEpisodes(int numberOfEpisodes) {
//        this.numberOfEpisodes = numberOfEpisodes;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
