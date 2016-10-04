package pl.wozniakbartlomiej.receipt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private SessionManager session;

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
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
    }
}
