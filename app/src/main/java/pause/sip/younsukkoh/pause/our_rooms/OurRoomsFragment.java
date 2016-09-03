package pause.sip.younsukkoh.pause.our_rooms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.our_rooms.add_room.AddRoomDialogFragment;
import pause.sip.younsukkoh.pause.pojo.Room;
import pause.sip.younsukkoh.pause.utility.Constants;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class OurRoomsFragment extends Fragment{

    private static final String TAG = OurRoomsFragment.class.getSimpleName();

    private DatabaseReference mOurRoomsRef;
    private String mUserEncodedEmail;
    private String mUserDecodedEmail;

    private RecyclerView mRecyclerView;
    private RoomAdapter mRoomAdapter;
    private FloatingActionButton mAddRoomFab;

    /**
     * Initialize OurRoomsFragment
     * @param userEncodedEmail pass on current user's encoded email
     * @return
     */
    public static OurRoomsFragment newInstance(String userEncodedEmail) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_USER_ENCODED_EMAIL, userEncodedEmail);

        OurRoomsFragment fragment = new OurRoomsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserEncodedEmail = getArguments().getString(Constants.ARG_USER_ENCODED_EMAIL);
        mUserDecodedEmail = Utility.decodeEmail(mUserEncodedEmail);
        mOurRoomsRef = FirebaseDatabase.getInstance().getReference().child(Constants.OUR_ROOMS_ + mUserEncodedEmail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.our_rooms_fragment, container, false);
        setUpUI(view);
        setUpRecyclerView(view);
        return view;
    }

    /**
     * Set up user interface
     */
    private void setUpUI(View view) {
        mAddRoomFab = (FloatingActionButton) view.findViewById(R.id.orf_fab_addRoom);
        mAddRoomFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                AddRoomDialogFragment addRoomDialogFragment = AddRoomDialogFragment.newInstance(mUserEncodedEmail);
                addRoomDialogFragment.setTargetFragment(OurRoomsFragment.this, Constants.REQUEST_TARGET_ORF);
                addRoomDialogFragment.show(fm, TAG);
            }
        });
    }

    /**
     * Set up recycler view for rooms
     */
    private void setUpRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.orf_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRoomAdapter = new RoomAdapter(Room.class, R.layout.room_view_holder, RoomViewHolder.class, mOurRoomsRef, mUserEncodedEmail);
        mRecyclerView.setAdapter(mRoomAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == Constants.REQUEST_TARGET_ORF) {
            ArrayList<String> roomMembers = data.getStringArrayListExtra(Constants.EXTRA_ROOM_MEMBERS);
            roomMembers.add(mUserDecodedEmail);
            addRoomsForAllUsers(roomMembers);
        }
    }

    /**
     * Add Room for all the users involved in creation of this room
     */
    private void addRoomsForAllUsers(ArrayList<String> roomMembers) {
        DatabaseReference newRoomRef = FirebaseDatabase.getInstance().getReference().child(Constants.OUR_ROOMS_ + mUserEncodedEmail).push();

        String roomId = newRoomRef.getKey();
        long created = new Date().getTime();
        int numberOfMembers = roomMembers.size();
        HashMap<String, Object> members = new HashMap<>();
        members.put(Constants.LIST, roomMembers);
        Room newRoom = new Room(roomId, created, members, numberOfMembers);

        for (int i = 0; i < numberOfMembers; i ++) {
            String decodedEmail = roomMembers.get(i);
            if (decodedEmail == mUserDecodedEmail) {
                newRoomRef.setValue(newRoom);
            }
            else {
                DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference().child(Constants.OUR_ROOMS_ + Utility.encodeEmail(decodedEmail)).child(roomId);
                roomRef.setValue(newRoom);
            }
        }
    }
}
