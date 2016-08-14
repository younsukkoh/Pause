package pause.sip.younsukkoh.pause.my_memory;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.Memory;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class MyMemoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    private static final String TAG = MyMemoryViewHolder.class.getSimpleName();

    private View mItemView;
    private String mUserEncodedEmail;
    private Memory mMemory;

    //Values for Memory POJO
    private String mTitle;
    private long mTimeCreated;
    private String mLocation;
    private long mLongitude;
    private long mLatitude;
    private String mDescription;

    //UI Layout
    private TextView mTitleTextView, mPeopleTextView, mTimeTextView, mLocationTextView, mDescriptionTextView;
    private LinearLayout mEpisodesLayout;

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
    }

    /**
     * Bind adapter items with each view holder
     * @param memory
     * @param userEncodedEmail
     */
    public void bindMemory(Memory memory, String userEncodedEmail) {
        mMemory = memory;
        mUserEncodedEmail = userEncodedEmail;

        mTitleTextView.setText(memory.getTitle());

//        String listOfPeople = "";
//        HashMap<String, String> people = memory.getPeople();
//        for (int i = 0; i < memory.getNumberOfPeople(); i ++) {
//            String index = i + "";
//            listOfPeople = listOfPeople + people.get(index) + ", ";
//        }

        mPeopleTextView.setText("YOLO SWAG");

        mTimeTextView.setText(Utility.DATE_FORMAT.format(new Date(memory.getTimeCreated())));

        mLocationTextView.setText(memory.getAddress());

//        HashMap<String, String> episodes = memory.getEpisodes();
//        for (int i = 0; i < memory.getNumberOfEpisodes(); i ++) {
//            String index = i + "";
//
//            ImageView imageView = new ImageView(mItemView.getContext());
//            Picasso.with(mItemView.getContext()).load(episodes.get(index)).resize(500, 500).into(imageView);
//
//            mEpisodesLayout.addView(imageView);
//        }

        mDescriptionTextView.setText(memory.getDescription());
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}
