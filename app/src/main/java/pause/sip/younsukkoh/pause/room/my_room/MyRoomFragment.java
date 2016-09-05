package pause.sip.younsukkoh.pause.room.my_room;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.basis.BaseFragment_AddContent;
import pause.sip.younsukkoh.pause.pojo.Memory;
import pause.sip.younsukkoh.pause.room.MemoryAdapter;
import pause.sip.younsukkoh.pause.room.MemoryViewHolder;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class MyRoomFragment extends BaseFragment_AddContent {

    private static final String TAG = MyRoomFragment.class.getSimpleName();

    /**
     * Initialize MyRoomFragment
     * @param userEncodedEmail pass on current user's encoded email
     * @return
     */
    public static MyRoomFragment newInstance(String userEncodedEmail) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_USER_ENCODED_EMAIL, userEncodedEmail);

        MyRoomFragment fragment = new MyRoomFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @return DatabaseReference for My Room
     */
    @Override
    protected DatabaseReference setUpCurrentDatabaseRef() {
        mRoomId = mUserEncodedEmail;
        return mMainDatabaseRef.child(Constants.MY_ROOM_ + mUserEncodedEmail);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_room_fragment, container, false);
        setUpUI(view);
        setUpRecyclerView(view, R.id.mrf_rv, new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
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
     * @return MemoryAdapter that lists Memory
     */
    @Override
    protected FirebaseRecyclerAdapter createRecyclerAdapter() {
        return new MemoryAdapter(Memory.class, R.layout.memory_view_holder, MemoryViewHolder.class, mCurrentDatabaseRef, getActivity(), mUserEncodedEmail, mRoomId);
    }

}
