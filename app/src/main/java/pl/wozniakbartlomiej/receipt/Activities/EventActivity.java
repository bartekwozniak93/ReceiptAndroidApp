package pl.wozniakbartlomiej.receipt.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import pl.wozniakbartlomiej.receipt.R;

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
