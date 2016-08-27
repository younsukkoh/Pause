package pause.sip.younsukkoh.pause.basis;

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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
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
 * Basis for Fragment
 * Main functions
 * (1) Request permissions for taking picture, launch camera, save the image taken in Storage and its information in Database
 * (2) Animation for FAB
 * (3) Retrieving location of the user once the fragment is created
 * Created by Younsuk on 8/14/2016.
 */
public class BaseFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = BaseFragment.class.getSimpleName();

    protected String mUserEncodedEmail;
    protected DatabaseReference mMainDatabaseRef;
    protected DatabaseReference mCurrentDatabaseRef;

    //What
    protected File mImageFile;

    //Where
    protected GoogleApiClient mGoogleApiClient;
    protected double mLatitude;
    protected double mLongitude;
    protected String mLocation;

    protected Boolean mIsFabOpen = false;
    protected Animation mFabOpen,mFabClose, mRotateForward, mRotateBackward;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserEncodedEmail = getArguments().getString(Constants.ARG_USER_ENCODED_EMAIL);
        mMainDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this) //As connection happens, the latitude, the longitude and the location will be setup.
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void setUpUI(View view) {
        mFabOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        mFabClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        mRotateForward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        mRotateBackward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);
    }

    /**
     * If build version is less than M, then no need to check for runtime permission. Otherwise, check for permission.
     */
    protected void checkForPermission_CameraButton() {
        Log.i(TAG, "checkForPermission;");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) return;

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, Constants.REQUEST_CAMERA_BUTTON_PERMISSIONS);
    }

    /**
     * Start camera
     */
    protected void launchCamera() {
        Log.i(TAG, "launchCamera;");
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

    /**
     * Create file in the folder with application's name.
     * File takes current time as its name
     */
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
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Log.i(TAG, "onActivityResult; resultCode OK");
            uploadEpisodeToFirebaseStorage();
        }
        else {
            Log.i(TAG, "onActivityResult; resultCode " + resultCode);
        }
    }

    /**
     * Upload the image into Firebase Storage
     */
    private void uploadEpisodeToFirebaseStorage() {
        Log.i(TAG, "uploadEpisodeToFirbaseStorage;");

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReferenceFromUrl(Constants.FIREBASE_STORAGE_URL);

        try {
            InputStream inputStream = new FileInputStream(mImageFile);

            StorageReference fileRef = storageRef.child(Constants.IMAGES + mImageFile.getName());

            UploadTask uploadTask = fileRef.putStream(inputStream);

            Log.i(TAG, "uploadEpisodeToFirebaseStorage; " + uploadTask.isSuccessful());

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

                            Log.i(TAG, "uploadEpisodeToFirebaseStorage; uploadTask onSuccess; " + downloadUrl_String);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "uploadEpisodeToFirebaseStorage; uploadTask onFailure; " + e.getMessage());
                        }
                    });
        }
        catch (IOException ioe) {
            Log.e(TAG, "uploadEpisodeToFirebaseStorage; InputStream failed; " + ioe.getMessage());
        }
        finally {
            mImageFile.delete();
            animateFloatingActionButton();
        }
    }

    /**
     * Store the episode into database
     */
    protected void uploadEpisodeToDatabase(String memoryId, String downloadUrl) {
        Log.i(TAG, "uploadEpisodeToDatabase");

        DatabaseReference episodeRef = mMainDatabaseRef.child(Constants.MY_ROOM_ + mUserEncodedEmail + Constants.UNDERSCORE + memoryId).push();
        Episode episode = new Episode(downloadUrl, new Date().getTime(), mLocation, mLongitude, mLatitude);
        episodeRef.setValue(episode);
    }

    /**
     * Store memory into database
     * @return unique memory id created from push()
     */
    protected String uploadMemoryToDatabase(String downloadUrl) {
        Log.i(TAG, "uploadMemoryToDatabase");

        DatabaseReference databaseReference = mCurrentDatabaseRef.push();

        String memoryId = databaseReference.getKey();

        //Latitude, longitude, and the location is obtained from onConnected
        Memory memory = new Memory(memoryId, mUserEncodedEmail, downloadUrl, new Date().getTime(), mLocation, mLongitude, mLatitude);
        databaseReference.setValue(memory);

        return memoryId;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult");
        if (grantResults.length == 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onRequestPermissionsResult, Permission not granted.");
            return;
        }

        if (requestCode == Constants.REQUEST_CAMERA_BUTTON_PERMISSIONS) {
            Log.i(TAG, "onRequestPermissionsResult; permission granted");
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
            ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION }, Constants.REQUEST_ACCESS_FINE_LOCATION);

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

        Log.i(TAG, "onConnected; Lat: " + mLatitude + ". Long: " + mLongitude + ". Location: " + mLocation);
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

    protected void animateFloatingActionButton() {
    }

    @Override
    public void onResume(){
        super.onResume();
        if (!mGoogleApiClient.isConnected()) mGoogleApiClient.connect();
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mGoogleApiClient.isConnected()) mGoogleApiClient.disconnect();
    }
}
