package pause.sip.younsukkoh.pause.my_memory;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.internal.GamesContract;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.Memory;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class MyMemoryFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MyMemoryFragment.class.getSimpleName();

    private String mUserEncodedEmail;
    private DatabaseReference mMainDatabaseRef;
    private DatabaseReference mMyMemoryRef;
    private RecyclerView mRecyclerView;
    private MyMemoryAdapter mMyMemoryAdapter;

    private GoogleApiClient mGoogleApiClient;
    private double mLatitude;
    private double mLongitude;
    private String mAddress;

    private Boolean mIsFabOpen = false;
    private FloatingActionButton mMainFab, mCameraFab, mRecorderFab, mPencilFab;
    private Animation mFabOpen,mFabClose, mRotateForward, mRotateBackward;

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
        mUserEncodedEmail = getArguments().getString(Constants.ARG_ENCODED_EMAIL);
        mMainDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mMyMemoryRef = mMainDatabaseRef.child(Constants.MY_ROOM + mUserEncodedEmail);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this) //As connection happens, the latitude, the longitude and the address will be setup.
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_memory_fragment, container, false);
        setUpUI(view);
        setUpRecyclerView(view);
        return view;
    }

    private void setUpUI(View view) {
        mFabOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        mFabClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        mRotateForward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        mRotateBackward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);

        mMainFab = (FloatingActionButton) view.findViewById(R.id.mmf_fab_main);
        mMainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFloatingActionButton();
            }
        });

        mCameraFab = (FloatingActionButton) view.findViewById(R.id.mmf_fab_camera);
        mCameraFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateCameraButton();
            }
        });

        mRecorderFab = (FloatingActionButton) view.findViewById(R.id.mmf_fab_recorder);
        mRecorderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mPencilFab = (FloatingActionButton) view.findViewById(R.id.mmf_fab_pencil);
        mPencilFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /**
     * Start camera, take a picture, create new memory with the given content
     */
    private void activateCameraButton() {
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, Constants.REQUEST_CAMERA_BUTTON_PERMISSIONS);
//        }

        Log.i(Constants.TAG_DEBUG, "activateCameraButton " + mGoogleApiClient.isConnected());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, Constants.REQUEST_CAMERA_BUTTON_PERMISSIONS);

        DatabaseReference databaseReference = mMyMemoryRef.push();

        String memoryId = databaseReference.getKey();

        HashMap<String, String> people = new HashMap<String, String>();
        people.put("0", mUserEncodedEmail);

        int numberOfPeople = 1;

        //TODO Get download url from the camera
        HashMap<String, String> episodes = new HashMap<String, String>();
        episodes.put("0", "downloadUrl");
        int numberOfEpisodes= 1;

        long timeCreated = new Date().getTime();

        //Latitude, longitude, and the address is obtained from onConnected
        Log.i(Constants.TAG_DEBUG, "Lat: " + mLatitude + ". Long: " + mLongitude + ". Address: " + mAddress);

//        Memory memory = new Memory(databaseReference.getKey(), title, timeCreated, location, longitude, latitude, description);
//        databaseReference.setValue(memory);

        animateFloatingActionButton();
    }

    /**
     * Set up recycler view for memories
     */
    private void setUpRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mmf_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mMyMemoryAdapter = new MyMemoryAdapter(Memory.class, R.layout.my_memory_view_holder, MyMemoryViewHolder.class, mMyMemoryRef, mUserEncodedEmail);
        mRecyclerView.setAdapter(mMyMemoryAdapter);
    }

    /**
     * Animate floating action button to the right or left depending on its current position
     */
    private void animateFloatingActionButton() {
        if (mIsFabOpen) {
            mMainFab.startAnimation(mRotateBackward);

            mCameraFab.startAnimation(mFabClose);
            mRecorderFab.startAnimation(mFabClose);
            mPencilFab.startAnimation(mFabClose);

            mCameraFab.setClickable(false);
            mRecorderFab.setClickable(false);
            mPencilFab.setClickable(false);

            mIsFabOpen = false;
        }
        else {
            mMainFab.startAnimation(mRotateForward);

            mCameraFab.startAnimation(mFabOpen);
            mRecorderFab.startAnimation(mFabOpen);
            mPencilFab.startAnimation(mFabOpen);

            mCameraFab.setClickable(true);
            mRecorderFab.setClickable(true);
            mPencilFab.setClickable(true);

            mIsFabOpen = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMyMemoryAdapter.cleanup();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length == 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "onRequestPermissionsResult, Permission not granted.");
            return;
        }

        if (requestCode == Constants.REQUEST_CAMERA_BUTTON_PERMISSIONS) {

        }

    }

    /**
     * As Google API Client is connected, the user is provided with current latitude and longitude.
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Check again for permission
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION }, Constants.REQUEST_ACCESS_FINE_LOCATION);

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            Log.i(TAG, "onConnected; location is null.");
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(getActivity(), 0, this)
                    .build();

            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(@NonNull PlaceLikelihoodBuffer placeLikelihoods) {
                    Place place = placeLikelihoods.get(0).getPlace();

                    mLatitude = place.getLatLng().latitude;
                    mLongitude = place.getLatLng().longitude;
                    mAddress = place.getName().toString();

                    placeLikelihoods.release();
                }
            });
        }
        else {
            Log.i(TAG, "onConnected; location is not null.");

            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();

            StringBuilder address = new StringBuilder();
            try {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1); //1 is the max result
                if (addresses.size() > 0){
                    android.location.Address a = addresses.get(0);
                    for (int i = 0; i < a.getMaxAddressLineIndex(); i ++) //Get full street address
                        address.append(a.getAddressLine(i)).append(" ");
                }
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }

            mAddress = address.toString();
        }

        Log.i(Constants.TAG_DEBUG, "onConnected; Lat: " + mLatitude + ". Long: " + mLongitude + ". Address: " + mAddress);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), Constants.REQUEST_CONNECTION_FAILURE_SOLUTION);
            }
            catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
        else Log.e(TAG, "onConnectionFailed. No solution found. " + connectionResult.getErrorMessage());
    }

    @Override
    public void onResume(){
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }
}
