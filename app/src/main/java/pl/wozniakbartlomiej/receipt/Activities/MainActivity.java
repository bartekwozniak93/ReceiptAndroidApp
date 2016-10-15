package pl.wozniakbartlomiej.receipt.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.SessionManager;

public class MainActivity extends AppCompatActivity {

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSession();
    }

    public void onClick_Camera(View view){
        Intent i = new Intent(this, CameraActivity.class);
        startActivity(i);
    }

    public void onClick_NewEvent(View view){
        Intent i = new Intent(this, AddEventActivity.class);
        startActivity(i);
    }

    public void onClick_GetEvents(View view){
        Intent i = new Intent(this, EventsActivity.class);
        startActivity(i);
    }

    /**
     * Init session for checking users permissions.
     */
    private void initSession(){
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
    }
}
