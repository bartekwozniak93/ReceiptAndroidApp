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

import pl.wozniakbartlomiej.receipt.Models.Receipt;
import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.IServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ReceiptServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiptsFragment extends Fragment implements IServiceHelper {

    private ReceiptServiceHelper asyncTask;
    private ListView listView;
    private ArrayList receiptsList;
    private String eventId;

    public ReceiptsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipts, container, false);
        assignViewElements(view);
        getArgumentsFromActivity();
        initReceiptsList();
        getReceipts();
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
    private void initReceiptsList() {
        receiptsList = new ArrayList();
    }

    /**
     * Assign view elements.
     */
    private void assignViewElements(View view) {
        listView = (ListView) view.findViewById(R.id.eventReceiptsList);
    }

    /**
     * Call async method to get receipts for event.
     */
    public void getReceipts() {
        asyncTask = new ReceiptServiceHelper(getActivity().getApplicationContext());
        asyncTask.delegate = this;
        asyncTask.execute(ServiceHelper.POST_METHOD, ServiceHelper.getReceiptsString(), "", "",eventId);
    }

    /**
     * Handle callbck for async method to get receipts for event
     */
    @Override
    public void userServiceProcess(String result) {
        extractJson(result);
        listView.setAdapter(new ReceiptsAdapter(getActivity().getApplicationContext(), receiptsList));
    }

    /**
     * Extract receipts JSON to get title and description.
     */
    private void extractJson(String result) {
        JSONObject resultObject = null;
        JSONArray receipts = null;
        try {
            resultObject = new JSONObject(result);
            receipts = resultObject.getJSONArray("receipts");
            for (int i = 0; i < receipts.length(); i++) {
                JSONObject objects = receipts.getJSONObject(i);
                String id = objects.get("_id").toString();
                String title = objects.getString("title");
                String description = objects.getString("description");
                String total = objects.getString("total");
                String eventId = objects.getString("eventId");
                addReceipttToList(id, title, description,total, eventId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add receipt element to list.
     */
    private void addReceipttToList(String id, String title, String description, String total, String eventId) {
        Receipt receipt = new Receipt();
        receipt.setId(id);
        receipt.setTitle(title);
        receipt.setDescription(description);
        receipt.setTotal(total);
        receipt.setEventId(eventId);
        receipt.setImageId(R.drawable.event_icon);
        receiptsList.add(receipt);
    }
}
