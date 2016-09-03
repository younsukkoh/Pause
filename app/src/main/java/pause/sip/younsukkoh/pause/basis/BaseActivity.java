package pause.sip.younsukkoh.pause.basis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pause.sip.younsukkoh.pause.MainActivity;
import pause.sip.younsukkoh.pause.login.LoginActivity;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 7/20/2016.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    protected String mUserEncodedEmail;
    protected DatabaseReference mMainDatabaseRef; //Database reference
    protected SharedPreferences mSharedPreferences; //Used for saving essential information such as user email
    protected SharedPreferences.Editor mSharedPreferencesEditor;
    protected FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mSharedPreferencesEditor = mSharedPreferences.edit();

        mAuth = FirebaseAuth.getInstance(); //Used in CAA02 for creating account. Used in LA for log in.
    }

    @Override
    public void onStart() {
        super.onStart();
        mUserEncodedEmail = mSharedPreferences.getString(Constants.SP_ENCODED_USER_EMAIL, null);
    }

    /**
     * Move to login activity
     */
    protected void goToLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Go to main activity
     */
    protected void goToMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
