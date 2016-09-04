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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.basis.BaseFragment;
import pause.sip.younsukkoh.pause.our_rooms.add_room.AddRoomDialogFragment;
import pause.sip.younsukkoh.pause.pojo.Room;
import pause.sip.younsukkoh.pause.utility.Constants;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class OurRoomsFragment extends BaseFragment {

    private static final String TAG = OurRoomsFragment.class.getSimpleName();

    private String mUserDecodedEmail;
    private FloatingActionButton mMainFab;

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
        mUserDecodedEmail = Utility.decodeEmail(mUserEncodedEmail);
    }

    @Override
    protected DatabaseReference setUpCurrentDatabaseRef() {
        return mMainDatabaseRef.child(Constants.OUR_ROOMS_ + mUserEncodedEmail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.our_rooms_fragment, container, false);
        setUpUI(view);
        setUpRecyclerView(view, R.id.orf_rv, new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    protected void setUpUI(View view) {
        mMainFab = (FloatingActionButton) view.findViewById(R.id.orf_fab_addRoom);
        mMainFab.setOnClickListener(new View.OnClickListener() {
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
     * @return FirebaseRecyclerAdapter that lists all the room that the current user is involved in
     */
    @Override
    protected FirebaseRecyclerAdapter createRecyclerAdapter() {
        return new RoomAdapter(Room.class, R.layout.room_view_holder, RoomViewHolder.class, mCurrentDatabaseRef, mUserEncodedEmail);
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
        Room newRoom = new Room(roomId, members, numberOfMembers, created);

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
