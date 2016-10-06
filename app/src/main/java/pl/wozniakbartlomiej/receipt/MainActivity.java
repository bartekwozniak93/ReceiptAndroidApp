package pl.wozniakbartlomiej.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

    /**
     * Init session for checking users permissions.
     */
    private void initSession(){
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
    }
}
