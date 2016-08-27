package pause.sip.younsukkoh.pause.users_friends;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.User;
import pause.sip.younsukkoh.pause.utility.Constants;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 8/27/2016.
 */
public class UserProfileDialogFragment extends DialogFragment {

    private static String TAG = UserProfileDialogFragment.class.getSimpleName();

    private User mFriend;
    private String mUserEncodedEmail;
    private String mFriendsEncodedEmail;
    private DatabaseReference mMainDatabaseRef;
    private DatabaseReference mUserDatabaseRef;
    private DatabaseReference mFriendsDatabaseRef;
    private ValueEventListener mValueEventListener;
    private TextView mEmailTextView, mNameTextView, mBirthdayTextView, mGenderTextView;
    private FloatingActionButton mAddFAB, mDeleteFAB, mFriendedFAB;

    /**
     * Initialize UserProfileDialogFragment
     */
    public static UserProfileDialogFragment newInstance(String userEncodedEmail, String friendsEncodedEmail) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_FRIENDS_ENCODED_EMAIL, friendsEncodedEmail);
        args.putString(Constants.ARG_USER_ENCODED_EMAIL, userEncodedEmail);

        UserProfileDialogFragment fragment = new UserProfileDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserEncodedEmail = getArguments().getString(Constants.ARG_USER_ENCODED_EMAIL);
        mFriendsEncodedEmail = getArguments().getString(Constants.ARG_FRIENDS_ENCODED_EMAIL);

        mMainDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mUserDatabaseRef = mMainDatabaseRef.child(Constants.USERS_INFO_ + mUserEncodedEmail);
        mFriendsDatabaseRef = mMainDatabaseRef.child(Constants.USERS_INFO_ + mFriendsEncodedEmail);
        Log.i(Constants.TAG_DEBUG, "FD? " + mFriendsDatabaseRef.getKey() + " UD? " + mUserDatabaseRef.getKey());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searched_friend_dialog_fragment, container, false);
        setUpUI(view);
        return view;
    }

    /**
     * Set up user interface
     */
    private void setUpUI(View view) {
        mEmailTextView = (TextView) view.findViewById(R.id.sfdf_tv_email);
        mNameTextView = (TextView) view.findViewById(R.id.sfdf_tv_name);
        mBirthdayTextView = (TextView) view.findViewById(R.id.sfdf_tv_birthday);
        mGenderTextView = (TextView) view.findViewById(R.id.sfdf_tv_gender);

        mFriendedFAB = (FloatingActionButton) view.findViewById(R.id.sfdf_fab_friended);
        mAddFAB = (FloatingActionButton) view.findViewById(R.id.sfdf_fab_add);
        mDeleteFAB = (FloatingActionButton) view.findViewById(R.id.sfdf_fab_delete);

        //Hide add button
        DatabaseReference usersDatabaseRef = mMainDatabaseRef.child(Constants.USERS_FRIENDS_ + mUserEncodedEmail).child(mFriendsEncodedEmail);
        usersDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) mAddFAB.setVisibility(View.GONE);
                else {
                    mFriendedFAB.setVisibility(View.GONE);
                    mDeleteFAB.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });

        mAddFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Log.i(Constants.TAG_DEBUG, "FD mFriend? " + (mFriend==null) + " UD mUser? " + (user==null));

                        //Add friend to user's database
                        DatabaseReference usersDatabaseRef = mMainDatabaseRef.child(Constants.USERS_FRIENDS_ + mUserEncodedEmail).child(mFriendsEncodedEmail);
                        usersDatabaseRef.setValue(mFriend);

                        //Add user to friend's database
                        DatabaseReference friendsDatabaseRef = mMainDatabaseRef.child(Constants.USERS_FRIENDS_ + mFriendsEncodedEmail).child(mUserEncodedEmail);
                        friendsDatabaseRef.setValue(user);

                        mAddFAB.setVisibility(View.GONE);
                        mFriendedFAB.setVisibility(View.VISIBLE);
                        mDeleteFAB.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
            }
        });

        mDeleteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mFriendsDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFriend = dataSnapshot.getValue(User.class);
                Log.i(Constants.TAG_DEBUG, "FD mFriend? " + (mFriend==null));
                mEmailTextView.setText(mFriend.getEmail());
                mNameTextView.setText(mFriend.getFirstName() + " " + mFriend.getLastName());
                mBirthdayTextView.setText(Utility.DATE_FORMAT.format(new Date(mFriend.getBirthday())));
                mGenderTextView.setText(mFriend.getGender());

                mFriendsDatabaseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
//        mFriendsDatabaseRef.addValueEventListener(mValueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
//        mFriendsDatabaseRef.removeEventListener(mValueEventListener);
    }
}
