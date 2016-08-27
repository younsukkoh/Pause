package pause.sip.younsukkoh.pause.users_friends;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.User;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 8/12/2016.
 */
public class FriendViewHolder extends RecyclerView.ViewHolder {

    private View mItemView;
    private TextView mEmailText, mNameText, mBirthdayText, mGenderText;
    private FloatingActionButton mFriendedButton, mDeleteButton;

    public FriendViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;

        mEmailText = (TextView) itemView.findViewById(R.id.fvh_tv_email);
        mNameText = (TextView) itemView.findViewById(R.id.fvh_tv_name);
        mBirthdayText = (TextView) itemView.findViewById(R.id.fvh_tv_birthday);
        mGenderText = (TextView) itemView.findViewById(R.id.fvh_tv_gender);

        mFriendedButton = (FloatingActionButton) itemView.findViewById(R.id.fvh_fab_friended);
        mDeleteButton = (FloatingActionButton) itemView.findViewById(R.id.fvh_fab_delete);

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void bindUsers(User user, String userEncodedEmail) {
        mEmailText.setText(user.getEmail());
        mNameText.setText(user.getFirstName() + " " + user.getLastName());
        mBirthdayText.setText(Utility.DATE_FORMAT.format(user.getBirthday()));
        mGenderText.setText(user.getGender());
    }
}
