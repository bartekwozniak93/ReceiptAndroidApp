package pl.wozniakbartlomiej.receipt.Widgets;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.wozniakbartlomiej.receipt.Models.Event;
import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.EventServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.IServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;

/**
 * Created by Bartek on 16/10/16.
 * Fragment used for listing user's events.
 */
public class EventsFragment extends Fragment implements IServiceHelper {

    private EventServiceHelper asyncTask;
    private ListView listView;
    private ArrayList eventsList;

    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        assignViewElements(view);
        initEventsList();
        getUserEvents();
        return view;
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
    private void assignViewElements(View view) {
        listView = (ListView) view.findViewById(R.id.lvCustomList);
    }

    /**
     * Call async method to get user's events.
     */
    private void getUserEvents() {
        asyncTask = new EventServiceHelper(getActivity().getApplicationContext());
        asyncTask.delegate = this;
        asyncTask.execute(ServiceHelper.GET_METHOD, asyncTask.getUserEventsString(), "", "");
    }

    /**
     * Handle callbck for async method to get user's events.
     */
    @Override
    public void userServiceProcess(String result) {
        extractJson(result);
        listView.setAdapter(new EventsAdapter(getActivity().getApplicationContext(), eventsList));
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
                String id = objects.get("_id").toString();
                String title = objects.getString("title");
                String description = objects.getString("description");
                addEventToList(id, title, description);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add event element to list.
     */
    private void addEventToList(String id, String title, String description) {
        Event event = new Event();
        event.setId(id);
        event.setTitle(title);
        event.setDescription(description);
        event.setImageId(R.drawable.event_icon);
        eventsList.add(event);
    }
}
