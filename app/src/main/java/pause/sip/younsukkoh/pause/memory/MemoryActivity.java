package pause.sip.younsukkoh.pause.memory;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import pause.sip.younsukkoh.pause.basis.SingleFragmentActivity;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/19/2016.
 */
public class MemoryActivity extends SingleFragmentActivity {

    private static final String TAG = MemoryActivity.class.getSimpleName();

    /**
     * Initialize MemoryActivity which in turn starts Memory Fragment
     */
    public static Intent newIntent(Context context, String userEncodedEmail, String memoryId){
        Intent intent = new Intent(context, MemoryActivity.class);
        intent.putExtra(Constants.EXTRA_USER_ENCODED_EMAIL, userEncodedEmail);
        intent.putExtra(Constants.EXTRA_MEMORY_ID, memoryId);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return MemoryFragment.newInstance(getIntent().getStringExtra(Constants.EXTRA_USER_ENCODED_EMAIL), getIntent().getStringExtra(Constants.EXTRA_MEMORY_ID));
    }
}
