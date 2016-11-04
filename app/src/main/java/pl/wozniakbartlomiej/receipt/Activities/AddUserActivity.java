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
import pl.wozniakbartlomiej.receipt.Services.UserSessionManager;
import pl.wozniakbartlomiej.receipt.Fragments.AutoCompleteFragment;
import pl.wozniakbartlomiej.receipt.Fragments.UsersFragment;

/**
 * Activity to add User to Event.
 */
public class AddUserActivity extends AppCompatActivity {

    private String eventId;
    private String eventTitle;
    private String eventDescription;
    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        getExtrasFromIntent();
        addFragmentsToView();
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
     * Go to Add Receipt Activity.
     */
    public void onClick_GoToAddReceiptActivity(View view) {
        Intent i = new Intent(this, AddReceiptActivity.class);
        i.putExtra("eventId", eventId);
        i.putExtra("eventTitle", eventTitle);
        i.putExtra("eventDescription", eventDescription);
        startActivity(i);
    }

    /**
     * Go to Event Activity.
     */
    public void onClick_GoToEventActivity(View view) {
        Intent i = new Intent(this, EventBalanceActivity.class);
        i.putExtra("eventId", eventId);
        i.putExtra("eventTitle", eventTitle);
        i.putExtra("eventDescription", eventDescription);
        startActivity(i);
    }

    /**
     * Add fragments to View.
     */
    private void addFragmentsToView() {
        addAddedUsersFragment();
        addAutoCompleteFragment();
    }

    /**
     * Check if there's already UsersFragment added.
     * If not, add.
     */
    private void addAddedUsersFragment() {
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
    private void addAutoCompleteFragment() {
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
