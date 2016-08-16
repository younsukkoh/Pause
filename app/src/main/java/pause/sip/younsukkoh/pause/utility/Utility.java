package pause.sip.younsukkoh.pause.utility;

import java.text.SimpleDateFormat;

/**
 * Created by Younsuk on 7/27/2016.
 */
public class Utility {

    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("MM/dd/yy, h:mm a");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd, yyyy");
    public static final SimpleDateFormat DATE_TIME_LABEL = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");

    public static String encodeEmail(String email) {
        return email.replace(".", ",");
    }
    public static String decodeEmail(String encodedEmail) { return encodedEmail.replace(",", "."); }

}
