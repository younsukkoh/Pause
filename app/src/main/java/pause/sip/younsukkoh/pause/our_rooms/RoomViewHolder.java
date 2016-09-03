package pause.sip.younsukkoh.pause.our_rooms;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.my_room.MyRoomFragment;
import pause.sip.younsukkoh.pause.our_room.OurRoomActivity;
import pause.sip.younsukkoh.pause.our_room.OurRoomFragment;
import pause.sip.younsukkoh.pause.pojo.Room;
import pause.sip.younsukkoh.pause.utility.Constants;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 8/27/2016.
 */
public class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private static final String TAG = RoomViewHolder.class.getSimpleName();

    private String mUserEncodedEmail;
    private Room mRoom;
    private TextView mNameTextView, mNumberOfMembersTextView, mMembersTextView, mTimeCreatedTextView, mTimeUpdatedTextView;

    public RoomViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mNameTextView = (TextView) itemView.findViewById(R.id.rvh_tv_name);
        mNumberOfMembersTextView = (TextView) itemView.findViewById(R.id.rvh_tv_numberOfMembers);
        mMembersTextView = (TextView) itemView.findViewById(R.id.rvh_tv_members);
        mTimeCreatedTextView = (TextView) itemView.findViewById(R.id.rvh_tv_timeCreated);
        mTimeUpdatedTextView = (TextView) itemView.findViewById(R.id.rvh_tv_timeUpdated);
    }

    public void bindRoom(String userEncodedEmail, Room room) {
        Log.i(Constants.TAG_DEBUG, TAG + " " + room.getRoomId());
        mRoom = room;
        mUserEncodedEmail = userEncodedEmail;

        String title = room.getName();
        if (title == null) mNameTextView.setText(R.string.no_title);
        else mNameTextView.setText(title);

        ArrayList<String> members = (ArrayList<String>) room.getMembers().get(Constants.LIST);

        mNumberOfMembersTextView.setText(members.size() + "");

        String membersList = "";
        for (int i = 0; i < members.size(); i ++) {
            membersList = membersList + " " + members.get(i);
        }
        mMembersTextView.setText(membersList);

        mTimeCreatedTextView.setText(Utility.DATE_TIME_FORMAT.format(room.getTimeCreated()));

        mTimeUpdatedTextView.setText(Utility.DATE_TIME_FORMAT.format(room.getTimeUpdated()));
    }

    @Override
    public void onClick(View view) {
        Log.i(Constants.TAG_DEBUG, TAG + " " + mRoom.getRoomId());
        Intent intent = OurRoomActivity.newIntent(view.getContext(), mUserEncodedEmail, mRoom.getRoomId());
        view.getContext().startActivity(intent);
    }
}
