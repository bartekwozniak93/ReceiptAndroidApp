package pl.wozniakbartlomiej.receipt.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.IServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.UserSessionManager;
import pl.wozniakbartlomiej.receipt.Services.UsersForNewReceiptServiceHelper;
import pl.wozniakbartlomiej.receipt.Fragments.UsersForNewReceiptFragment;

/**
 * Activity to add Receipt to Event.
 */
public class AddReceiptActivity extends AppCompatActivity implements IServiceHelper {

    private UsersForNewReceiptServiceHelper asyncTask;
    private String eventId;
    private String eventTitle;
    private String eventDescription;
    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt);
        getExtrasFromIntent();
        addUsersForNewReceiptFragment();
        setTitleOnView();
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
     * Set Title on View.
     */
    private void setTitleOnView() {
        TextView textView_Title = (TextView) findViewById(R.id.textView_Title);
        textView_Title.setText(eventTitle);
    }

    /**
     * Execute async method for add event.
     */
    public void onClick_AddReceipt(View view) {
        asyncTask = new UsersForNewReceiptServiceHelper(AddReceiptActivity.this);
        asyncTask.delegate = this;
        asyncTask.setProcessDialog(getApplicationContext().getString(R.string.progress_dialog_header));
        asyncTask.execute(ServiceHelper.POST_METHOD, ServiceHelper.getNewReceiptString(), getTitleFromView(), getDescriptionFromView(), eventId, getTotalFromView());
    }

    /**
     * Callback for add event method.
     */
    @Override
    public void userServiceProcess(String result) {
        session.checkResult(result);
        try {
            //Redirect to EventsActivity
            Intent i = new Intent(getApplicationContext(), EventBalanceActivity.class);
            i.putExtra("eventId", eventId);
            i.putExtra("eventTitle", eventTitle);
            i.putExtra("eventDescription", eventDescription);
            startActivity(i);
            finish();
        } catch (Exception e) {
            Log.d("RegisterActivity", e.getMessage());
        }
    }

    /**
     * Get Title From View
     */
    private String getTitleFromView() {
        //Retrieve string for Title.
        EditText editText_Title = (EditText) findViewById(R.id.editText_Title);
        String title = editText_Title.getText().toString().toLowerCase();
        return title;
    }

    /**
     * Get Description From View
     */
    private String getDescriptionFromView() {
        //Retrieve string for Description.
        EditText editText_Description = (EditText) findViewById(R.id.editText_Description);
        String description = editText_Description.getText().toString().toLowerCase();
        return description;
    }

    /**
     * Get Total From View
     */
    private String getTotalFromView() {
        //Retrieve string for total.
        EditText editText_Total = (EditText) findViewById(R.id.editText_Total);
        String total = editText_Total.getText().toString();
        return total;
    }

    /**
     * Check if there's already UsersForNewReceiptFragment added.
     * If not, add.
     */
    private void addUsersForNewReceiptFragment() {
        if (findViewById(R.id.frameUsersForNewReceipt) != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment usersFragmentForNewReceipt = new UsersForNewReceiptFragment();
            Bundle bundle = new Bundle();
            bundle.putString("eventId", eventId);
            bundle.putString("eventTitle", eventTitle);
            bundle.putString("eventDescription", eventDescription);
            usersFragmentForNewReceipt.setArguments(bundle);
            fragmentTransaction.add(R.id.frameUsersForNewReceipt, usersFragmentForNewReceipt);
            fragmentTransaction.commit();
        }
    }

    /**
     * Go to Main Activity
     */
    public void onClick_GoToMainActivity(View view) {
        Intent i = new Intent(this, UserEventsActivity.class);
        startActivity(i);
    }

    /**
     * Go to Add User Activity
     */
    public void onClick_GoToAddUser(View view) {
        Intent i = new Intent(this, AddUserActivity.class);
        i.putExtra("eventId", eventId);
        i.putExtra("eventTitle", eventTitle);
        i.putExtra("eventDescription", eventDescription);
        startActivity(i);
    }

    /**
     * Go to Event Activity
     */
    public void onClick_GoToEventActivity(View view) {
        Intent i = new Intent(this, EventBalanceActivity.class);
        i.putExtra("eventId", eventId);
        i.putExtra("eventTitle", eventTitle);
        i.putExtra("eventDescription", eventDescription);
        startActivity(i);
    }

}

