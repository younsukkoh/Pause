package pause.sip.younsukkoh.pause.users_friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.User;
import pause.sip.younsukkoh.pause.utility.Constants;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 8/12/2016.
 */
public class UsersFriendsFragment extends Fragment {

    private static final String TAG = UsersFriendsFragment.class.getSimpleName();

    private DatabaseReference mUsersFriendsDatabaseRef;
    private String mUserEncodedEmail;
    private RecyclerView mRecyclerView;
    private FriendsAdapter mFriendsAdapter;
    private TextView mNoFriendsTextView;

    private SearchView mSearchView;
    private ValueEventListener mValueEventListener;
    private ToggleButton mSearchForFriendsSwitch, mSearchForUsersSwitch;

    /**
     * Initialize Users Fragment
     * @param userEncodedEmail pass on current user's encoded email
     * @return
     */
    public static UsersFriendsFragment newInstance(String userEncodedEmail) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_USER_ENCODED_EMAIL, userEncodedEmail);

        UsersFriendsFragment fragment = new UsersFriendsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserEncodedEmail = getArguments().getString(Constants.ARG_USER_ENCODED_EMAIL);
        mUsersFriendsDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_FRIENDS_ + mUserEncodedEmail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users_friends_fragment, container, false);
        setUpUI(view);
        setUpRecyclerView(view);
        return view;
    }

    private void setUpRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.uff_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mFriendsAdapter = new FriendsAdapter(User.class, R.layout.friends_view_holder, FriendViewHolder.class, mUsersFriendsDatabaseRef, mUserEncodedEmail);
        mRecyclerView.setAdapter(mFriendsAdapter);
    }

    private void setUpUI(View view) {
        mNoFriendsTextView = (TextView) view.findViewById(R.id.uff_tv_noFriends);
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) mNoFriendsTextView.setVisibility(View.VISIBLE);
                else mNoFriendsTextView.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        };

        mSearchForFriendsSwitch = (ToggleButton) view.findViewById(R.id.uff_s_searchForFriends);
        mSearchForUsersSwitch = (ToggleButton) view.findViewById(R.id.uff_s_searchForUsers);
        mSearchForFriendsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) mSearchForUsersSwitch.setChecked(false);
                else mSearchForUsersSwitch.setChecked(true);
            }
        });
        mSearchForUsersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) mSearchForFriendsSwitch.setChecked(false);
                else mSearchForFriendsSwitch.setChecked(true);
            }
        });

        mSearchView = (SearchView) view.findViewById(R.id.uff_sv_searchUsers);
        mSearchView.setQueryHint(getString(R.string.search_for_email));
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if the user does not have any friends yet AND the user is searching for a friend (which does not exist, because the user did not add any)
                if (mNoFriendsTextView.getVisibility() == View.VISIBLE && mSearchForFriendsSwitch.isChecked()) {
                    Toast.makeText(getActivity(), getString(R.string.no_friends), Toast.LENGTH_SHORT).show();
                    mSearchView.setIconified(true);
                }
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchedEmail) {
                final String friendEncodedEmail = Utility.encodeEmail(searchedEmail);

                //Check if the user searched for himself or herself
                if (friendEncodedEmail.equals(mUserEncodedEmail)) {
                    Toast.makeText(getActivity(), getString(R.string.searched_user_is_you), Toast.LENGTH_LONG).show();
                    return true;
                }

                final DatabaseReference userDataBaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_INFO_ + friendEncodedEmail);

                userDataBaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            UserProfileDialogFragment userProfileDialogFragment = UserProfileDialogFragment.newInstance(mUserEncodedEmail, friendEncodedEmail);
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            userProfileDialogFragment.show(fm, TAG);

                            mSearchView.setIconified(true);
                        }
                        else Toast.makeText(getActivity(), getString(R.string.user_does_not_exist), Toast.LENGTH_LONG).show();

                        userDataBaseRef.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mUsersFriendsDatabaseRef.addValueEventListener(mValueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mUsersFriendsDatabaseRef.removeEventListener(mValueEventListener);
    }


}