package pause.sip.younsukkoh.pause.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.login.LoginActivity;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class SettingsFragment extends Fragment {

    private String mUserEncodedEmail;
    private Button mLogoutButton;
    /**
     * Initialize Settings Fragment
     * @param userEncodedEmail pass on current user's encoded email
     * @return
     */
    public static SettingsFragment newInstance(String userEncodedEmail) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_USER_ENCODED_EMAIL, userEncodedEmail);

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        setUpUI(view);
        return view;
    }

    /**
     * Set up user interface
     */
    private void setUpUI(View view) {
        mLogoutButton = (Button) view.findViewById(R.id.sf_b_logout);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    /**
     * Log the current user out of the application
     */
    private void logout() {
        //Sign out of Firebase auth
        FirebaseAuth.getInstance().signOut();

        //Make mUserEncodedEmail null, which is used to see if the user logged in before
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString(Constants.SP_ENCODED_USER_EMAIL, null).apply();

        //Take the user back to Login Activity
        goToLoginActivity(getActivity());
    }

    /**
     * Move to login activity
     */
    private void goToLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
