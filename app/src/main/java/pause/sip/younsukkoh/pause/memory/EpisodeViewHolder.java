package pause.sip.younsukkoh.pause.memory;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.Episode;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/19/2016.
 */
public class EpisodeViewHolder extends RecyclerView.ViewHolder {

    private View mItemView;
    private ImageView mEpisodeImageView;
    private Episode mEpisode;

    public EpisodeViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;

        mEpisodeImageView = (ImageView) itemView.findViewById(R.id.evh_iv);
        mEpisodeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void bindEpisode(Episode episode){
        mEpisode = episode;

        Picasso.with(mItemView.getContext()).load(episode.getDownloadUrl()).resize(500, 500).into(mEpisodeImageView);

        Log.i(Constants.TAG_DEBUG, "EP " + mEpisode.getDownloadUrl());
    }
}
