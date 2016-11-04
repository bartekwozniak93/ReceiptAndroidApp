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

import pl.wozniakbartlomiej.receipt.Adapters.EventCostsAdapter;
import pl.wozniakbartlomiej.receipt.Models.Cost;
import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.EventServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.IServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventCostsFragment extends Fragment implements IServiceHelper {

    private EventServiceHelper asyncTask;
    private ListView listView;
    private ArrayList eventCostsList;
    private String eventId;
    Context context;

    public EventCostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_event_costs, container, false);
        assignViewElements(view);
        getArgumentsFromActivity();
        initEventsList();
        getEventCosts();
        return view;
    }

    /**
     * Assign view elements.
     */
    private void initEventsList() {
        eventCostsList = new ArrayList();
    }

    /**
     * Assign view elements.
     */
    private void assignViewElements(View view) {
        listView = (ListView) view.findViewById(R.id.userEventCostsList);
    }

    /**
     * Call async method to get user's events.
     */
    private void getEventCosts() {
        asyncTask = new EventServiceHelper(getActivity().getApplicationContext());
        asyncTask.delegate = this;
        asyncTask.execute(ServiceHelper.POST_METHOD, ServiceHelper.getEventBalanceString(), "", "",eventId);
    }

    /**
     * Handle callbck for async method to get user's events.
     */



    @Override
    public void userServiceProcess(String result) {
        try{
            extractJson(result);
            listView.setAdapter(new EventCostsAdapter(context, eventCostsList));
        } catch (Exception e){

        }
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
                JSONObject userBalance=(JSONObject) user.get("balance");
                    Iterator<?> userBalanceKeys = userBalance.keys();
                    while( userBalanceKeys.hasNext() ) {
                        String userForCost = (String) userBalanceKeys.next();
                        String valueForCost = userBalance.getString(userForCost);
                        addCostToList(userKey,userForCost, valueForCost);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add cost element to list.
     */
    private void addCostToList(String user, String userForCost, String valueForCost) {
        double value=Double.parseDouble(valueForCost);
        if(value>0) {
            Cost cost = new Cost();
            cost.setUser(user);
            cost.setUserForCost(userForCost);
            cost.setValueForCost(new DecimalFormat("##.##").format(value));
            eventCostsList.add(cost);
        }
    }

    /**
     * Get arguments from Activity.
     */
    private void getArgumentsFromActivity() {
        eventId = getArguments().getString("eventId");
    }
}
