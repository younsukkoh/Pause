package pause.sip.younsukkoh.pause.pojo;

import java.util.HashMap;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class Episode {

    String episodeId;
    //who
    HashMap<Integer, String> tags;
    //what
    String downloadUrl;
    //when
    long timeAdded;
    //where
    String location;
    double longitude;
    double latitude;
    //why
    String description;

    public Episode() {
    }

    public Episode(String episodeId, String downloadUrl, long timeAdded, String location, double longitude, double latitude) {
        this.episodeId = episodeId;
        this.tags = tags;
        this.downloadUrl = downloadUrl;
        this.timeAdded = timeAdded;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }

    public HashMap<Integer, String> getTags() {
        return tags;
    }

    public void setTags(HashMap<Integer, String> tags) {
        this.tags = tags;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
