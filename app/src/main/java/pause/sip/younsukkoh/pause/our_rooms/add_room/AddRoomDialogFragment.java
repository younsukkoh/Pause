package pause.sip.younsukkoh.pause.our_rooms.add_room;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.User;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/27/2016.
 */
public class AddRoomDialogFragment extends DialogFragment {

    private RecyclerView mRecyclerView;
    private MemberAdapter mMemberAdapter;
    private String mUserEncodedEmail;
    private DatabaseReference mUsersFriendsDatabaseRef;

    /**
     * Initialize AddRoomDialogFragment
     */
    public static AddRoomDialogFragment newInstance(String userEncodedEmail) {

        Bundle args = new Bundle();
        args.putString(Constants.ARG_USER_ENCODED_EMAIL, userEncodedEmail);

        AddRoomDialogFragment fragment = new AddRoomDialogFragment();
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.add_room_dialog_fragment, null);

        setUpRecyclerView(view);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.select_friends_to_invite)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResultOk(mMemberAdapter.getRoomMembers());
                    }
                });
        alertDialogBuilder.setView(view);

        return alertDialogBuilder.create();
    }

    private void sendResultOk(ArrayList<String> roomMembers) {
        if (getTargetFragment() == null) return;

        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_ROOM_MEMBERS, roomMembers);

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    private void setUpRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.ardf_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mMemberAdapter = new MemberAdapter(User.class, R.layout.member_view_holder, MemberViewHolder.class, mUsersFriendsDatabaseRef, mUserEncodedEmail);
        mRecyclerView.setAdapter(mMemberAdapter);
    }
}
