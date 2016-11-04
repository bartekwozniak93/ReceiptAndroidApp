package pl.wozniakbartlomiej.receipt.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.wozniakbartlomiej.receipt.Adapters.EventsAdapter;
import pl.wozniakbartlomiej.receipt.Models.Event;
import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.EventServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.IServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.UserSessionManager;

public class UserEventsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, IServiceHelper {

    private UserSessionManager session;
    private GoogleMap mMap;
    private EventServiceHelper asyncTask;
    private ListView listView;
    private ArrayList<Event> eventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_events);
        assignViewElements();
        initEventsList();
        getUserEvents();
        initSession();
        initMap();
    }

    /**
     * Init session for checking users permissions.
     */
    private void initSession(){
        session = new UserSessionManager(getApplicationContext());
        session.checkLogin();
    }

    /**
     * Go to AddEvent Activity
     */
    public void onClick_GoToAddEventActivity(View view){
        Intent i = new Intent(this, AddEventActivity.class);
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
     * Assign view elements.
     */
    private void initEventsList() {
        eventsList = new ArrayList();
    }

    /**
     * Assign view elements.
     */
    private void assignViewElements() {
        listView = (ListView) findViewById(R.id.userEventsList);
    }

    /**
     * Call async method to get user's events.
     */
    private void getUserEvents() {
        asyncTask = new EventServiceHelper(getApplicationContext());
        asyncTask.delegate = this;
        asyncTask.execute(ServiceHelper.GET_METHOD, ServiceHelper.getUserEventsString(), "", "");
    }

    /**
     * Handle callbck for async method to get user's events.
     */
    @Override
    public void userServiceProcess(String result) {
        extractJson(result);
        listView.setAdapter(new EventsAdapter(getApplicationContext(), eventsList));
    }

    /**
     * Extract event JSON to get title and description.
     */
    private void extractJson(String result) {
        JSONObject resultObject = null;
        JSONArray events = null;
        String latitude="";
        String longitude="";
        try {
            resultObject = new JSONObject(result);
            events = resultObject.getJSONArray("events");
            for (int i = 0; i < events.length(); i++) {
                JSONObject objects = events.getJSONObject(i);
                String id = objects.get("_id").toString();
                String title = objects.getString("title");
                String description = objects.getString("description");
                latitude = objects.has("latitude") ? objects.getString("latitude") : "";
                longitude = objects.has("longitude") ? objects.getString("longitude") : "";
                addEventToList(id, title, description,latitude, longitude );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addMarkersToMap();
        moveCameraToLastAddedEvent(latitude, longitude);
    }

    private void addMarkersToMap(){
        for(int i=0; i< eventsList.size(); i++){
            String latitude=eventsList.get(i).getLatitude();
            String longitude=eventsList.get(i).getLongitude();
            if(!latitude.isEmpty() && !longitude.isEmpty()) {
                String id=eventsList.get(i).getId();
                String title=eventsList.get(i).getTitle();
                String description=eventsList.get(i).getDescription();
                double latitudeMap = Double.parseDouble(latitude);
                double longitudeMap = Double.parseDouble(longitude);
                LatLng location = new LatLng(latitudeMap, longitudeMap);
                Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(title.trim()).snippet(description.trim()));
                marker.setTag(id);
                marker.showInfoWindow();
            }
        }
    }

    private void moveCameraToLastAddedEvent(String latitude, String longitude){
        if(!latitude.isEmpty() && !longitude.isEmpty()) {
            double latitudeMap = Double.parseDouble(latitude);
            double longitudeMap = Double.parseDouble(longitude);
            LatLng location = new LatLng(latitudeMap, longitudeMap);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
        }
    }
    /**
     * Add event element to list.
     */
    private void addEventToList(String id, String title, String description, String latitude,String longitude) {
        Event event = new Event();
        event.setId(id);
        event.setTitle(title);
        event.setDescription(description);
        event.setLatitude(latitude);
        event.setLongitude(longitude);
        event.setImageId(R.drawable.events);
        eventsList.add(event);
    }


    private void initMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        String eventId = (String) marker.getTag();
        Event event = null;
        for(int i=0; i< eventsList.size(); i++){
            if(eventsList.get(i).getId().equals(eventId)){
                event = eventsList.get(i);
                break;
            }
        }
        if(event!=null) {
            Intent itent = new Intent(UserEventsActivity.this, EventBalanceActivity.class);
            itent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            itent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            itent.putExtra("eventId", event.getId());
            itent.putExtra("eventTitle", event.getTitle());
            itent.putExtra("eventDescription", event.getDescription());
            this.startActivity(itent);
        }
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

}


