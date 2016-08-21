package pause.sip.younsukkoh.pause.basis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import pause.sip.younsukkoh.pause.R;

/**
 * Created by Younsuk on 8/28/2015.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.single_fragment_activity_frameLayout);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction().add(R.id.single_fragment_activity_frameLayout, fragment).commit();
        }
    }

    /**
     *
     * @return Fragment the screen will take up
     */
    protected abstract Fragment createFragment();
}
