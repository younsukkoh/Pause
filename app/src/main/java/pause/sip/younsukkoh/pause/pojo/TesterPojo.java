package pause.sip.younsukkoh.pause.pojo;

import java.util.HashMap;

/**
 * Created by Younsuk on 7/20/2016.
 */
public class TesterPojo {
    String name;
    HashMap<String, String> information;

    public TesterPojo() {
    }

    public TesterPojo(String name, HashMap<String, String> information) {
        this.name = name;
        this.information = information;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getInformation() {
        return information;
    }

    public void setInformation(HashMap<String, String> information) {
        this.information = information;
    }
}
