package pause.sip.younsukkoh.pause.utility;

import pause.sip.younsukkoh.pause.BuildConfig;

/**
 * Created by Younsuk on 7/20/2016.
 */
public final class Constants {

    //Tag for Debugging
    public static final String TAG_DEBUG = "YOLO";

    //Firebase URL
    public static final String FB_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;

    //Firebase POJOS
    public static final String USERS = "users";

    //Firebase Reference Points
    public static final String MY_ROOM = "my-room-";

    //Shared Preference
    public static final String SP_ENCODED_USER_EMAIL = "sp_encoded_user_email";

    //Intent Extras
    public static final String EXTRA_USER_INFORMATION = "extra_user_information";
    public static final String EXTRA_USER_BIRTHDAY = "extra_user_birthday";

    //Bundle Argument Extras
    public static final String ARG_ENCODED_EMAIL = "arg_encoded_email";
}
