package pl.wozniakbartlomiej.receipt.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.EventServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.IServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.UserSessionManager;

public class EventFromQrCodeActivity extends AppCompatActivity implements IServiceHelper {

    public String eventId;
    public String userEmail;
    SurfaceView cameraView;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    private UserSessionManager session;
    private EventServiceHelper asyncTask;

    //Temporary solution
    private boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_from_qr_code);

        session = new UserSessionManager(getApplicationContext());
        userEmail = session.getProperty(UserSessionManager.SessionKey.EMAIL);

        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException | SecurityException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    eventId =barcodes.valueAt(0).displayValue;
                    addUserToEvent();
                }
            }
        });
    }

    private void addUserToEvent(){
        if(flag) {
            asyncTask = new EventServiceHelper(getApplicationContext());
            asyncTask.delegate = this;
                asyncTask.execute(ServiceHelper.POST_METHOD, ServiceHelper.getAddUserToEventString(), "", "", eventId, userEmail);
            flag = false;
        }

    }

     /**
     * Go to Main Activity
     */
    public void onClick_GoToAddEventActivity(View view){
        Intent i = new Intent(this, AddEventActivity.class);
        startActivity(i);
    }

    @Override
    public void userServiceProcess(String result) {
        Intent i = new Intent(this, UserEventsActivity.class);
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        startActivity(i);
    }
}