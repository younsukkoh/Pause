package pause.sip.younsukkoh.pause.editor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import pause.sip.younsukkoh.pause.basis.SingleFragmentActivity;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/21/2016.
 */
public class EditMemoryActivity extends SingleFragmentActivity {

    private static final String TAG = EditMemoryActivity.class.getSimpleName();

    /**
     * Initialize Edit Memory Activity which in turn starts Memory Fragment
     */
    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, EditMemoryActivity.class);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }
}
