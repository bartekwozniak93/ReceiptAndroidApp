package pl.wozniakbartlomiej.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements IUserServiceHelper {

    private UserServiceHelper asyncTask;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        session = new SessionManager(getApplicationContext());
    }

    /**
     * User register.
     */
    public void onClick_Register(View view) {
        //Retrive string for email.
        EditText editText_Email = (EditText) findViewById(R.id.editText_Email);
        String email = editText_Email.getText().toString().toLowerCase();
        //Retrive string for password.
        EditText editText_Password = (EditText) findViewById(R.id.editText_Password);
        String password = editText_Password.getText().toString().toLowerCase();
        //Execute async method for register.
        asyncTask =new UserServiceHelper(RegisterActivity.this);
        asyncTask.delegate = this;
        asyncTask.execute(ServiceHelper.POST_METHOD, asyncTask.getUserString(), email, password);
    }

    @Override
    public void userServiceProcess(String result) {
        try {
            JSONObject resultObject = new JSONObject(result);
            JSONObject userObject = resultObject.getJSONObject("user");
            JSONObject localUserObject = userObject.getJSONObject("local");
            String email = localUserObject.getString("email");
            String token = resultObject.getString("token");
            session.createLoginSession(email, token);
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
        catch(Exception e){
            Toast.makeText(this, "This login is already taken.", Toast.LENGTH_SHORT).show();
        }
    }
}
