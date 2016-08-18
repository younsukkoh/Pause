package pause.sip.younsukkoh.pause.my_memory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.Memory;
import pause.sip.younsukkoh.pause.utility.Constants;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class MyMemoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = MyMemoryViewHolder.class.getSimpleName();

    private View mItemView;
    private String mUserEncodedEmail;
    private Memory mMemory;
    private Context mContext;

    //UI Layout
    private TextView mTitleTextView, mPeopleTextView, mTimeTextView, mLocationTextView, mDescriptionTextView;
    private LinearLayout mEpisodesLayout;
    private ImageButton mAddButton, mEditButton;

    private Button mCameraButton, mRecorderButton, mDocumentButton, mPhotoButton, mCancelButton;

    public MyMemoryViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        mItemView = itemView;

        mTitleTextView = (TextView) itemView.findViewById(R.id.mmvh_tv_what_title);
        mPeopleTextView = (TextView) itemView.findViewById(R.id.mmvh_tv_who_people);
        mTimeTextView = (TextView) itemView.findViewById(R.id.mmvh_tv_when_time);
        mLocationTextView = (TextView) itemView.findViewById(R.id.mmvh_tv_where_location);
        mDescriptionTextView = (TextView) itemView.findViewById(R.id.mmvh_tv_why_description);
        mEpisodesLayout = (LinearLayout) itemView.findViewById(R.id.mmvh_ll_images);

        mAddButton = (ImageButton) itemView.findViewById(R.id.mmvh_b_add);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) mContext;

                MyMemoryDialogFragment myMemoryDialogFragment = MyMemoryDialogFragment.newInstance(mUserEncodedEmail, mMemory.getMemoryId());
                android.app.FragmentManager fm = activity.getFragmentManager();
                myMemoryDialogFragment.show(fm, TAG);
            }
        });

        mEditButton = (ImageButton) itemView.findViewById(R.id.mmvh_b_edit);
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

    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

}
