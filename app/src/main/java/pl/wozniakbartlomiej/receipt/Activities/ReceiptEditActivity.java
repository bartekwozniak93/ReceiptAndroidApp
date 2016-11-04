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
import pl.wozniakbartlomiej.receipt.Fragments.UsersForEditReceiptFragment;

public class ReceiptEditActivity extends AppCompatActivity implements IServiceHelper {

    private UsersForNewReceiptServiceHelper asyncTask;
    private String receiptId;
    private String receiptTitle;
    private String receiptDescription;
    private String receiptTotal;
    private String eventId;
    private String eventTitle;
    private String eventDescription;
    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_edit);
        getExtrasFromIntent();
        addUsersForNewReceiptFragment();
        setValuesOnView();
        setTextToViewElements();
        initSession();
    }

    /**
     * Init session for checking users permissions.
     */
    private void initSession(){
        session = new UserSessionManager(getApplicationContext());
        session.checkLogin();
    }
    private void  setTextToViewElements(){
        TextView textView_Title = (TextView) findViewById(R.id.textView_Title);
        textView_Title.setText(eventTitle+" : "+receiptTitle);
    }

    /**
     * Get extras from Intent.
     */
    private void getExtrasFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            receiptId = getIntent().getExtras().getString("receiptId");
            receiptTitle = getIntent().getExtras().getString("receiptTitle");
            receiptDescription = getIntent().getExtras().getString("receiptDescription");
            receiptTotal = getIntent().getExtras().getString("receiptTotal");
            eventId = getIntent().getExtras().getString("eventId");
            eventTitle = getIntent().getExtras().getString("eventTitle");
            eventDescription = getIntent().getExtras().getString("eventDescription");
        }
    }



    public void onClick_EditReceipt(View view) {
        //Execute async method for add event.
        asyncTask = new UsersForNewReceiptServiceHelper(ReceiptEditActivity.this);
        asyncTask.delegate = this;
        asyncTask.setProcessDialog(getApplicationContext().getString(R.string.progress_dialog_header));
        asyncTask.execute(ServiceHelper.POST_METHOD, ServiceHelper.getEditReceiptString(), getTitleFromView(), getDescriptionFromView(), "", getTotalFromView(), receiptId);
    }

    /**
     * Callback for add event method.
     */
    @Override
    public void userServiceProcess(String result) {
        try {

            //Redirect to ReceiptEditActivity
            Intent i = new Intent(getApplicationContext(), EventReceiptsActivity.class);
            i.putExtra("eventId",eventId);
            i.putExtra("eventTitle", eventTitle);
            i.putExtra("eventDescription", eventDescription);
            startActivity(i);
            finish();
        } catch (Exception e) {
            Log.d("ReceiptEditActivity", e.getMessage());
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
        if (findViewById(R.id.frameUsersForEditReceipt) != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment usersFragmentForNewReceipt = new UsersForEditReceiptFragment();
            Bundle bundle = new Bundle();
            bundle.putString("receiptId", receiptId);
            usersFragmentForNewReceipt.setArguments(bundle);
            fragmentTransaction.add(R.id.frameUsersForEditReceipt, usersFragmentForNewReceipt);
            fragmentTransaction.commit();
        }
    }


    private void setValuesOnView() {
        setTitleFromView();
        setDescriptionFromView();
        setTotalFromView();
    }

    /**
     * Set Title In View
     */
    private void setTitleFromView() {
        //Retrieve string for Title.
        EditText editText_Title = (EditText) findViewById(R.id.editText_Title);
        editText_Title.setText(receiptTitle);
    }

    /**
     * Set Description In View
     */
    private void setDescriptionFromView() {
        //Retrieve string for Description.
        EditText editText_Description = (EditText) findViewById(R.id.editText_Description);
        editText_Description.setText(receiptDescription);
    }

    /**
     * Set Total In View
     */
    private void setTotalFromView() {
        //Retrieve string for total.
        EditText editText_Total = (EditText) findViewById(R.id.editText_Total);
        editText_Total.setText(receiptTotal);
    }


    public void onClick_GoToEventReceiptsActivity(View v){
        Intent i = new Intent(getApplicationContext(), EventReceiptsActivity.class);
        i.putExtra("eventId",eventId);
        i.putExtra("eventTitle", eventTitle);
        i.putExtra("eventDescription", eventDescription);
        startActivity(i);
        finish();
    }


}
