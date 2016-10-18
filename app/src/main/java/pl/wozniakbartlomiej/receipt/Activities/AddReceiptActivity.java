package pl.wozniakbartlomiej.receipt.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.IServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ReceiptServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;

public class AddReceiptActivity extends AppCompatActivity implements IServiceHelper {

    private ReceiptServiceHelper asyncTask;
    private String eventId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId=getIntent().getExtras().getString("eventId");
        setContentView(R.layout.activity_add_receipt);
    }

    public void onClick_AddReceipt(View view) {
        //Execute async method for add event.
        asyncTask =new ReceiptServiceHelper(AddReceiptActivity.this);
        asyncTask.delegate = this;
        asyncTask.setProcessDialog(getApplicationContext().getString(R.string.progress_dialog_header));
        asyncTask.execute(ServiceHelper.POST_METHOD, ServiceHelper.getNewReceiptString(),getTitleFromView(), getDescriptionFromView(), eventId);
    }

    /**
     * Callback for add event method.
     */
    @Override
    public void userServiceProcess(String result) {
        try {
            //Redirect to EventsActivity
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
}
