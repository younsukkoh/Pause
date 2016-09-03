package pause.sip.younsukkoh.pause.our_room;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;

import java.util.Date;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.basis.BaseFragment;
import pause.sip.younsukkoh.pause.my_room.MemoryAdapter;
import pause.sip.younsukkoh.pause.my_room.MemoryViewHolder;
import pause.sip.younsukkoh.pause.pojo.Episode;
import pause.sip.younsukkoh.pause.pojo.Memory;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 9/1/2016.
 */
public class OurRoomFragment extends BaseFragment {

    private static final String TAG = OurRoomFragment.class.getSimpleName();

    private String mRoomId;

    private RecyclerView mRecyclerView;
    private MemoryAdapter mMemoryAdapter;
    private FloatingActionButton mMainFab, mCameraFab, mRecorderFab, mDocumentFab, mPhotoFab;

    /**
     * Initialize MyRoomFragment
     * @param roomId pass on current user's encoded email
     * @return
     */
    public static OurRoomFragment newInstance(String userEncodedEmail, String roomId) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_USER_ENCODED_EMAIL, userEncodedEmail);
        args.putString(Constants.ARG_ROOM_ID, roomId);

        OurRoomFragment fragment = new OurRoomFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoomId = getArguments().getString(Constants.ARG_ROOM_ID);
        //Database reference for current ROOM
        mCurrentDatabaseRef = mMainDatabaseRef.child(Constants.OUR_ROOMS_ + mRoomId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_room_fragment, container, false);
        setUpUI(view);
        setUpRecyclerView(view);
        return view;
    }

    protected void setUpUI(View view) {
        super.setUpUI(view);

        mMainFab = (FloatingActionButton) view.findViewById(R.id.mrf_fab_main);
        mMainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFloatingActionButton();
            }
        });

        mCameraFab = (FloatingActionButton) view.findViewById(R.id.mrf_fab_camera);
        mCameraFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForPermission_CameraButton();
                launchCamera();
            }
        });

        mRecorderFab = (FloatingActionButton) view.findViewById(R.id.mrf_fab_recorder);
        mRecorderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mDocumentFab = (FloatingActionButton) view.findViewById(R.id.mrf_fab_document);
        mDocumentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mPhotoFab = (FloatingActionButton) view.findViewById(R.id.mrf_fab_photo);
        mPhotoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /**
     * Set up recycler view for memories
     */
    private void setUpRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mrf_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mMemoryAdapter = new MemoryAdapter(Memory.class, R.layout.memory_view_holder, MemoryViewHolder.class, mCurrentDatabaseRef, mUserEncodedEmail, getActivity());
        mRecyclerView.setAdapter(mMemoryAdapter);
    }

    /**
     * Store the episode into database
     */
    protected void uploadEpisodeToDatabase(String memoryId, String downloadUrl) {
        Log.i(TAG, "uploadEpisodeToDatabase");

        DatabaseReference ourRoomRef_room_timeUpdated = mMainDatabaseRef.child(Constants.OUR_ROOMS_ + mUserEncodedEmail).child(mRoomId).child(Constants.TIME_UPDATED);
        ourRoomRef_room_timeUpdated.setValue(new Date().getTime());

        DatabaseReference episodeRef = mMainDatabaseRef.child(Constants.OUR_ROOMS_ + mRoomId + Constants.UNDERSCORE + memoryId).push();
        Episode episode = new Episode(downloadUrl, new Date().getTime(), mLocation, mLongitude, mLatitude);
        episodeRef.setValue(episode);
    }

    /**
     * Animate floating action button to the right or left depending on its current position
     */
    @Override
    protected void animateFloatingActionButton() {
        if (mIsFabOpen) {
            mMainFab.startAnimation(mRotateBackward);

            mCameraFab.startAnimation(mFabClose);
            mRecorderFab.startAnimation(mFabClose);
            mDocumentFab.startAnimation(mFabClose);
            mPhotoFab.startAnimation(mFabClose);

            mCameraFab.setClickable(false);
            mRecorderFab.setClickable(false);
            mDocumentFab.setClickable(false);
            mPhotoFab.setClickable(false);

            mIsFabOpen = false;
        }
        else {
            mMainFab.startAnimation(mRotateForward);

            mCameraFab.startAnimation(mFabOpen);
            mRecorderFab.startAnimation(mFabOpen);
            mDocumentFab.startAnimation(mFabOpen);
            mPhotoFab.startAnimation(mFabOpen);

            mCameraFab.setClickable(true);
            mRecorderFab.setClickable(true);
            mDocumentFab.setClickable(true);
            mPhotoFab.setClickable(true);

            mIsFabOpen = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMemoryAdapter.cleanup();
    }
}
