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

    //Shared Preference
    public static final String SP_ENCODED_USER_EMAIL = "sp_encoded_user_email";

    //Intent Extras
    public static final String EXTRA_USER_INFORMATION = "extra_user_information";
    public static final String EXTRA_USER_BIRTHDAY = "extra_user_birthday";

    //Bundle Argument Extras
    public static final String ARG_ENCODED_EMAIL = "arg_encoded_email";

    //REQUEST PERMISSION
    public static final int REQUEST_CAMERA_BUTTON_PERMISSIONS = 7;
    public static final int REQUEST_ACCESS_FINE_LOCATION = 0;
    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    public static final int REQUEST_CONNECTION_FAILURE_SOLUTION = 3;
    public static final int REQUEST_IMAGE_CAPTURE = 4;

    //Miscellaneous
    public static final String UNDERSCORE = "_";
}
