package pause.sip.younsukkoh.pause.our_rooms.add_room;

import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;

import pause.sip.younsukkoh.pause.pojo.User;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/28/2016.
 */
public class MemberAdapter extends FirebaseRecyclerAdapter<User, MemberViewHolder> {

    private String mUserEncodedEmail;
    private ArrayList<String> mRoomMembers;

    public MemberAdapter(Class<User> modelClass, int modelLayout, Class<MemberViewHolder> viewHolderClass, DatabaseReference ref, String userEncodedEmail) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mUserEncodedEmail = userEncodedEmail;
        mRoomMembers = new ArrayList<>();
    }

    @Override
    protected void populateViewHolder(MemberViewHolder viewHolder, User user, int position) {
        viewHolder.bindMember(user, mUserEncodedEmail, mRoomMembers);
    }

    public ArrayList<String> getRoomMembers() {
        return mRoomMembers;
    }
}
