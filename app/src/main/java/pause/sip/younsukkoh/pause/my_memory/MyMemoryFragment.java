package pause.sip.younsukkoh.pause.my_memory;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.Memory;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class MyMemoryFragment extends Fragment {

    private static final String TAG = MyMemoryFragment.class.getSimpleName();

    private String mUserEncodedEmail;
    private DatabaseReference mMainDatabaseRef;
    private DatabaseReference mMyMemoryRef;
    private RecyclerView mRecyclerView;
    private MyMemoryAdapter mMyMemoryAdapter;

    private Boolean mIsFabOpen = false;
    private FloatingActionButton mMainFab, mCameraFab, mRecorderFab, mPencilFab;
    private Animation mFabOpen,mFabClose, mRotateForward, mRotateBackward;

    /**
     * Initialize My Memory Fragment
     * @param userEncodedEmail pass on current user's encoded email
     * @return
     */
    public static MyMemoryFragment newInstance(String userEncodedEmail) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_ENCODED_EMAIL, userEncodedEmail);

        MyMemoryFragment fragment = new MyMemoryFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserEncodedEmail = getArguments().getString(Constants.ARG_ENCODED_EMAIL);
        mMainDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mMyMemoryRef = mMainDatabaseRef.child(Constants.MY_ROOM + mUserEncodedEmail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_memory_fragment, container, false);
        setUpUI(view);
        setUpRecyclerView(view);
        return view;
    }

    private void setUpUI(View view) {
        mFabOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        mFabClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        mRotateForward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        mRotateBackward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);

        mMainFab = (FloatingActionButton) view.findViewById(R.id.mmf_fab_main);
        mMainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFloatingActionButton();
            }
        });

        mCameraFab = (FloatingActionButton) view.findViewById(R.id.mmf_fab_camera);
        mCameraFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> people = new HashMap<String, String>();
                people.put("0", mUserEncodedEmail);
                people.put("1", "Joe");

                int numberOfPeople = 2;

                HashMap<String, String> episodes = new HashMap<String, String>();
                episodes.put("0", "downloadUrl");

                int numberOfEpisodes = 1;

                String title = "Title";

                long timeCreated = new Date().getTime();

                String location = "Location";
                long longitude = 1l;
                long latitude = 2l;

                String description = "This is description!";

                DatabaseReference databaseReference = mMyMemoryRef.push();

//                Memory memory = new Memory(databaseReference.getKey(), people, numberOfPeople, episodes, numberOfEpisodes, title, timeCreated, location, longitude, latitude, description);
                Memory memory = new Memory(databaseReference.getKey(), title, timeCreated, location, longitude, latitude, description);
                databaseReference.setValue(memory);

                animateFloatingActionButton();
            }
        });

        mRecorderFab = (FloatingActionButton) view.findViewById(R.id.mmf_fab_recorder);
        mRecorderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mPencilFab = (FloatingActionButton) view.findViewById(R.id.mmf_fab_pencil);
        mPencilFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setUpRecyclerView(View view) {
        Log.i(Constants.TAG_DEBUG, "setUpRecyclerView");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.mmf_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mMyMemoryAdapter = new MyMemoryAdapter(Memory.class, R.layout.my_memory_view_holder, MyMemoryViewHolder.class, mMyMemoryRef, mUserEncodedEmail);
        mRecyclerView.setAdapter(mMyMemoryAdapter);
    }

    private void animateFloatingActionButton() {
        if (mIsFabOpen) {
            mMainFab.startAnimation(mRotateBackward);

            mCameraFab.startAnimation(mFabClose);
            mRecorderFab.startAnimation(mFabClose);
            mPencilFab.startAnimation(mFabClose);

            mCameraFab.setClickable(false);
            mRecorderFab.setClickable(false);
            mPencilFab.setClickable(false);

            mIsFabOpen = false;
        }
        else {
            mMainFab.startAnimation(mRotateForward);

            mCameraFab.startAnimation(mFabOpen);
            mRecorderFab.startAnimation(mFabOpen);
            mPencilFab.startAnimation(mFabOpen);

            mCameraFab.setClickable(true);
            mRecorderFab.setClickable(true);
            mPencilFab.setClickable(true);

            mIsFabOpen = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMyMemoryAdapter.cleanup();
    }
}
