package pause.sip.younsukkoh.pause.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;

import java.util.Random;

import pause.sip.younsukkoh.pause.basis.BaseActivity;
import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.User;
import pause.sip.younsukkoh.pause.utility.Constants;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 7/19/2016.
 */
public class CreateAccountActivity02 extends BaseActivity {

    private static String TAG = CreateAccountActivity02.class.getSimpleName();

    private ProgressDialog mAuthProgressDialog;

    private String mFirstName, mLastName, mPassword, mGender, mEmail;
    private long mBirthday;
    private String mConfirmationCode;
    private EditText mEmailInput, mEmailInputConfirm, mConfirmationCodeInput;
    private Button mSendConfirmationEmail, mConfirm;
    private TextView mConfirmationCodeText, mSignIn;

    public static Intent newIntent(Context context, String[] information, long birthday) {
        Intent intent = new Intent(context, CreateAccountActivity02.class);
        intent.putExtra(Constants.EXTRA_USER_INFORMATION, information);
        intent.putExtra(Constants.EXTRA_USER_BIRTHDAY, birthday);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_activity_02);

        String[] userInformation = getIntent().getStringArrayExtra(Constants.EXTRA_USER_INFORMATION);
        long userBirthday = getIntent().getLongExtra(Constants.EXTRA_USER_BIRTHDAY, 0);

        mFirstName = userInformation[0];
        mLastName = userInformation[1];
        mPassword = userInformation[2];
        mGender = userInformation[3];
        mBirthday = userBirthday;

        setupUI();
        setupProgressDialog();
    }

    /**
     * Set up user interface
     */
    private void setupUI() {
        mEmailInput = (EditText) findViewById(R.id.caa02_et_email);
        mEmailInputConfirm = (EditText) findViewById(R.id.caa02_et_email_confirm);

        //TODO Beta phase, the confirmation email should be sent to the user along with
        mConfirmationCodeText = (TextView) findViewById(R.id.caa02_tv_confirmationCode);
        mConfirmationCodeInput = (EditText) findViewById(R.id.caa02_et_confirmationCode);

        mSendConfirmationEmail = (Button) findViewById(R.id.caa02_send_confirm_email);
        mSendConfirmationEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmail = mEmailInput.getText().toString().toLowerCase();
                String userEmailConfirm = mEmailInputConfirm.getText().toString().toLowerCase();

                //TODO Current: set code to show up in the edit text, but change it so that code is sent to user email
                if (areEmailsValid(mEmail, userEmailConfirm)) {
                    Random random = new Random();
                    int confirmationCode = random.nextInt(999999 - 100000) + 100001;
                    mConfirmationCode = "" + confirmationCode;

                    mConfirmationCodeInput.setText(mConfirmationCode);

                    setupUIAfterEmail();
                }
                else
                    mConfirmationCodeInput.setError(getString(R.string.confirm_error));

            }
        });

        mConfirm = (Button) findViewById(R.id.caa02_b_confirm);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInputConfirmationCode = mConfirmationCodeInput.getText().toString();

                if (mConfirmationCode.equals(userInputConfirmationCode)) {
                    mAuthProgressDialog.show();
                    createUserInFirebaseAuth();
                }
                else
                    mConfirmationCodeInput.setError(getString(R.string.confirm_error));
            }
        });
        //

        mSignIn = (TextView) findViewById(R.id.caa02_tv_signIn);
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginActivity(CreateAccountActivity02.this);
            }
        });

    }

    /**
     * Add user in Firebase Auth
     */
    private void createUserInFirebaseAuth() {
        mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.i(TAG, "createUserWithEmailAndPassword Success: " + authResult.toString());

                        createUserInFirebaseDatabase();

                        Toast.makeText(CreateAccountActivity02.this, R.string.signUp_successful, Toast.LENGTH_LONG).show();

                        goToLoginActivity(CreateAccountActivity02.this);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "createUserWithEmailAndPassword Failure: " + e.getMessage());
                    }
                });
    }

    /**
     * Add user in Firebase Database
     */
    private void createUserInFirebaseDatabase() {
        String encodedUserEmail = Utility.encodeEmail(mEmail);
        User user = new User(mEmail, mFirstName, mLastName, mGender, mBirthday);

        DatabaseReference userRef = mMainDatabaseRef.child(Constants.USERS_INFO_ + encodedUserEmail);
        userRef.setValue(user);
    }



    /**
     * Check if both emails are not null, valid and match
     */
    private boolean areEmailsValid(String email, String confirmEmail) {
        boolean valid = true;
        //Check if either email or confirm email is null
        if (email == null) {
            mEmailInput.setError(getString(R.string.empty_error));
            valid = false;
        }
        if (confirmEmail == null) {
            mEmailInputConfirm.setError(getString(R.string.empty_error));
            valid = false;
        }

        //Check if either email or confirm email has the right pattern
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailInput.setError(getString(R.string.email_invalid_error));
            valid = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(confirmEmail).matches()) {
            mEmailInputConfirm.setError(getString(R.string.email_invalid_error));
            valid = false;
        }

        //Check if emails match
        if (!email.equals(confirmEmail)) {
            mEmailInput.setError(getString(R.string.email_match_error));
            mEmailInputConfirm.setError(getString(R.string.email_match_error));
            valid = false;
        }

        return valid;
    }

    /**
     * Change the user interface after sending confirmation email //TODO
     */
    private void setupUIAfterEmail() {
        mSendConfirmationEmail.setText(getString(R.string.resend_email));
        mConfirmationCodeText.setEnabled(true);
        mConfirmationCodeText.setTextColor(getResources().getColor(R.color.black));
        mConfirmationCodeInput.setEnabled(true);
        mConfirm.setEnabled(true);
        mConfirm.setTextColor(getResources().getColor(R.color.white));
    }

    /**
     * Set up progress dialog
     */
    private void setupProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getString(R.string.loading));
        mAuthProgressDialog.setMessage(getString(R.string.creating_account));
        mAuthProgressDialog.setCancelable(false);
    }
}
