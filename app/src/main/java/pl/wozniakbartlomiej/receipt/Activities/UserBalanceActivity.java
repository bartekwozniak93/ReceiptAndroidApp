package pl.wozniakbartlomiej.receipt.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pl.wozniakbartlomiej.receipt.Fragments.UserBalanceFragment;
import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.UserSessionManager;

/**
 * Balance Activity for user's balance
 */
public class UserBalanceActivity extends AppCompatActivity {

    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_balance);
        initSession();
        addUserBalanceFragment();
    }

    /**
     * Init session for checking users permissions.
     */
    private void initSession(){
        session = new UserSessionManager(getApplicationContext());
        session.checkLogin();
    }
    /**
     * Go to Add Event Activity
     */
    public void onClick_GoToAddEventActivity(View view){
        Intent i = new Intent(this, AddEventActivity.class);
        startActivity(i);
    }

    /**
     * Go to Events Activity
     */
    public void onClick_GoToMainActivity(View view){
        Intent i = new Intent(this, UserEventsActivity.class);
        startActivity(i);
    }

    /**
     * Go to Add User Activity
     */
    public void onClick_GoToUserActivity(View view){
        Intent i = new Intent(this, UserActivity.class);
        startActivity(i);
    }

    /**
     * Check if there's already User Balance Fragment added.
     * If not, add.
     */
    private void addUserBalanceFragment() {
        if (findViewById(R.id.frameUserBalanceFragment) != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment userBalanceFragment = new UserBalanceFragment();
            fragmentTransaction.add(R.id.frameUserBalanceFragment, userBalanceFragment);
            fragmentTransaction.commit();
        }
    }

}
