package pause.sip.younsukkoh.pause;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pause.sip.younsukkoh.pause.login.LoginActivity;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 7/20/2016.
 */
public class BaseActivity extends AppCompatActivity {

    //TODO save the user email in DB
    public String mUserEncodedEmail;
    public DatabaseReference mDatabase;
    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mSharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    @Override
    public void onStart() {
        super.onStart();
        mUserEncodedEmail = mSharedPreferences.getString(Constants.SP_ENCODED_USER_EMAIL, null);
    }

    /**
     * Move to login activity
     */
    public void goToLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}
