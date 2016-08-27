package pause.sip.younsukkoh.pause.users_friends;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import pause.sip.younsukkoh.pause.pojo.User;

/**
 * Created by Younsuk on 8/12/2016.
 */
public class FriendsAdapter extends FirebaseRecyclerAdapter<User, FriendViewHolder> {

    private String mUserEncodedEmail;

    public FriendsAdapter(Class<User> modelClass, int modelLayout, Class<FriendViewHolder> viewHolderClass, Query ref, String userEncodedEmail) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mUserEncodedEmail = userEncodedEmail;
    }

    @Override
    protected void populateViewHolder(FriendViewHolder viewHolder, User user, int position) {
        viewHolder.bindUsers(user, mUserEncodedEmail);
    }
}
