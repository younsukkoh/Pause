package pause.sip.younsukkoh.pause;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import pause.sip.younsukkoh.pause.basis.BaseActivity;
import pause.sip.younsukkoh.pause.room.my_room.MyRoomFragment;
import pause.sip.younsukkoh.pause.our_rooms.OurRoomsFragment;
import pause.sip.younsukkoh.pause.settings.SettingsFragment;
import pause.sip.younsukkoh.pause.users_friends.UsersFriendsFragment;

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
                    return MyRoomFragment.newInstance(mUserEncodedEmail);
                case 1:
                    return OurRoomsFragment.newInstance(mUserEncodedEmail);
                case 2:
                    return UsersFriendsFragment.newInstance(mUserEncodedEmail);
                case 3:
                    return SettingsFragment.newInstance(mUserEncodedEmail);
                default:
                    return MyRoomFragment.newInstance(mUserEncodedEmail);
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
