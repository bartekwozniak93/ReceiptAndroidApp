package pl.wozniakbartlomiej.receipt.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.UserSessionManager;

public class MainActivity extends AppCompatActivity {

    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
     * Go to AddEvent Activity
     */
    public void onClick_GoToAddEventActivity(View view){
        Intent i = new Intent(this, AddEventActivity.class);
        startActivity(i);
    }

    /**
     * Go to Balance Activity
     */
    public void onClick_GoToBalanceActivity(View view){
        Intent i = new Intent(this, BalanceActivity.class);
        startActivity(i);
    }

    /**
     * Go to User Activity
     */
    public void onClick_GoToUserActivity(View view){
        Intent i = new Intent(this, UserActivity.class);
        startActivity(i);
    }

}
