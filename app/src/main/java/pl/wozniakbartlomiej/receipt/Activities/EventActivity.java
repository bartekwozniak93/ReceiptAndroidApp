package pl.wozniakbartlomiej.receipt.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Widgets.ReceiptsFragment;

public class EventActivity extends AppCompatActivity {

    private String eventId;
    private String eventTitle;
    private String eventDescription;
    private TextView textView_Title;
    private TextView textView_Description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getExtrasFromIntent();
        assignViewElements();
        setTextToViewElements();
        addReceiptsFragment();
    }


    /**
     * Check if there's already ReceiptsFragment added.
     * If not, add.
     */
    private void addReceiptsFragment(){
        if (findViewById(R.id.frameReceiptsLayout) != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment receiptsFragment = new ReceiptsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("eventId", eventId);
            receiptsFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.frameReceiptsLayout, receiptsFragment);
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
        textView_Description = (TextView) findViewById(R.id.textView_Description);
    }

    /**
     * Set text (title, description) to view elements.
     */
    private void  setTextToViewElements(){
        textView_Title.setText(eventTitle);
        textView_Description.setText(eventDescription);
    }




    /**
     * Go to Event section
     */
    public void onClick_BackToEvents(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    /**
     * Go to view
     * to add Receipt.
     */
    public void onClick_AddReceipt(View view){
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
    public void onClick_AddUser(View view){
        Intent i = new Intent(this, AddUserActivity.class);
        i.putExtra("eventId",eventId);
        i.putExtra("eventTitle", eventTitle);
        i.putExtra("eventDescription", eventDescription);
        startActivity(i);
    }



}
