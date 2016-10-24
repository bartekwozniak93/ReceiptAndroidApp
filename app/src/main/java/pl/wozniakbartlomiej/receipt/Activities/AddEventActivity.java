package pl.wozniakbartlomiej.receipt.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.EventServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.IServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;

public class AddEventActivity extends AppCompatActivity implements IServiceHelper {

    private EventServiceHelper asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    }

    public void onClick_AddEvent(View view) {
        //Execute async method for add event.
        asyncTask =new EventServiceHelper(AddEventActivity.this);
        asyncTask.delegate = this;
        asyncTask.setProcessDialog(getApplicationContext().getString(R.string.progress_dialog_header));
        asyncTask.execute(ServiceHelper.POST_METHOD, ServiceHelper.getPostEventString(), getTitleFromView(), getDescriptionFromView());
    }

    /**
     * Callback for add event method.
     */
    @Override
    public void userServiceProcess(String result) {
        try {
            ////Redirect to EventsActivity
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
        catch(Exception e){
            Log.d("RegisterActivity", e.getMessage());
        }
    }


    /**
     * Get Title From View
     */
    private String getTitleFromView()
    {
        //Retrieve string for email.
        EditText editText_Title = (EditText) findViewById(R.id.editText_Title);
        String title = editText_Title.getText().toString().toLowerCase();
        return title;
    }

    /**
     * Get Description From View
     */
    private String getDescriptionFromView() {
        //Retrieve string for password.
        EditText editText_Description = (EditText) findViewById(R.id.editText_Description);
        String description = editText_Description.getText().toString().toLowerCase();
        return description;
    }

    public void btn_go_to_events_activity(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void btn_go_to_balance_activity(View view){
        Intent i = new Intent(this, BalanceActivity.class);
        startActivity(i);
    }

    public void btn_go_to_user_activity(View view){
        Intent i = new Intent(this, UserActivity.class);
        startActivity(i);
    }

}

