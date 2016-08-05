package pause.sip.younsukkoh.pause;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import pause.sip.younsukkoh.pause.my_memory.MyMemoryFragment;
import pause.sip.younsukkoh.pause.our_memories.OurMemoriesFragment;
import pause.sip.younsukkoh.pause.pojo.TesterPojo;
import pause.sip.younsukkoh.pause.settings.SettingsFragment;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button mTester;

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
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.ma_tl);
        tabLayout.setupWithViewPager(viewPager);

        //Set tabs with appropriate icons
        final int[] TAB_ICONS = new int[] { R.drawable.my_memory_brown, R.drawable.our_memories_brown, R.drawable.settings_brown };
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
                    return SettingsFragment.newInstance(mUserEncodedEmail);
                default:
                    return MyMemoryFragment.newInstance(mUserEncodedEmail);
            }

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Mine";
                case 1:
                    return "Ours";
                case 2:
                    return "Settings";
                default:
                    return "Mine";
            }
        }
    }
}
