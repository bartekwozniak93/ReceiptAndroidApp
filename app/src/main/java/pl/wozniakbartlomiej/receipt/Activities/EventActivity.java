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
import pl.wozniakbartlomiej.receipt.Widgets.AutoCompleteFragment;
import pl.wozniakbartlomiej.receipt.Widgets.UsersFragment;

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
        addFragmentsToView();
    }

    /**
     * Add fragments to View.
     */
    private void addFragmentsToView(){
        addAddedUsersFragment();
        addAutoCompleteFragment();
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
     * to see all receipts for Event
     */
    public void onClick_GetReceipts(View view){
        Intent i = new Intent(this, ReceiptsActivity.class);
        i.putExtra("eventId",eventId);
        startActivity(i);
    }

    /**
     * Check if there's already UsersFragment added.
     * If not, add.
     */
    private void addAddedUsersFragment(){
        if (findViewById(R.id.frameAddedUsersLayout) != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment usersFragment = new UsersFragment();
            Bundle bundle = new Bundle();
            bundle.putString("eventId", eventId);
            usersFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.frameAddedUsersLayout, usersFragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * Check if there's already AutoCompleteFragment added.
     * If not, add.
     */
    private void addAutoCompleteFragment(){
        if (findViewById(R.id.frameAutoCompleteLayout) != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment autoCompleteFragment = new AutoCompleteFragment();
            Bundle bundle = new Bundle();
            bundle.putString("eventId", eventId);
            autoCompleteFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.frameAutoCompleteLayout, autoCompleteFragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * Get event's id, title and description from Intent.
     */
    private void getExtrasFromIntent(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventId = extras.getString("id");
            eventTitle = extras.getString("title");
            eventDescription = extras.getString("description");
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


}
