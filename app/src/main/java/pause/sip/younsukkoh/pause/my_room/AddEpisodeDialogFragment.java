package pause.sip.younsukkoh.pause.my_room;

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
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.pojo.Episode;
import pause.sip.younsukkoh.pause.pojo.Memory;
import pause.sip.younsukkoh.pause.utility.Constants;
import pause.sip.younsukkoh.pause.utility.Utility;

/**
 * Created by Younsuk on 8/18/2016.
 */
public class AddEpisodeDialogFragment extends android.app.DialogFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static String TAG = AddEpisodeDialogFragment.class.getSimpleName();

    private String mUserEncodedEmail, mMemoryId;
    private File mImageFile;
    private Button mCameraButton, mRecorderButton, mDocumentButton, mPhotoButton, mCancelButton;
    private DatabaseReference mMainDatabaseRef;

    //Where
    protected GoogleApiClient mGoogleApiClient;
    protected double mLatitude;
    protected double mLongitude;
    protected String mLocation;

    /**
     * Initialize My Memory Dialog Fragment
     */
    public static AddEpisodeDialogFragment newInstance(String userEncodedEmail, String memoryId) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_USER_ENCODED_EMAIL, userEncodedEmail);
        args.putString(Constants.ARG_MEMORY_ID, memoryId);

        AddEpisodeDialogFragment fragment = new AddEpisodeDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserEncodedEmail = getArguments().getString(Constants.ARG_USER_ENCODED_EMAIL);
        mMemoryId = getArguments().getString(Constants.ARG_MEMORY_ID);

        mMainDatabaseRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_episode_dialog_fragment, container, false);

        mCameraButton = (Button) view.findViewById(R.id.mrdf_b_camera);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForPermission_CameraButton();
                launchCamera();
            }
        });

        mRecorderButton = (Button) view.findViewById(R.id.mrdf_b_recorder);
        mRecorderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mDocumentButton = (Button) view.findViewById(R.id.mrdf_b_document);
        mDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mPhotoButton = (Button) view.findViewById(R.id.mrdf_b_photo);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mCancelButton = (Button) view.findViewById(R.id.mrdf_b_cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    /**
     * If build version is less than M, then no need to check for runtime permission. Otherwise, check for permission.
     */
    public void checkForPermission_CameraButton() {
        Log.i(TAG, "checkForPermission;");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) return;

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, Constants.REQUEST_CAMERA_BUTTON_PERMISSIONS);
    }

    /**
     * Start camera
     */
    public void launchCamera() {
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
                            String newEpisode_Download_URL = taskSnapshot.getDownloadUrl().toString();
                            //Using memory id and download url upload episode into database
                            uploadEpisodeToDatabase(newEpisode_Download_URL);

                            Log.i(TAG, "uploadEpisodeToFirebaseStorage; uploadTask onSuccess; " + newEpisode_Download_URL);
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
        }
    }

    /**
     * Store the episode into database
     */
    private void uploadEpisodeToDatabase(final String newEpisode) {
        Log.i(TAG, "uploadEpisodeToDatabase");

        //Update my_room_EMAIL
        final DatabaseReference myMemoryDatabaseRef = mMainDatabaseRef.child(Constants.MY_ROOM + mUserEncodedEmail).child(mMemoryId);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Memory memory = dataSnapshot.getValue(Memory.class);

                ArrayList<String> episodes = (ArrayList<String>) memory.getEpisodes().get(Constants.LIST);
                episodes.add(newEpisode);

                HashMap<String, Object> updatedEpisodes = new HashMap<String, Object>();
                updatedEpisodes.put(Constants.LIST, episodes);

                int updatedNumberOfEpisodes = memory.getNumberOfEpisodes() + 1;

                HashMap<String, Object> updatedMemoryInfo = new HashMap<String, Object>();
                updatedMemoryInfo.put(Constants.EPISODES, updatedEpisodes);
                updatedMemoryInfo.put(Constants.NUMBER_OF_EPISODES, updatedNumberOfEpisodes);

                myMemoryDatabaseRef.updateChildren(updatedMemoryInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        };
        myMemoryDatabaseRef.addListenerForSingleValueEvent(valueEventListener);
        myMemoryDatabaseRef.removeEventListener(valueEventListener);

        //Add episode to current memory
        DatabaseReference episodeDatabaseRef = mMainDatabaseRef.child(Constants.MY_ROOM + mUserEncodedEmail + Constants.UNDERSCORE + mMemoryId).push();
        Episode episode = new Episode(newEpisode, new Date().getTime(), mLocation, mLongitude, mLatitude);
        episodeDatabaseRef.setValue(episode);

        dismiss();
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
                    .enableAutoManage((FragmentActivity) getActivity(), 0, this)
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
}
