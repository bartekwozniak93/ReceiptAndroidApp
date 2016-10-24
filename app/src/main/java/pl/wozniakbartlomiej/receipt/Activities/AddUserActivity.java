package pl.wozniakbartlomiej.receipt.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Widgets.AutoCompleteFragment;
import pl.wozniakbartlomiej.receipt.Widgets.UsersFragment;

public class AddUserActivity extends AppCompatActivity {

    private String eventId;
    private String eventTitle;
    private String eventDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        getExtrasFromIntent();
        addFragmentsToView();
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

    public void onClick_BackToEvents(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void onClick_AddReceipt(View view){
        Intent i = new Intent(this, AddReceiptActivity.class);
        i.putExtra("eventId",eventId);
        i.putExtra("eventTitle", eventTitle);
        i.putExtra("eventDescription", eventDescription);
        startActivity(i);
    }

    public void onClick_EventActivity(View view){
        Intent i = new Intent(this, EventActivity.class);
        i.putExtra("eventId",eventId);
        i.putExtra("eventTitle", eventTitle);
        i.putExtra("eventDescription", eventDescription);
        startActivity(i);
    }

    /**
     * Add fragments to View.
     */
    private void addFragmentsToView(){
        addAddedUsersFragment();
        addAutoCompleteFragment();
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

}
