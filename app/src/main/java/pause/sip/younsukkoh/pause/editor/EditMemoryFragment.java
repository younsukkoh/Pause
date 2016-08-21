package pause.sip.younsukkoh.pause.editor;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/21/2016.
 */
public class EditMemoryFragment extends Fragment {

    /**
     * Initialize Memory Fragment
     * @param memoryId pass on current user memory's id
     * @return
     */
    public static EditMemoryFragment newInstance(String userEncodedEmail, String memoryId) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_MEMORY_ID, memoryId);
        args.putString(Constants.ARG_USER_ENCODED_EMAIL, userEncodedEmail);

        EditMemoryFragment fragment = new EditMemoryFragment();
        fragment.setArguments(args);

        return fragment;
    }

}
