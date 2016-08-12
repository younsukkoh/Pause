package pause.sip.younsukkoh.pause.users;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import pause.sip.younsukkoh.pause.pojo.User;

/**
 * Created by Younsuk on 8/12/2016.
 */
public class UsersAdapter extends FirebaseRecyclerAdapter<User, UsersViewHolder> {

    private String mUserEncodedEmail;

    public UsersAdapter(Class<User> modelClass, int modelLayout, Class<UsersViewHolder> viewHolderClass, Query ref, String userEncodedEmail) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mUserEncodedEmail = userEncodedEmail;
    }

    @Override
    protected void populateViewHolder(UsersViewHolder viewHolder, User user, int position) {
        viewHolder.bindUsers(user, mUserEncodedEmail);
    }
}
