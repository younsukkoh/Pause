package pause.sip.younsukkoh.pause.memory;

import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import pause.sip.younsukkoh.pause.pojo.Episode;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/19/2016.
 */
public class EpisodeAdapter extends FirebaseRecyclerAdapter<Episode, EpisodeViewHolder> {

    private static final String TAG = EpisodeAdapter.class.getSimpleName();

    public EpisodeAdapter(Class<Episode> modelClass, int modelLayout, Class<EpisodeViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(EpisodeViewHolder viewHolder, Episode episode, int position) {

        viewHolder.bindEpisode(episode);
    }
}
