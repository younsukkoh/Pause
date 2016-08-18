package pause.sip.younsukkoh.pause;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import pause.sip.younsukkoh.pause.my_memory.MyMemoryFragment;
import pause.sip.younsukkoh.pause.our_memories.OurMemoriesFragment;
import pause.sip.younsukkoh.pause.pojo.Memory;
import pause.sip.younsukkoh.pause.settings.SettingsFragment;
import pause.sip.younsukkoh.pause.users.UsersFragment;
import pause.sip.younsukkoh.pause.utility.Constants;
import pause.sip.younsukkoh.pause.utility.Utility;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setUpUI();
    }

    /**
     * Set up user interface
     */
    private void setUpUI() {
        SectionPagerAdapter pagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.ma_vp);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.ma_tl);
        tabLayout.setupWithViewPager(viewPager);

        //Set tabs with appropriate icons
        final int[] TAB_ICONS = new int[] { R.drawable.my_memory_brown, R.drawable.our_memories_brown, R.drawable.friends_brown, R.drawable.settings_brown };
        for (int i = 0; i < TAB_ICONS.length; i ++) tabLayout.getTabAt(i).setIcon(TAB_ICONS[i]);
    }

    /**
     * Pager adapter for different sections of the app
     */
    private class SectionPagerAdapter extends FragmentStatePagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return MyMemoryFragment.newInstance(mUserEncodedEmail);
                case 1:
                    return OurMemoriesFragment.newInstance(mUserEncodedEmail);
                case 2:
                    return UsersFragment.newInstance(mUserEncodedEmail);
                case 3:
                    return SettingsFragment.newInstance(mUserEncodedEmail);
                default:
                    return MyMemoryFragment.newInstance(mUserEncodedEmail);
            }

        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Mine";
                case 1:
                    return "Ours";
                case 2:
                    return "Friends";
                case 3:
                    return "Settings";
                default:
                    return "Mine";
            }
        }


    }
}
