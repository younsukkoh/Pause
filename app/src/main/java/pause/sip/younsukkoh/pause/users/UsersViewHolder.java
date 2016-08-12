package pause.sip.younsukkoh.pause.users;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.User;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 8/12/2016.
 */
public class UsersViewHolder extends RecyclerView.ViewHolder {

    private View mItemView;
    private TextView mEmailText;
    private TextView mNameText;
    private TextView mBirthdayText;
    private TextView mGenderText;

    public UsersViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;

        mEmailText = (TextView) itemView.findViewById(R.id.uvh_tv_email);
        mNameText = (TextView) itemView.findViewById(R.id.uvh_tv_name);
        mBirthdayText = (TextView) itemView.findViewById(R.id.uvh_tv_birthday);
        mGenderText = (TextView) itemView.findViewById(R.id.uvh_tv_gender);
    }

    public void bindUsers(User user, String userEncodedEmail) {
        mEmailText.setText(user.getEmail());
        mNameText.setText(user.getFirstName() + " " + user.getLastName());
        mBirthdayText.setText(Utility.DATE_FORMAT.format(user.getBirthday()));
        mGenderText.setText(user.getGender());
    }
}
