package pl.wozniakbartlomiej.receipt.Widgets;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.wozniakbartlomiej.receipt.Models.User;
import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.EventServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.IServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment implements IServiceHelper {

    private EventServiceHelper asyncTask;
    private ListView listView;
    private ArrayList usersList;
    private String eventId;


    public UsersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        getArgumentsFromActivity();
        assignViewElements(view);
        initUsersList();
        getUsersForEvent();
        return view;
    }

    /**
     * Get arguments from Activity.
     */
    private void getArgumentsFromActivity(){
        eventId = getArguments().getString("eventId");
    }

    /**
     * Assign view elements.
     */
    private void initUsersList() {
        usersList = new ArrayList();
    }

    /**
     * Assign view elements.
     */
    private void assignViewElements(View view) {
        listView = (ListView) view.findViewById(R.id.eventUsersList);
    }

    /**
     * Call async method to get users for event.
     */
    private void getUsersForEvent() {
        asyncTask = new EventServiceHelper(getActivity().getApplicationContext());
        asyncTask.delegate = this;
        asyncTask.execute(ServiceHelper.POST_METHOD, asyncTask.getEventString(), "", "", eventId);
    }

    /**
     * Handle callbck for async method to get user's events.
     */
    @Override
    public void userServiceProcess(String result) {
        extractJson(result);
        listView.setAdapter(new UsersAdapter(getActivity().getApplicationContext(), usersList));
    }

    /**
     * Extract event JSON to get title and description.
     */
    private void extractJson(String result) {
        JSONObject resultObject = null;
        JSONObject eventObject = null;
        JSONArray eventUsers= null;
        try {
            resultObject = new JSONObject(result);
            eventObject = resultObject.getJSONArray("event").getJSONObject(0);
            eventUsers = eventObject.getJSONArray("users");
            for (int i = 0; i < eventUsers.length(); i++) {
                JSONObject objects = eventUsers.getJSONObject(i);
                String id = objects.get("_id").toString();
                JSONObject a =objects.getJSONObject("local");
                String email = a.get("email").toString();
                //String email = objects.getString("email");
                addEventToList(id, email);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add user to list.
     */
    private void addEventToList(String id, String email) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        usersList.add(user);
    }
}
