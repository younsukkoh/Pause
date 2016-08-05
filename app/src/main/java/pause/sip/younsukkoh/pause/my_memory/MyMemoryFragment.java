package pause.sip.younsukkoh.pause.my_memory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class MyMemoryFragment extends Fragment {

    private String mUserEncodedEmail;

    /**
     * Initialize My Memory Fragment
     * @param userEncodedEmail pass on current user's encoded email
     * @return
     */
    public static MyMemoryFragment newInstance(String userEncodedEmail) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_ENCODED_EMAIL, userEncodedEmail);

        MyMemoryFragment fragment = new MyMemoryFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_memory_fragment, container, false);
        return view;
    }

}
