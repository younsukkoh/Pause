package pause.sip.younsukkoh.pause.utility;

import pause.sip.younsukkoh.pause.BuildConfig;

/**
 * Created by Younsuk on 7/20/2016.
 */
public final class Constants {

    //Tag for Debugging
    public static final String TAG_DEBUG = "YOLO";

    //Firebase URL
    public static final String FIREBASE_ROOT_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_STORAGE_URL = "gs://pause-95ab1.appspot.com";

    //Firebase Database Reference Points
    public static final String USERS_INFO = "users_information_";
    public static final String USERS_ROOM = "users_room_";
    public static final String USERS_FRIENDS = "users_friends_";
    public static final String MY_ROOM = "my_room_";
    public static final String OUR_ROOM_INFO = "our_room_information_";
    public static final String OUR_ROOM_MEMORIES = "our_room_memories_";
    public static final String LIST = "list";

    //Firebase Storage Reference Points
    public static final String IMAGES = "images/";

    //Memory POJO
    public static final String PEOPLE = "people";
    public static final String NUMBER_OF_PEOPLE = "numberOfPeople";
    public static final String TITLE = "title";
    public static final String EPISODES = "episodes";
    public static final String NUMBER_OF_EPISODES = "numberOfEpisodes";
    public static final String TIME_CREATED = "timeCreated";
    public static final String LOCATION = "location";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String DESCRIPTION = "description";

    //Shared Preference
    public static final String SP_ENCODED_USER_EMAIL = "sp_encoded_user_email";

    //Intent Extras
    public static final String EXTRA_USER_INFORMATION = "extra_user_information";
    public static final String EXTRA_USER_BIRTHDAY = "extra_user_birthday";
    public static final String EXTRA_USER_ENCODED_EMAIL = "extra_user_encoded_email";
    public static final String EXTRA_MEMORY_ID = "extra_memory_id";
    public static final String EXTRA_EPISODE_URL = "extra_episode_url";
    public static final String EXTRA_DATE_TIME = "extra_date_time";

    //Bundle Argument Extras
    public static final String ARG_USER_ENCODED_EMAIL = "arg_user_encoded_email";
    public static final String ARG_MEMORY_ID = "arg_memory_id";
    public static final String ARG_EPISODE_URL = "arg_episode_url";
    public static final String ARG_DATE_TIME = "arg_date_time";

    //REQUEST PERMISSION
    public static final int REQUEST_CAMERA_BUTTON_PERMISSIONS = 0;
    public static final int REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final int REQUEST_CAMERA = 2;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 3;
    public static final int REQUEST_CONNECTION_FAILURE_SOLUTION = 4;
    public static final int REQUEST_IMAGE_CAPTURE = 5;
    public static final int REQUEST_PLACE_PICKER = 6;

    //REQUEST TARGET FRAGMENT
    public static final int REQUEST_TARGET_EMDF = 8;

    //Miscellaneous
    public static final String UNDERSCORE = "_";
}
