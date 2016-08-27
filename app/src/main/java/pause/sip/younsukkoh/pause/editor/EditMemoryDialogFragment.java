package pause.sip.younsukkoh.pause.editor;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.Memory;
import pause.sip.younsukkoh.pause.utility.Constants;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 8/21/2016.
 */
public class EditMemoryDialogFragment extends DialogFragment {

    private static final String TAG = EditMemoryDialogFragment.class.getSimpleName();

    private Memory mMemory;
    private String mUserEncodedEmail;
    private String mMemoryId;
    private DatabaseReference mMemoryDatabaseRef;
    private ValueEventListener mMemoryValueEventListener;

    private EditText mTitleEditText, mLocationEditText, mDescriptionEditText;
    private Button mPeopleButton, mDateTimeButton;
    private ImageButton mLocationImageButton;

    /**
     * Initialize Edit Memory Dialog Fragment
     */
    public static EditMemoryDialogFragment newInstance(String userEncodedEmail, String memoryId) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_MEMORY_ID, memoryId);
        args.putString(Constants.ARG_USER_ENCODED_EMAIL, userEncodedEmail);

        EditMemoryDialogFragment fragment = new EditMemoryDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserEncodedEmail = getArguments().getString(Constants.ARG_USER_ENCODED_EMAIL);
        mMemoryId = getArguments().getString(Constants.ARG_MEMORY_ID);
        mMemoryDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.MY_ROOM_ + mUserEncodedEmail).child(mMemoryId);

        mMemoryDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMemory = dataSnapshot.getValue(Memory.class);
                mMemoryDatabaseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.edit_memory_fragment, null);
        setUpUI(view);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.edit_memory)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateFirebaseDatabase();
                    }
                });
        alertDialogBuilder.setView(view);

        return alertDialogBuilder.create();
    }

    /**
     * Set up user interface
     */
    private void setUpUI(View view) {
        mTitleEditText = (EditText) view.findViewById(R.id.emf_et_title);

        mPeopleButton = (Button) view.findViewById(R.id.emf_b_people);
        mPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Add friends once the function to add friend goes online
            }
        });

        mDateTimeButton = (Button) view.findViewById(R.id.emf_b_timeCreated);
        mDateTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long dateTime = parseTextToLong(mDateTimeButton.getText().toString());
                FragmentManager fm = getFragmentManager();
                EditDateTimeDialogFragment editDateTimeDialogFragment = EditDateTimeDialogFragment.newInstance(dateTime);
                editDateTimeDialogFragment.setTargetFragment(EditMemoryDialogFragment.this, Constants.REQUEST_TARGET_EMDF);
                editDateTimeDialogFragment.show(fm, TAG);
            }
        });

        mLocationEditText = (EditText) view.findViewById(R.id.emf_et_location);

        mLocationImageButton = (ImageButton) view.findViewById(R.id.emf_ib_location);
        mLocationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO mMemory getLatitude is not returning full double only the integer part
                launchPlacePicker(mMemory.getLatitude(), mMemory.getLongitude());
            }
        });

        mDescriptionEditText = (EditText) view.findViewById(R.id.emf_et_description);

        mMemoryDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Memory memory = dataSnapshot.getValue(Memory.class);

                mTitleEditText.setText(memory.getTitle());
                mDateTimeButton.setText(Utility.DATE_TIME_FORMAT.format(new Date(memory.getTimeCreated())));
                mLocationEditText.setText(memory.getLocation());
                mDescriptionEditText.setText(memory.getDescription());

                mMemoryDatabaseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

    /**
     * Update Firebase Database with updated information
     */
    private void updateFirebaseDatabase() {
        String title = mTitleEditText.getText().toString();
        long dateTime = parseTextToLong(mDateTimeButton.getText().toString());
        String description = mDescriptionEditText.getText().toString();

        HashMap<String, Object> updatedInfo = new HashMap<>();
        updatedInfo.put(Constants.TITLE, title);
        updatedInfo.put(Constants.TIME_CREATED, dateTime);
        updatedInfo.put(Constants.DESCRIPTION, description);
        mMemoryDatabaseRef.updateChildren(updatedInfo);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == Constants.REQUEST_TARGET_EMDF) {
            Date dateTime = (Date) data.getSerializableExtra(Constants.EXTRA_DATE_TIME);
            mDateTimeButton.setText(Utility.DATE_TIME_FORMAT.format(dateTime));
        }

        else if (requestCode == Constants.REQUEST_PLACE_PICKER) {
            Place place = PlacePicker.getPlace(getActivity(), data);
            LatLngBounds latLngBounds = PlacePicker.getLatLngBounds(data);
            updateFirebaseDatabse_Where(place, latLngBounds);
        }
    }

    /**
     * Parse text of date button to long
     */
    private long parseTextToLong(String text) {
        long dateTime = 0;
        try {
            dateTime = Utility.DATE_TIME_FORMAT.parse(text).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    private void updateFirebaseDatabse_Where(Place place, LatLngBounds latLngBounds) {
        String location = place.getName().toString();
        double latitude = latLngBounds.getCenter().latitude;
        double longitude = latLngBounds.getCenter().longitude;

        HashMap<String, Object> updatedInfo = new HashMap<>();
        updatedInfo.put(Constants.LOCATION, location);
        updatedInfo.put(Constants.LATITUDE, latitude);
        updatedInfo.put(Constants.LONGITUDE, longitude);
        mMemoryDatabaseRef.updateChildren(updatedInfo);

        mLocationEditText.setText(location);
    }

    private void launchPlacePicker(double latitude, double longitude) {
        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder().include(new LatLng(latitude, longitude));

        LatLng center = latLngBoundsBuilder.build().getCenter();
        LatLng northEast = SphericalUtil.computeOffset(center, 700, 45); //center, distance, degree starting from north
        LatLng southWest = SphericalUtil.computeOffset(center, 700, 225);

        latLngBoundsBuilder.include(center).include(northEast).include(southWest);

        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        intentBuilder.setLatLngBounds(latLngBoundsBuilder.build());
        try {
            Intent intent = intentBuilder.build(getActivity());
            startActivityForResult(intent, Constants.REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
}
