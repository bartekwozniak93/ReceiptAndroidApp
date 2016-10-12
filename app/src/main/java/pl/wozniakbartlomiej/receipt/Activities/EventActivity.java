package pl.wozniakbartlomiej.receipt.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.EventServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.IUserServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;

public class EventActivity extends AppCompatActivity implements IUserServiceHelper {

    private EventServiceHelper asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
    }



    @Override
    public void userServiceProcess(String result) {
        try {
            //retrieveUserInformationFromJSON(result);
            ////Redirect to MainActivity
            //Intent i = new Intent(getApplicationContext(), MainActivity.class);
            //startActivity(i);
            //finish();
        }
        catch(Exception e){
            Log.d("RegisterActivity", e.getMessage());
        }
    }

    public void onClick_AddEvent(View view) {
        //Execute async method for register.
        asyncTask =new EventServiceHelper(EventActivity.this);
        asyncTask.delegate = this;
        asyncTask.setProcessDialog(getApplicationContext().getString(R.string.progress_dialog_header));
        asyncTask.execute(ServiceHelper.POST_METHOD, asyncTask.getPostEventString(), getTitleFromView(), getDescriptionFromView());
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

}

