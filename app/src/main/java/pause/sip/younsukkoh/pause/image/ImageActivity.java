package pause.sip.younsukkoh.pause.image;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import pause.sip.younsukkoh.pause.basis.SingleFragmentActivity;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/19/2016.
 */
public class ImageActivity extends SingleFragmentActivity {

    private static final String TAG = ImageActivity.class.getSimpleName();

    /**
     * Initialize ImageActivity which in turn starts Memory Fragment
     */
    public static Intent newIntent(Context context, String episodeUrl){
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(Constants.EXTRA_EPISODE_URL, episodeUrl);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return ImageFragment.newInstance(getIntent().getStringExtra(Constants.EXTRA_EPISODE_URL));
    }

}
