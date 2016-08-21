package pause.sip.younsukkoh.pause.image;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/19/2016.
 */
public class ImageFragment extends Fragment {

    private String mEpisodeUrl;
    private ImageView mImageView;
    /**
     * Initialize ImageFragment
     * @param episodeUrl pass on clicked episode's url
     * @return
     */
    public static ImageFragment newInstance(String episodeUrl) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_EPISODE_URL, episodeUrl);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEpisodeUrl = getArguments().getString(Constants.ARG_EPISODE_URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fragment, container, false);
        setUpUI(view);
        return view;
    }

    private void setUpUI(View view) {
        mImageView = (ImageView) view.findViewById(R.id.if_iv);

        Picasso.with(view.getContext()).load(mEpisodeUrl).resize(500, 500).into(mImageView);
    }
}
