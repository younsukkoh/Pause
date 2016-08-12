package pause.sip.younsukkoh.pause.pojo;

import java.util.HashMap;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class Episode {

    //who
    HashMap<Integer, String> tags;
    //what
    String downloadUrl;
    //when
    long timeAdded;
    //where
    String location;
    long longitude;
    long latitude;
    //why
    String description;

    public Episode() {
    }

    public Episode(String downloadUrl, long timeAdded, String description, HashMap<Integer, String> tags, long longitude, long latitude, String location) {
        this.downloadUrl = downloadUrl;
        this.timeAdded = timeAdded;
        this.description = description;
        this.tags = tags;
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public long getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(long timeAdded) {
        this.timeAdded = timeAdded;
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
