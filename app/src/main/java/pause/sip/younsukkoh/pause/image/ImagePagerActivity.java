package pause.sip.younsukkoh.pause.image;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/19/2016.
 */
public class ImagePagerActivity extends AppCompatActivity {

    private static final String TAG = ImagePagerActivity.class.getSimpleName();

    private String mEpisodeUrl;
    private ViewPager mViewPager;

    /**
     * Initialize ImageActivity which in turn starts Memory Fragment
     */
    public static Intent newIntent(Context context, String episodeUrl){
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(Constants.EXTRA_EPISODE_URL, episodeUrl);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_pager_activity);

        mEpisodeUrl = getIntent().getStringExtra(Constants.EXTRA_EPISODE_URL);

        mViewPager = (ViewPager) findViewById(R.id.ipa_vp);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ImageFragment.newInstance(mEpisodeUrl);
            }

            @Override
            public int getCount() {
                return 0;
            }
        });
    }
}
