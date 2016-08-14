package pause.sip.younsukkoh.pause.pojo;

import java.util.HashMap;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class Memory {

    String memoryId;
    //who
    HashMap<String, String> people;
    int numberOfPeople;
    //what
    String title;
    HashMap<String, String> episodes;
    int numberOfEpisodes;
    //when
    long timeCreated;
    //where
    String address;
    long longitude;
    long latitude;
    //why
    String description;

    public Memory() {
    }

    public Memory(String memoryId, HashMap<String, String> people, int numberOfPeople, HashMap<String, String> episodes, int numberOfEpisodes, long timeCreated, String address, long longitude, long latitude) {
        this.memoryId = memoryId;
        this.people = people;
        this.numberOfPeople = numberOfPeople;
        this.episodes = episodes;
        this.numberOfEpisodes = numberOfEpisodes;
        this.timeCreated = timeCreated;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(String memoryId) {
        this.memoryId = memoryId;
    }

    public HashMap<String, String> getPeople() {
        return people;
    }

    public void setPeople(HashMap<String, String> people) {
        this.people = people;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public HashMap<String, String> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(HashMap<String, String> episodes) {
        this.episodes = episodes;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(int numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
