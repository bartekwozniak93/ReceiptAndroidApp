package pl.wozniakbartlomiej.receipt.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.UserSessionManager;

public class EventQrCodeActivity extends AppCompatActivity {

    private String eventId;
    private String eventTitle;
    private String eventDescription;
    private TextView textView_Title;
    private UserSessionManager session;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_qr_code);
        initSession();
        getExtrasFromIntent();
        assignViewElements();
        setTextToViewElements();
        initQR();
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
     * Assign view elements.
     */
    private void assignViewElements() {
        textView_Title = (TextView) findViewById(R.id.textView_Title);
    }

    public void onClick_GoToEventActivity(View v){
        Intent i = new Intent(getApplicationContext(), EventBalanceActivity.class);
        i.putExtra("eventId",eventId);
        i.putExtra("eventTitle", eventTitle);
        i.putExtra("eventDescription", eventDescription);
        startActivity(i);
        finish();
    }

    public void initQR(){
        iv = (ImageView) findViewById(R.id.imageView);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Bitmap bitmap=null;
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(eventId, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        iv.setImageBitmap(bitmap);
    }

    /**
     * Set text (title, description) to view elements.
     */
    private void  setTextToViewElements(){
        textView_Title.setText(eventTitle);
    }
}
