package pl.wozniakbartlomiej.receipt.Fragments;


import android.content.Context;
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

import pl.wozniakbartlomiej.receipt.Adapters.UsersAdapter;
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
    static  UsersFragment usersFragment;
    private Context context;

    public UsersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_users, container, false);
        context = container.getContext();
        getArgumentsFromActivity();
        assignViewElements(view);
        initUsersList();
        getUsersForEvent();
        usersFragment=this;
        return view;
    }

    public static UsersFragment getInstance(){
        return usersFragment;
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
    public void getUsersForEvent() {
        asyncTask = new EventServiceHelper(context);
        asyncTask.delegate = this;
        asyncTask.execute(ServiceHelper.POST_METHOD, ServiceHelper.getEventString(), "", "", eventId);
    }

    /**
     * Handle callbck for async method to get user's events.
     */
    @Override
    public void userServiceProcess(String result) {
        extractJson(result);
        listView.setAdapter(new UsersAdapter(context, usersList));
    }

    /**
     * Extract event JSON to get title and description.
     */
    private void extractJson(String result) {
        usersList.clear();
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
                addUserToList(id, email);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add user to list.
     */
    private void addUserToList(String id, String email) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        usersList.add(user);
    }
}