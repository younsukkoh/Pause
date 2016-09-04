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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
public abstract class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();

    protected String mUserEncodedEmail;
    protected DatabaseReference mMainDatabaseRef;
    protected DatabaseReference mCurrentDatabaseRef;

    protected RecyclerView mRecyclerView;
    protected FirebaseRecyclerAdapter mCurrentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserEncodedEmail = getArguments().getString(Constants.ARG_USER_ENCODED_EMAIL);
        mMainDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mCurrentDatabaseRef = setUpCurrentDatabaseRef();
    }

    protected abstract DatabaseReference setUpCurrentDatabaseRef();

    protected abstract void setUpUI(View view);

    /**
     *
     * @param view View that will get inflated
     * @param recyclerViewId ID for recycler view that will get inflated
     * @param layoutManager layout manager for either linear layout or gridlayout
     */
    protected void setUpRecyclerView(View view, int recyclerViewId,RecyclerView.LayoutManager layoutManager) {
        mRecyclerView = (RecyclerView) view.findViewById(recyclerViewId);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);

        mCurrentAdapter = createRecyclerAdapter();
        mRecyclerView.setAdapter(mCurrentAdapter);
    }

    /**
     * @return Custom FirebaseRecyclerAdapter
     */
    protected abstract FirebaseRecyclerAdapter createRecyclerAdapter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCurrentAdapter.cleanup();
    }
}
