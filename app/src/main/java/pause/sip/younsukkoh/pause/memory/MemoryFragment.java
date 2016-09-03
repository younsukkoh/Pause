package pause.sip.younsukkoh.pause.memory;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import pause.sip.younsukkoh.pause.basis.BaseFragment;
import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.Episode;
import pause.sip.younsukkoh.pause.pojo.Memory;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/19/2016.
 */
public class MemoryFragment extends BaseFragment {

    private static final String TAG = MemoryFragment.class.getSimpleName();

    private String mMemoryId;

    /**
     * Initialize Memory Fragment
     * @param memoryId pass on current user memory's id
     * @return
     */
    public static MemoryFragment newInstance(String userEncodedEmail, String memoryId) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_MEMORY_ID, memoryId);
        args.putString(Constants.ARG_USER_ENCODED_EMAIL, userEncodedEmail);

        MemoryFragment fragment = new MemoryFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMemoryId = getArguments().getString(Constants.ARG_MEMORY_ID);
    }

    @Override
    protected DatabaseReference setUpCurrentDatabaseRef() {
        return mMainDatabaseRef.child(Constants.MY_ROOM_ + mUserEncodedEmail + Constants.UNDERSCORE + mMemoryId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.memory_fragment, container, false);
        setUpUI(view);
        setUpRecyclerView(view, R.id.mf_rv);
        return view;
    }

    protected void setUpUI(View view) {
        super.setUpUI(view);

        mMainFab = (FloatingActionButton) view.findViewById(R.id.mf_fab_main);
        mMainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFloatingActionButton();
            }
        });

        mCameraFab = (FloatingActionButton) view.findViewById(R.id.mf_fab_camera);
        mCameraFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForPermission_CameraButton();
                launchCamera();
            }
        });

        mRecorderFab = (FloatingActionButton) view.findViewById(R.id.mf_fab_recorder);
        mRecorderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mDocumentFab = (FloatingActionButton) view.findViewById(R.id.mf_fab_document);
        mDocumentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mPhotoFab = (FloatingActionButton) view.findViewById(R.id.mf_fab_photo);
        mPhotoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected FirebaseRecyclerAdapter createRecyclerAdapter() {
        return new EpisodeAdapter(Episode.class, R.layout.episode_view_holder, EpisodeViewHolder.class, mCurrentDatabaseRef);
    }

    /**
     * Original method will add a new memory, but we do not need a new memory, just new episode
     * @return current memory's id
     */
    @Override
    protected String uploadMemoryToDatabase(String episodeUrl) {
        return mMemoryId;
    }

    /**
     * Original method only adds to current memory;
     * Add episode to list of episode to my room
     */
    @Override
    protected void uploadEpisodeToDatabase(String memoryId, String downloadUrl) {
        super.uploadEpisodeToDatabase(memoryId, downloadUrl);

        Log.i(TAG, "uploadEpisodeToDatabase");

        //Update my_room_EMAIL
        final String newEpisode = downloadUrl;
        final DatabaseReference myMemoryDatabaseRef = mMainDatabaseRef.child(Constants.MY_ROOM_ + mUserEncodedEmail).child(mMemoryId);
        myMemoryDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Memory memory = dataSnapshot.getValue(Memory.class);

                ArrayList<String> episodes = (ArrayList<String>) memory.getEpisodes().get(Constants.LIST);
                episodes.add(newEpisode);

                HashMap<String, Object> updatedEpisodes = new HashMap<String, Object>();
                updatedEpisodes.put(Constants.LIST, episodes);

                int updatedNumberOfEpisodes = memory.getNumberOfEpisodes() + 1;

                HashMap<String, Object> updatedMemoryInfo = new HashMap<String, Object>();
                updatedMemoryInfo.put(Constants.EPISODES, updatedEpisodes);
                updatedMemoryInfo.put(Constants.NUMBER_OF_EPISODES, updatedNumberOfEpisodes);

                myMemoryDatabaseRef.updateChildren(updatedMemoryInfo);

                myMemoryDatabaseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

}
