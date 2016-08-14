package pause.sip.younsukkoh.pause.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

import pause.sip.younsukkoh.pause.BaseActivity;
import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.utility.Constants;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 7/27/2016.
 */
public class LoginActivity extends BaseActivity{

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText mEmailInput, mPasswordInput;
    private Button mSignIn;
    private TextView mSignUp;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        setupUI();
        setupProgressDialog();
    }

    /**
     * Set up user interface
     */
    private void setupUI() {
        mEmailInput = (EditText) findViewById(R.id.la_et_email);
        mPasswordInput = (EditText) findViewById(R.id.la_et_password);

        mSignIn = (Button) findViewById(R.id.la_b_signIn);
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithPasswordProvider();
            }
        });

        mSignUp = (TextView) findViewById(R.id.la_tv_signUp);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity01.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Login using email and password provider
     */
    private void loginWithPasswordProvider() {
        final String email = mEmailInput.getText().toString();
        String password = mPasswordInput.getText().toString();

        if (email.equals("")) {
            mEmailInput.setError(getString(R.string.empty_error));
            return;
        }
        if (password.equals("")) {
            mPasswordInput.setError(getString(R.string.empty_error));
            return;
        }

        mProgressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.i(TAG, "signInWithEmailAndPassword Success: " + authResult.toString());
                        mProgressDialog.dismiss();
                        mSharedPreferencesEditor.putString(Constants.SP_ENCODED_USER_EMAIL, Utility.encodeEmail(email)).apply();
                        goToMainActivity(LoginActivity.this);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "signInWithEmailAndPassword Fail: " + e.getMessage());
                        mProgressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Set up progress dialog for log in
     */
    private void setupProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(R.string.loading);
        mProgressDialog.setMessage(getString(R.string.authenticating));
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //If user has logged in before thereby saving his/her email in SP, take the user to the main activity
        Log.i(TAG, "onStart " + mUserEncodedEmail);
        if (mUserEncodedEmail != null) goToMainActivity(getApplicationContext());
    }
}
