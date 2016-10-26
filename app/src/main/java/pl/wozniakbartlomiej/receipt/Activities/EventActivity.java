package pl.wozniakbartlomiej.receipt.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.wozniakbartlomiej.receipt.Models.Receipt;
import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.IServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ReceiptServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.UserSessionManager;
import pl.wozniakbartlomiej.receipt.Widgets.ReceiptsAdapter;

/**
 * Activity to show all receipts of Event
 */
public class EventActivity extends AppCompatActivity implements IServiceHelper{

    private String eventId;
    private String eventTitle;
    private String eventDescription;
    private TextView textView_Title;
    private ReceiptServiceHelper asyncTask;
    private ListView listView;
    private ArrayList receiptsList;
    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getExtrasFromIntent();
        assignViewElements();
        setTextToViewElements();
        initReceiptsList();
        getReceipts();
        initSession();
    }

    /**
     * Init session for checking users permissions.
     */
    private void initSession(){
        session = new UserSessionManager(getApplicationContext());
        session.checkLogin();
    }
    /**
     * Assign view elements.
     */
    private void initReceiptsList() {
        receiptsList = new ArrayList();
    }


    /**
     * Call async method to get receipts for event.
     */
    public void getReceipts() {
        asyncTask = new ReceiptServiceHelper(EventActivity.this);
        asyncTask.delegate = this;
        asyncTask.execute(ServiceHelper.POST_METHOD, ServiceHelper.getReceiptsString(), "", "",eventId);
    }

    /**
     * Handle callbck for async method to get receipts for event
     */
    @Override
    public void userServiceProcess(String result) {
        extractJson(result);
        listView.setAdapter(new ReceiptsAdapter(EventActivity.this, receiptsList));
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
                addReceiptToList(id, title, description,total, eventId, eventTitle, eventDescription);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add receipt element to list for Adapter
     */
    private void addReceiptToList(String id, String title, String description, String total, String eventId, String eventTitle, String eventDescription) {
        Receipt receipt = new Receipt();
        receipt.setId(id);
        receipt.setTitle(title);
        receipt.setDescription(description);
        receipt.setTotal(total);
        receipt.setEventId(eventId);
        receipt.setEventTitle(eventTitle);
        receipt.setEventDescription(eventDescription);
        receipt.setImageId(R.drawable.receipt);
        receiptsList.add(receipt);
    }

    /**
     * Get extras from Intent.
     */
    private void getExtrasFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventId = getIntent().getExtras().getString("eventId");
            eventTitle = getIntent().getExtras().getString("eventTitle");
            eventDescription = getIntent().getExtras().getString("eventDescription");
        }
    }

    /**
     * Assign view elements.
     */
    private void assignViewElements() {
        listView = (ListView) findViewById(R.id.eventReceiptsList);
        textView_Title = (TextView) findViewById(R.id.textView_Title);
    }

    /**
     * Set text (title, description) to view elements.
     */
    private void  setTextToViewElements(){
        textView_Title.setText(eventTitle);
    }


    /**
     * Go to Main Activity
     */
    public void onClick_GoToMainActivity(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    /**
     * Go to view
     * to add Receipt.
     */
    public void onClick_GoToAddReceiptActivity(View view){
        Intent i = new Intent(this, AddReceiptActivity.class);
        i.putExtra("eventId",eventId);
        i.putExtra("eventTitle", eventTitle);
        i.putExtra("eventDescription", eventDescription);
        startActivity(i);
    }

    /**
     * Go to view
     * to add UserActivity.
     */
    public void onClick_GoToAddUser(View view){
        Intent i = new Intent(this, AddUserActivity.class);
        i.putExtra("eventId",eventId);
        i.putExtra("eventTitle", eventTitle);
        i.putExtra("eventDescription", eventDescription);
        startActivity(i);
    }

}
