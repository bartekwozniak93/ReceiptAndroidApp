package pl.wozniakbartlomiej.receipt.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import pl.wozniakbartlomiej.receipt.Adapters.UserBalanceAdapter;
import pl.wozniakbartlomiej.receipt.Models.UserBalance;
import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.EventServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.IServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserBalanceFragment extends Fragment implements IServiceHelper {

    private EventServiceHelper asyncTask;
    private ListView listView;
    private ArrayList userBalanceList;
    private Context context;
    public UserBalanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_balance, container, false);
        context = container.getContext();
        assignViewElements(view);
        initEventsList();
        getUserBalance();
        return view;
    }

    /**
     * Assign view elements.
     */
    private void initEventsList() {
        userBalanceList = new ArrayList();
    }

    /**
     * Assign view elements.
     */
    private void assignViewElements(View view) {
        listView = (ListView) view.findViewById(R.id.userBalanceList);
    }

    /**
     * Get User Balance
     */
    public void getUserBalance(){
        //Execute async method for login.
        asyncTask = new EventServiceHelper(context);
        asyncTask.delegate = this;
        asyncTask.execute(ServiceHelper.POST_METHOD, ServiceHelper.getUserBalanceString());
    }

    /**
     * Handle callbck for async method to get user's events.
     */
    @Override
    public void userServiceProcess(String result) {
        userBalanceList = new ArrayList();
        extractJson(result);
        listView.setAdapter(new UserBalanceAdapter(context, userBalanceList));
    }

    /**
     * Extract event JSON to get title and description.
     */
    private void extractJson(String result) {
        JSONObject resultObject = null;
        try {
            resultObject = new JSONObject(result);
            Iterator<?> keys = resultObject.keys();

            while( keys.hasNext() ) {
                String userKey = (String)keys.next();
                JSONObject user=(JSONObject) resultObject.get(userKey);
                    String eventTitle = (String) user.getString("eventTitle");
                    String eventDescription = (String) user.getString("eventDescription");
                    String balance =(String) user.getString("balance");
                    addUserBalanceToList(eventTitle,eventDescription, balance);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add cost element to list.
     */
    private void addUserBalanceToList(String eventTitle, String eventDescription, String balance) {
        double value=Double.parseDouble(balance);
            UserBalance cost = new UserBalance();
            cost.setEventTitle(eventTitle);
            cost.setEventDescription(eventDescription);
            cost.setBalance(new DecimalFormat("##.##").format(value));
            userBalanceList.add(cost);
    }
}
