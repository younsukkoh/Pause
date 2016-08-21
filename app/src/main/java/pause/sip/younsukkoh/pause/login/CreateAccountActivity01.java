package pause.sip.younsukkoh.pause.login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import pause.sip.younsukkoh.pause.basis.BaseActivity;
import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 7/19/2016.
 */
public class CreateAccountActivity01 extends BaseActivity {

    private static String TAG = CreateAccountActivity01.class.getSimpleName();

    private EditText mFirstNameInput, mLastNameInput, mPasswordInput, mPasswordInputConfirm;
    private String mFirstName, mLastName, mPassword, mGender;
    private Button mBirthdayInput, mContinue;
    private Date mBirthday;
    private CheckBox mMaleInput, mFemaleInput;
    private TextView mSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_activity_01);
        setupUI();
    }

    /**
     * Set up user interface
     */
    private void setupUI() {
        mFirstNameInput = (EditText) findViewById(R.id.caa01_et_firstName);

        mLastNameInput = (EditText) findViewById(R.id.caa01_et_lastName);

        mPasswordInput = (EditText) findViewById(R.id.caa01_et_password);

        mPasswordInputConfirm = (EditText) findViewById(R.id.caa01_et_password_confirm);

        mMaleInput = (CheckBox) findViewById(R.id.caa01_cb_male);

        mFemaleInput = (CheckBox) findViewById(R.id.caa01_cb_female);

        setBirthday();
        setGender();

        mContinue = (Button) findViewById(R.id.caa01_b_continue);
        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupUserSignUp();
            }
        });

        mSignIn = (TextView) findViewById(R.id.caa01_tv_signIn);
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginActivity(CreateAccountActivity01.this);
            }
        });
    }

    /**
     * Get user information from input and take the user to email confirmation page if everything checks out
     */
    private void setupUserSignUp() {
        mFirstName = mFirstNameInput.getText().toString();
        mLastName = mLastNameInput.getText().toString();
        mPassword = mPasswordInput.getText().toString();

        boolean userNameValid = isUserNameValid(mFirstName, mLastName);
        boolean passwordValid = isPasswordValid(mPassword, mPasswordInputConfirm.getText().toString());
        boolean birthdayValid = isBirthdayPicked();
        boolean genderChecked = isGenderChecked();

        if (!userNameValid || !passwordValid || !birthdayValid || !genderChecked) {
            Log.i(TAG, "setupUserSignUp: " + userNameValid + " " + passwordValid + " " + birthdayValid + " " + genderChecked + " Fail");
            return;
        }
        else goToCreateAccountActivity02();
    }

    /**
     * Move to create account activity 02
     */
    private void goToCreateAccountActivity02() {
        String[] userInformation = {mFirstName, mLastName, mPassword, mGender};
        long userBirthday = mBirthday.getTime();

        Intent intent = CreateAccountActivity02.newIntent(CreateAccountActivity01.this, userInformation, userBirthday);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * When user presses birthday button, the user is asked for his/her birth date.
     * User selects the date, and when the dialog closes, date is saved in mBirthday and the date will show on the button.
     */
    private void setBirthday() {
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                mBirthday = calendar.getTime();
                mBirthdayInput.setText(Utility.DATE_FORMAT.format(mBirthday));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        mBirthdayInput = (Button) findViewById(R.id.caa01_b_birthday);
        mBirthdayInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });
    }

    /**
     * User checks one gender, the other checkbox will be disabled.
     */
    private void setGender() {
        mMaleInput = (CheckBox) findViewById(R.id.caa01_cb_male);
        mFemaleInput = (CheckBox) findViewById(R.id.caa01_cb_female);

        mMaleInput.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mGender = getString(R.string.male);
                    mFemaleInput.setEnabled(false);
                }
                else {
                    mGender = null;
                    mFemaleInput.setEnabled(true);
                }
            }
        });
        mFemaleInput.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mGender = getString(R.string.female);
                    mMaleInput.setEnabled(false);
                } else {
                    mGender = null;
                    mMaleInput.setEnabled(true);
                }
            }
        });
    }

    /**
     * @param first
     * @param last
     * @return true if neither first name and last name inputs are empty
     */
    private boolean isUserNameValid(String first, String last) {
        boolean valid = true;
        if (first.equals("")) {
            mFirstNameInput.setError(getString(R.string.empty_error));
            valid = false;
        }
        if (last.equals("")) {
            mLastNameInput.setError(getString(R.string.empty_error));
            valid = false;
        }
        return valid;
    }

    /**
     * @param password
     * @param passwordConfirm
     * @return true if password and password confirm
     */
    private boolean isPasswordValid(String password, String passwordConfirm) {
        boolean valid = true;
        //Check if passwords match
        if (!password.equals(passwordConfirm)) {
            mPasswordInputConfirm.setError(getString(R.string.password_match_error));
            valid = false;
        }
        //Check password length
        if (password.length() < 6) {
            mPasswordInput.setError(getString(R.string.password_length_error));
            valid = false;
        }
        if (passwordConfirm.length() < 6) {
            mPasswordInput.setError(getString(R.string.password_length_error));
            valid = false;
        }
        return valid;
    }

    /**
     * @return true if birth date is chosen, false otherwise
     */
    private boolean isBirthdayPicked() {
        if (mBirthday == null) {
            mBirthdayInput.setError(getString(R.string.empty_error));
            return false;
        }
        return true;
    }

    /**
     * @return true if user picked a gender, false otherwise
     */
    private boolean isGenderChecked() {
        if (!mMaleInput.isChecked() && !mFemaleInput.isChecked()) {
            mMaleInput.setError(getString(R.string.empty_error));
            mFemaleInput.setError(getString(R.string.empty_error));
            return false;
        }
        return true;
    }
}
