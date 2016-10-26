package pl.wozniakbartlomiej.receipt.Widgets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pl.wozniakbartlomiej.receipt.Models.User;
import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.EventServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.IServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class UsersForNewReceiptFragment extends android.app.Fragment implements IServiceHelper {

    private EventServiceHelper asyncTask;
    private ListView listView;
    private ArrayList<User> usersList;
    private String eventId;
    private UsersForReceiptAdapter adapter;
    static UsersForNewReceiptFragment usersFragmentForNewReceipt;

    public UsersForNewReceiptFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_for_new_receipt, container, false);
        getArgumentsFromActivity();
        assignViewElements(view);
        initUsersList();
        getUsersForEvent();
        usersFragmentForNewReceipt = this;
        return view;
    }



    public static UsersForNewReceiptFragment getInstance() {
        return usersFragmentForNewReceipt;
    }

    /**
     * Get arguments from Activity.
     */
    private void getArgumentsFromActivity() {
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
        listView = (ListView) view.findViewById(R.id.usersForReceiptList);
    }

    /**
     * Call async method to get users for event.
     */
    public void getUsersForEvent() {
        asyncTask = new EventServiceHelper(getActivity().getApplicationContext());
        asyncTask.delegate = this;
        asyncTask.execute(ServiceHelper.POST_METHOD, ServiceHelper.getEventString(), "", "", eventId);
    }

    /**
     * Handle callbck for async method to get user's events.
     */
    @Override
    public void userServiceProcess(String result) {
        extractJson(result);
        adapter= new UsersForReceiptAdapter(getActivity().getApplicationContext(), usersList);
        listView.setAdapter(adapter);
    }

    /**
     * Extract event JSON to get title and description.
     */
    private void extractJson(String result) {
        usersList.clear();
        JSONObject resultObject = null;
        JSONObject eventObject = null;
        JSONArray eventUsers = null;
        try {
            resultObject = new JSONObject(result);
            eventObject = resultObject.getJSONArray("event").getJSONObject(0);
            eventUsers = eventObject.getJSONArray("users");
            for (int i = 0; i < eventUsers.length(); i++) {
                JSONObject objects = eventUsers.getJSONObject(i);
                String id = objects.get("_id").toString();
                JSONObject a = objects.getJSONObject("local");
                String email = a.get("email").toString();
                //String email = objects.getString("email");
                addUserToList(id, email, true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add user to list.
     */
    private void addUserToList(String id, String email, boolean isChecked) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setIsChecked(isChecked);
        usersList.add(user);
    }

    public HashMap<String, Boolean> getCheckedUsers(){
        HashMap<String, Boolean> map = new HashMap<>();
        for (int i = 0; i < usersList.size(); i++)
        {
            if(usersList.get(i).getIsChecked())
                map.put(usersList.get(i).getId(), usersList.get(i).getIsChecked());
        }
        return map;
    }

    public int getNumberOfCheckedUsers(){
        int nCheckedUsers=0;
        for (int i = 0; i < usersList.size(); i++)
        {
            if(usersList.get(i).getIsChecked())
                nCheckedUsers++;
        }
        return nCheckedUsers;
    }


}
