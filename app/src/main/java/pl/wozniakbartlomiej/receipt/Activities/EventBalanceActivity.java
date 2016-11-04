package pl.wozniakbartlomiej.receipt.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import pl.wozniakbartlomiej.receipt.Fragments.EventCostsFragment;
import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.UserSessionManager;

/**
 * Activity to show all receipts of Event
 */
public class EventBalanceActivity extends AppCompatActivity{

    private String eventId;
    private String eventTitle;
    private String eventDescription;
    private TextView textView_Title;
    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_balance);
        getExtrasFromIntent();
        assignViewElements();
        setTextToViewElements();
        initSession();
        addEventCostsFragment();
    }

    /**
     * Init session for checking users permissions.
     */
    private void initSession(){
        session = new UserSessionManager(getApplicationContext());
        session.checkLogin();
    }

    /**
     * Check if there's already EventCostsFragment added.
     * If not, add.
     */
    private void addEventCostsFragment() {
        if (findViewById(R.id.frameEventCostsFragment) != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment eventCostsFragment = new EventCostsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("eventId", eventId);
            eventCostsFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.frameEventCostsFragment, eventCostsFragment);
            fragmentTransaction.commit();
        }
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
        Intent i = new Intent(this, UserEventsActivity.class);
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

    /**
     * Go to view
     * to get QRCode
     */
    public void onClick_getQrCode(View view){
        Intent i = new Intent(this, EventQrCodeActivity.class);
        i.putExtra("eventId",eventId);
        i.putExtra("eventTitle", eventTitle);
        i.putExtra("eventDescription", eventDescription);
        startActivity(i);
    }

    /**
     * Go to view
     * to get event receipts
     */
    public void onClick_getReceipts(View view){
        Intent i = new Intent(this, EventReceiptsActivity.class);
        i.putExtra("eventId",eventId);
        i.putExtra("eventTitle", eventTitle);
        i.putExtra("eventDescription", eventDescription);
        startActivity(i);
    }

}
