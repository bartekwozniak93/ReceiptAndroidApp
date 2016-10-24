package pl.wozniakbartlomiej.receipt.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pl.wozniakbartlomiej.receipt.R;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

    public void btn_go_to_add_event_activity(View view){
        Intent i = new Intent(this, AddEventActivity.class);
        startActivity(i);
    }

    public void btn_go_to_events_activity(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void btn_go_to_balance_activity(View view){
        Intent i = new Intent(this, BalanceActivity.class);
        startActivity(i);
    }

}
