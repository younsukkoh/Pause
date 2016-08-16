package pause.sip.younsukkoh.pause.my_memory;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.Episode;
import pause.sip.younsukkoh.pause.pojo.Memory;
import pause.sip.younsukkoh.pause.utility.Constants;
import pause.sip.younsukkoh.pause.utility.Utility;

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

    //What
    private File mImageFile;
    private Uri mDownloadUrl;

    //Where
    private GoogleApiClient mGoogleApiClient;
    private double mLatitude;
    private double mLongitude;
    private String mLocation;

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
                .addConnectionCallbacks(this) //As connection happens, the latitude, the longitude and the location will be setup.
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
                checkForPermission_CameraButton();
                launchCamera();
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
     * If build version is less than M, then no need to check for runtime permission. Otherwise, check for permission.
     */
    private void checkForPermission_CameraButton() {
        Log.i(Constants.TAG_DEBUG, "checkForPermission;");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) return;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, Constants.REQUEST_CAMERA_BUTTON_PERMISSIONS);
    }

    /**
     * Start camera
     */
    private void launchCamera() {
        Log.i(Constants.TAG_DEBUG, "launchCamera;");
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA }, Constants.REQUEST_CAMERA);
            }
        }

        mImageFile = createFileInExternalStorage();

        if (mImageFile != null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
            startActivityForResult(intent, Constants.REQUEST_IMAGE_CAPTURE); //Result handled in onActivityResult
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Log.i(Constants.TAG_DEBUG, "onActivityResult; resultCode OK");
            uploadToFirebaseStorage();
        }
    }

    /**
     * Upload the image into Firebase Storage
     */
    private void uploadToFirebaseStorage() {
        Log.i(Constants.TAG_DEBUG, "uploadToFirbaseStorage;");

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReferenceFromUrl(Constants.FIREBASE_STORAGE_URL);

        try {
            InputStream inputStream = new FileInputStream(mImageFile);

            StorageReference fileRef = storageRef.child(Constants.IMAGES + mImageFile.getName());

            UploadTask uploadTask = fileRef.putStream(inputStream);

            Log.i(Constants.TAG_DEBUG, "uploadToFirebaseStorage; " + uploadTask.isSuccessful());

            uploadTask
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Retrieve download url into String.class
                            String downloadUrl_String = taskSnapshot.getDownloadUrl().toString();
                            //Upload memory into database and receive memoryId
                            String memoryId = uploadMemoryToDatabase(downloadUrl_String);
                            //Using memory id and download url upload episode into database
                            uploadEpisodeToDatabase(memoryId, downloadUrl_String);

                            Log.i(Constants.TAG_DEBUG, "uploadToFirebaseStorage; uploadTask onSuccess; " + downloadUrl_String);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(Constants.TAG_DEBUG, "uploadToFirebaseStorage; uploadTask onFailure; " + e.getMessage());
                        }
                    });
        }
        catch (IOException ioe) {
            Log.e(Constants.TAG_DEBUG, "uploadToFirebaseStorage; InputStream failed; " + ioe.getMessage());
        }
        finally {
            mImageFile.delete();
        }
    }

    /**
     * Store the episode into database
     */
    private void uploadEpisodeToDatabase(String memoryId, String downloadUrl) {
        Log.i(Constants.TAG_DEBUG, "uploadEpisodeToDatabase");

        DatabaseReference episodeRef = mMainDatabaseRef.child(Constants.MY_ROOM + mUserEncodedEmail + Constants.UNDERSCORE + memoryId).push();
        Episode episode = new Episode(downloadUrl, new Date().getTime(), mLocation, mLongitude, mLatitude);
        episodeRef.setValue(episode);
    }

    /**
     * Store memory into database
     * @return unique memory id created from push()
     */
    private String uploadMemoryToDatabase(String downloadUrl) {
        Log.i(Constants.TAG_DEBUG, "uploadMemoryToDatabase");

        DatabaseReference databaseReference = mMyMemoryRef.push();

        String memoryId = databaseReference.getKey();

        //Latitude, longitude, and the location is obtained from onConnected
        Memory memory = new Memory(memoryId, mUserEncodedEmail, downloadUrl, new Date().getTime(), mLocation, mLongitude, mLatitude);
        databaseReference.setValue(memory);

        animateFloatingActionButton();

        return memoryId;
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

    private File createFileInExternalStorage() {
        //Create a folder if it does not exist yet
        String folderName = getActivity().getString(getActivity().getApplicationInfo().labelRes);
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), folderName);
        if (!folder.exists()) folder.mkdirs();

        //Create the file
        String fileName = Utility.DATE_TIME_LABEL.format(new Date());
        return new File(folder.getPath() + File.separator + "IMG_" + fileName + ".jpg");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.i(Constants.TAG_DEBUG, "onRequestPermissionsResult");
        if (grantResults.length == 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.i(Constants.TAG_DEBUG, "onRequestPermissionsResult, Permission not granted.");
            return;
        }

        if (requestCode == Constants.REQUEST_CAMERA_BUTTON_PERMISSIONS) {
            Log.i(Constants.TAG_DEBUG, "onRequestPermissionsResult; permission granted");
            return;
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
                    mLocation = place.getName().toString();

                    placeLikelihoods.release();
                }
            });
        }
        else {
            Log.i(TAG, "onConnected; location is not null.");

            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();

            try {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1); //1 is the max result

                mLocation = addresses.get(0).getAddressLine(0);
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        Log.i(Constants.TAG_DEBUG, "onConnected; Lat: " + mLatitude + ". Long: " + mLongitude + ". Location: " + mLocation);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMyMemoryAdapter.cleanup();
    }

}
