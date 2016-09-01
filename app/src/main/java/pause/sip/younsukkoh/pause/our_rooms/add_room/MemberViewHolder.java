package pause.sip.younsukkoh.pause.our_rooms.add_room;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.User;
import pause.sip.younsukkoh.pause.utility.Constants;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 8/28/2016.
 */
public class MemberViewHolder extends RecyclerView.ViewHolder {

    private TextView mEmailTextView, mNameTextView, mBirthdayTextView, mGenderTextView;
    private FloatingActionButton mSelectedFAB;

    public MemberViewHolder(View itemView) {
        super(itemView);

        mEmailTextView = (TextView) itemView.findViewById(R.id.mvh_tv_email);
        mNameTextView = (TextView) itemView.findViewById(R.id.mvh_tv_name);
        mBirthdayTextView = (TextView) itemView.findViewById(R.id.mvh_tv_birthday);
        mGenderTextView = (TextView) itemView.findViewById(R.id.mvh_tv_gender);
        mSelectedFAB = (FloatingActionButton) itemView.findViewById(R.id.mvh_fab_selected);
    }

    /**
     * bind Member
     */
    public void bindMember(final User member, final String userEncodedEmail, final ArrayList<String> roomMembers) {

        mEmailTextView.setText(member.getEmail());
        mNameTextView.setText(member.getFirstName() + " " + member.getLastName());
        mBirthdayTextView.setText(Utility.DATE_FORMAT.format(new Date(member.getBirthday())));
        mGenderTextView.setText(member.getGender());

        mSelectedFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.isActivated()) {
                    view.setActivated(false);
                    roomMembers.remove(member.getEmail());
                }
                else {
                    view.setActivated(true);
                    roomMembers.add(member.getEmail());
                }
            }
        });
    }

}
