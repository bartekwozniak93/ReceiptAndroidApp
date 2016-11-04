package pl.wozniakbartlomiej.receipt.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.EventServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.IServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.UserSessionManager;

import static android.content.ContentValues.TAG;

/**
 * Activity to add Event.
 */
public class AddEventActivity extends AppCompatActivity implements IServiceHelper, com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private EventServiceHelper asyncTask;
    private UserSessionManager session;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        initSession();
        initLocation();
    }

    /**
     * Init session for checking users permissions.
     */
    private void initSession(){
        session = new UserSessionManager(getApplicationContext());
        session.checkLogin();
    }
    /**
     * Execute async method for add event.
     */
    public void onClick_AddEvent(View view) {
        String longitude= String.valueOf( mCurrentLocation.getLongitude());
        String latitude= String.valueOf( mCurrentLocation.getLatitude());
        asyncTask =new EventServiceHelper(AddEventActivity.this);
        asyncTask.delegate = this;
        asyncTask.setProcessDialog(getApplicationContext().getString(R.string.progress_dialog_header));
        asyncTask.execute(ServiceHelper.POST_METHOD, ServiceHelper.getPostEventString(), getTitleFromView(), getDescriptionFromView(),"","",longitude, latitude);
    }

    /**
     * Callback for add event method.
     */
    @Override
    public void userServiceProcess(String result) {
        session.checkResult(result);
        try {
            Intent i = new Intent(getApplicationContext(), UserEventsActivity.class);
            startActivity(i);
            finish();
        }
        catch(Exception e){
            Log.d("RegisterActivity", e.getMessage());
        }
    }


    private void initLocation(){{
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(12000);
        mLocationRequest.setFastestInterval(6000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();}
    }
    /**
     * Get Title From View
     */
    private String getTitleFromView()
    {
        //Retrieve string for email.
        EditText editText_Title = (EditText) findViewById(R.id.editText_Title);
        String title = editText_Title.getText().toString().toLowerCase();
        return title;
    }

    /**
     * Get Description From View
     */
    private String getDescriptionFromView() {
        //Retrieve string for password.
        EditText editText_Description = (EditText) findViewById(R.id.editText_Description);
        String description = editText_Description.getText().toString().toLowerCase();
        return description;
    }

    /**
     * Go to Events Activity
     */
    public void onClick_GoToMainActivity(View view){
        Intent i = new Intent(this, UserEventsActivity.class);
        startActivity(i);
    }

    /**
     * Go to Balance Activity
     */
    public void onClick_GoToBalanceActivity(View view){
        Intent i = new Intent(this, UserBalanceActivity.class);
        startActivity(i);
    }

    /**
     * Go to User Activity
     */
    public void onClick_GoToUserActivity(View view){
        Intent i = new Intent(this, UserActivity.class);
        startActivity(i);
    }

    /**
     * Go to QR From Camera Activity
     */
    public void onClick_GoToQRFromCameraActivity(View view){
        Intent i = new Intent(this, EventFromQrCodeActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mCurrentLocation = location;
        }
    }
}

