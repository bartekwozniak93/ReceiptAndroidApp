package pl.wozniakbartlomiej.receipt.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.EventServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.IUserServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;

public class EventsActivity extends AppCompatActivity implements IUserServiceHelper {

    private EventServiceHelper asyncTask;
    private LinearLayout layoutEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        assignViewElements();
        getUserEvents();
    }

    /**
     * Assign view elements.
     */
    private void assignViewElements() {
        layoutEvents = (LinearLayout) findViewById(R.id.layout_events);
    }

    /**
     * Call async method to get user's events.
     */
    private void getUserEvents() {
        asyncTask = new EventServiceHelper(EventsActivity.this);
        asyncTask.delegate = this;
        asyncTask.execute(ServiceHelper.GET_METHOD, asyncTask.getUserEventsString(), "", "");
    }

    /**
     * Handle callbck for async method to get user's events.
     */
    @Override
    public void userServiceProcess(String result) {
        extractJson(result);
    }

    /**
     * Extract event JSON to get title and description.
     */
    private void extractJson(String result) {
        JSONObject resultObject = null;
        JSONArray events = null;
        try {
            resultObject = new JSONObject(result);
            events = resultObject.getJSONArray("events");
            for (int i = 0; i < events.length(); i++) {
                JSONObject objects = events.getJSONObject(i);
                String text = objects.getString("title") + objects.getString("description");
                addEventToList(text);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add event element to list.
     */
    private void addEventToList(String text) {
        TextView textView = new TextView(getApplicationContext());
        textView.setText(text);
        layoutEvents.addView(textView);
    }
}


