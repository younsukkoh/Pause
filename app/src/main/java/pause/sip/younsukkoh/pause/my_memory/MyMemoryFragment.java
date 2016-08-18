package pause.sip.younsukkoh.pause.my_memory;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;

import pause.sip.younsukkoh.pause.BaseFragment;
import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.Memory;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class MyMemoryFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MyMemoryFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MyMemoryAdapter mMyMemoryAdapter;

    private FloatingActionButton mMainFab, mCameraFab, mRecorderFab, mDocumentFab, mPhotoFab;

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
        mRoomDatabaseRef = mMainDatabaseRef.child(Constants.MY_ROOM + mUserEncodedEmail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_memory_fragment, container, false);
        setUpUI(view);
        setUpRecyclerView(view);
        return view;
    }

    protected void setUpUI(View view) {
        super.setUpUI(view);

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
                checkForPermission_CameraButton();
                launchCamera();
            }
        });

        mRecorderFab = (FloatingActionButton) view.findViewById(R.id.mmf_fab_recorder);
        mRecorderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mDocumentFab = (FloatingActionButton) view.findViewById(R.id.mmf_fab_document);
        mDocumentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mPhotoFab = (FloatingActionButton) view.findViewById(R.id.mmf_fab_photo);
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mmf_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mMyMemoryAdapter = new MyMemoryAdapter(Memory.class, R.layout.my_memory_view_holder, MyMemoryViewHolder.class, mRoomDatabaseRef, mUserEncodedEmail, getActivity());
        mRecyclerView.setAdapter(mMyMemoryAdapter);
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
        mMyMemoryAdapter.cleanup();
    }

}
