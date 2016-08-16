package pause.sip.younsukkoh.pause.pojo;

import com.google.firebase.database.Exclude;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class Memory {

    String memoryId;
    //who
    HashMap<String, Object> people;
    int numberOfPeople;
    //what
    String title;
    HashMap<String, Object> episodes;
    int numberOfEpisodes;
    //when
    long timeCreated;
    //where
    String location;
    double longitude;
    double latitude;
    //why
    String description;

    public Memory() {
    }

    /**
     * Used for creating a memory from a scratch with a single photo
     * @param memoryId
     * @param currentUser
     * @param episodeUrl
     * @param timeCreated
     * @param location
     * @param longitude
     * @param latitude
     */
    public Memory(String memoryId, String currentUser, String episodeUrl, long timeCreated, String location, double longitude, double latitude) {
        this.memoryId = memoryId;

        ArrayList<String> listOfPeople = new ArrayList();
        listOfPeople.add(currentUser);
        HashMap<String, Object> people = new HashMap<>();
        people.put(Constants.LIST, listOfPeople);
        this.people = people;

        this.numberOfPeople = 1;

        ArrayList<String> listOfEpisodes = new ArrayList<>();
        listOfEpisodes.add(episodeUrl);
        HashMap<String, Object> episodes = new HashMap<>();
        episodes.put(Constants.LIST, listOfEpisodes);
        this.episodes = episodes;

        this.numberOfEpisodes = 1;

        this.timeCreated = timeCreated;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(String memoryId) {
        this.memoryId = memoryId;
    }

    public HashMap<String, Object> getPeople() {
        return people;
    }

    public void setPeople(HashMap<String, Object> people) {
        this.people = people;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public HashMap<String, Object> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(HashMap<String, Object> episodes) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
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
