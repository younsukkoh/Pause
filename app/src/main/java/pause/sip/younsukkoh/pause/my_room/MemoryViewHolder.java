package pause.sip.younsukkoh.pause.my_room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.memory.MemoryActivity;
import pause.sip.younsukkoh.pause.pojo.Memory;
import pause.sip.younsukkoh.pause.utility.Constants;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class MemoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = MemoryViewHolder.class.getSimpleName();

    private View mItemView;
    private String mUserEncodedEmail;
    private Memory mMemory;
    private Context mContext;
    private Activity mActivity;

    //UI Layout
    private TextView mTitleTextView, mPeopleTextView, mTimeTextView, mLocationTextView, mDescriptionTextView;
    private LinearLayout mEpisodesLayout;
    private ImageButton mAddButton, mEditButton;

    public MemoryViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        mItemView = itemView;

        mTitleTextView = (TextView) itemView.findViewById(R.id.mvh_tv_what_title);
        mPeopleTextView = (TextView) itemView.findViewById(R.id.mvh_tv_who_people);
        mTimeTextView = (TextView) itemView.findViewById(R.id.mvh_tv_when_time);
        mLocationTextView = (TextView) itemView.findViewById(R.id.mvh_tv_where_location);
        mDescriptionTextView = (TextView) itemView.findViewById(R.id.mvh_tv_why_description);
        mEpisodesLayout = (LinearLayout) itemView.findViewById(R.id.mvh_ll_images);

        mAddButton = (ImageButton) itemView.findViewById(R.id.mvh_b_add);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEpisodeDialogFragment addEpisodeDialogFragment = AddEpisodeDialogFragment.newInstance(mUserEncodedEmail, mMemory.getMemoryId());
                android.app.FragmentManager fm = mActivity.getFragmentManager();
                addEpisodeDialogFragment.show(fm, TAG);
            }
        });

        mEditButton = (ImageButton) itemView.findViewById(R.id.mvh_b_edit);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /**
     * Bind adapter items with each view holder
     * @param memory
     * @param userEncodedEmail
     */
    public void bindMemory(Memory memory, String userEncodedEmail, Context context) {
        mMemory = memory;
        mUserEncodedEmail = userEncodedEmail;
        mContext = context;
        mActivity = (Activity) mContext;

        String title = memory.getTitle();
        if (title == null) mTitleTextView.setText(R.string.no_title);
        else mTitleTextView.setText(title);

        mTimeTextView.setText(Utility.DATE_FORMAT.format(new Date(memory.getTimeCreated())));

        mLocationTextView.setText(memory.getLocation());

        for (int i = 0; i < memory.getNumberOfEpisodes(); i ++) {
            ImageView imageView = new ImageView(mItemView.getContext());
            imageView.setPadding(6, 4, 4, 4);
            ArrayList<String> episodes = (ArrayList<String>) memory.getEpisodes().get(Constants.LIST);
            Picasso.with(mItemView.getContext()).load(episodes.get(i)).resize(500, 500).into(imageView);
            mEpisodesLayout.addView(imageView);
        }

        String description = memory.getDescription();
        if (description == null) mDescriptionTextView.setText(R.string.no_description);
        else mDescriptionTextView.setText(description);
    }

    @Override
    public void onClick(View view) {
        Intent intent = MemoryActivity.newIntent(mActivity, mUserEncodedEmail, mMemory.getMemoryId());
        mActivity.startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

}