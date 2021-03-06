package pl.wozniakbartlomiej.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements IUserServiceHelper {

    private UserServiceHelper asyncTask;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initSession();
    }

    /**
     * Init session for checking users permissions.
     */
    private void initSession(){
        session = new SessionManager(getApplicationContext());
    }

    /**
     * User register.
     */
    public void onClick_Register(View view) {
        //Execute async method for register.
        asyncTask =new UserServiceHelper(RegisterActivity.this);
        asyncTask.delegate = this;
        asyncTask.execute(ServiceHelper.POST_METHOD, asyncTask.getUserString(), getUserEmailFromView(), getUserPasswordFromView());
    }

    /**
     * Get User Email From View
     */
    private String getUserEmailFromView()
    {
        //Retrieve string for email.
        EditText editText_Email = (EditText) findViewById(R.id.editText_Email);
        String email = editText_Email.getText().toString().toLowerCase();
        return email;
    }

    /**
     * Get User Password From View
     */
    private String getUserPasswordFromView() {
        //Retrieve string for password.
        EditText editText_Password = (EditText) findViewById(R.id.editText_Password);
        String password = editText_Password.getText().toString().toLowerCase();
        return password;
    }

    @Override
    public void userServiceProcess(String result) {
        try {
            retrieveUserInformationFromJSON(result);
            //Redirect to MainActivity
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
        catch(Exception e){
            Log.d("RegisterActivity", e.getMessage());
        }
    }

    /**
     * Retrieve user information from result(JSON)
     */
    private void retrieveUserInformationFromJSON(String result) throws JSONException {
        try {
        JSONObject resultObject = new JSONObject(result);
        JSONObject userObject = resultObject.getJSONObject("user");
        JSONObject localUserObject = userObject.getJSONObject("local");
        String email = localUserObject.getString("email");
        String token = resultObject.getString("token");
        session.createLoginSession(email, token);}
        catch(JSONException e){
            Log.d("RegisterActivity", e.getMessage());
            Toast.makeText(this, R.string.warning_email_already_taken, Toast.LENGTH_SHORT).show();
            throw e;
        }
    }
}
