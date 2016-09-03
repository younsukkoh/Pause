package pause.sip.younsukkoh.pause.our_rooms;

import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import pause.sip.younsukkoh.pause.pojo.Room;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/27/2016.
 */
public class RoomAdapter extends FirebaseRecyclerAdapter<Room, RoomViewHolder> {

    private static final String TAG = RoomAdapter.class.getSimpleName();

    private String mUserEncodedEmail;

    public RoomAdapter(Class<Room> modelClass, int modelLayout, Class<RoomViewHolder> viewHolderClass, Query ref, String userEncodedEmail) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mUserEncodedEmail = userEncodedEmail;
    }

    @Override
    protected void populateViewHolder(RoomViewHolder viewHolder, Room room, int position) {
        viewHolder.bindRoom(mUserEncodedEmail, room);
        Log.i(Constants.TAG_DEBUG, TAG + " " + room.getRoomId());
    }
}
