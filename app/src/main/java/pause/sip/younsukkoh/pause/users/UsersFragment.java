package pause.sip.younsukkoh.pause.users;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.User;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/12/2016.
 */
public class UsersFragment extends Fragment {

    private String mUserEncodedEmail;
    private Button mAdd;
    private RecyclerView mRecyclerView;
    private UsersAdapter mUsersAdapter;

    /**
     * Initialize Users Fragment
     * @param userEncodedEmail pass on current user's encoded email
     * @return
     */
    public static UsersFragment newInstance(String userEncodedEmail) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_ENCODED_EMAIL, userEncodedEmail);

        UsersFragment fragment = new UsersFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserEncodedEmail = getArguments().getString(Constants.ARG_ENCODED_EMAIL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users_fragment, container, false);
        setUpUI(view);
        setUpRecyclerView(view);
        return view;
    }

    private void setUpRecyclerView(View view) {
        DatabaseReference mMainDatabaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mMainDatabaseRef.child("users");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.uf_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mUsersAdapter = new UsersAdapter(User.class, R.layout.users_view_holder, UsersViewHolder.class, mUsersRef, mUserEncodedEmail);
        mRecyclerView.setAdapter(mUsersAdapter);
    }

    private void setUpUI(View view) {
        mAdd = (Button) view.findViewById(R.id.uf_b_add);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
