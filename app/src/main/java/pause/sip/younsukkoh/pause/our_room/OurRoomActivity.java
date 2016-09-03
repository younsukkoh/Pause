package pause.sip.younsukkoh.pause.our_room;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import pause.sip.younsukkoh.pause.basis.SingleFragmentActivity;
import pause.sip.younsukkoh.pause.my_room.MyRoomFragment;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 9/1/2016.
 */
public class OurRoomActivity extends SingleFragmentActivity {

    private static final String TAG = OurRoomActivity.class.getSimpleName();

    /**
     * Initialize OurRoomActivity which in turn starts OurRoomFragment (extends from MyRoomFragment)
     */
    public static Intent newIntent(Context context, String userEncodedEmail, String roomId){
        Intent intent = new Intent(context, OurRoomActivity.class);
        intent.putExtra(Constants.EXTRA_USER_ENCODED_EMAIL, userEncodedEmail);
        intent.putExtra(Constants.EXTRA_ROOM_ID, roomId);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return OurRoomFragment.newInstance(getIntent().getStringExtra(Constants.EXTRA_USER_ENCODED_EMAIL), getIntent().getStringExtra(Constants.EXTRA_ROOM_ID));
    }
}
