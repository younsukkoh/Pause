package pause.sip.younsukkoh.pause.our_rooms;

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
public class RoomsFragment extends Fragment{

    /**
     * Initialize Our Memories Fragment
     * @param userEncodedEmail pass on current user's encoded email
     * @return
     */
    public static RoomsFragment newInstance(String userEncodedEmail) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_USER_ENCODED_EMAIL, userEncodedEmail);

        RoomsFragment fragment = new RoomsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.our_room_fragment, container, false);
        return view;
    }

}
