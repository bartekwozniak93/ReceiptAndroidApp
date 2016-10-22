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
import pl.wozniakbartlomiej.receipt.Services.IServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ReceiptServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;

/**
 * Created by Bartek on 22/10/16.
 */
public class UsersForEditReceiptFragment extends android.app.Fragment implements IServiceHelper {

    private ReceiptServiceHelper asyncTask;
    private ListView listView;
    private ArrayList<User> usersList;
    private String receiptId;
    static UsersForEditReceiptFragment usersFragmentForEditReceipt;

    public UsersForEditReceiptFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_for_edit_receipt, container, false);
        getArgumentsFromActivity();
        assignViewElements(view);
        initUsersList();
        getUsersForEvent();
        usersFragmentForEditReceipt = this;
        return view;
    }

    public static UsersForEditReceiptFragment getInstance() {
        return usersFragmentForEditReceipt;
    }

    /**
     * Get arguments from Activity.
     */
    private void getArgumentsFromActivity() {
        receiptId = getArguments().getString("receiptId");
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
        asyncTask = new ReceiptServiceHelper(getActivity().getApplicationContext());
        asyncTask.delegate = this;
        asyncTask.execute(ServiceHelper.POST_METHOD, ServiceHelper.getReceiptString(), "", "", "", "", receiptId);
    }

    /**
     * Handle callbck for async method to get user's events.
     */
    @Override
    public void userServiceProcess(String result) {
        extractJson(result);
        listView.setAdapter(new UsersForReceiptAdapter(getActivity().getApplicationContext(), usersList));
    }

    /**
     * Extract event JSON to get title and description.
     */
    private void extractJson(String result) {
        usersList.clear();
        JSONObject resultObject = null;
        JSONObject eventObject = null;
        JSONArray usersObject=null;
        JSONArray eventUsers = null;
        try {
            resultObject = new JSONObject(result);
            usersObject = resultObject.getJSONArray("users");
            for (int i = 0; i < usersObject.length(); i++) {
                JSONObject objects = usersObject.getJSONObject(i);
                String id = objects.get("_id").toString();
                JSONObject local = objects.getJSONObject("local");
                String email = local.get("email").toString();
                addUserToList(id, email, false);
            }

            eventObject = resultObject.getJSONObject("receipt");
            eventUsers = eventObject.getJSONArray("users");
            for (int i = 0; i < eventUsers.length(); i++) {
                JSONObject objects = eventUsers.getJSONObject(i);
                String id = objects.get("_id").toString();
                setCheckedUser(id);
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

    private void setCheckedUser(String id){
        for (int i=0; i< usersList.size(); i++){
            if(usersList.get(i).getId().equals(id)) {
                usersList.get(i).setIsChecked(true);
            }
        }
    }

    public HashMap<String, Boolean> getCheckedUsers() {
        HashMap<String, Boolean> map = new HashMap<>();
        for (int i = 0; i < usersList.size(); i++) {
            if(usersList.get(i).getIsChecked())
                map.put(usersList.get(i).getId(), usersList.get(i).getIsChecked());
        }
        return map;
    }
}
