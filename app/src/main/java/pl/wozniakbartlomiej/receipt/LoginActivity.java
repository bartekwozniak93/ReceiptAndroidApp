package pl.wozniakbartlomiej.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements IUserServiceHelper {

    //Button for facebook login
    private LoginButton loginFacebookButton;
    private CallbackManager callbackManager;
    private UserServiceHelper asyncTask;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Init Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());

        //Add callback to Facebook button.
        loginFacebookButton = (LoginButton) findViewById(R.id.login_button);
        loginFacebookButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_friends"));
        callbackManager = CallbackManager.Factory.create();

        /**
         * Handle facebook callback
         */
        loginFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    Toast.makeText(LoginActivity.this, "Response error!", Toast.LENGTH_SHORT).show();
                                } else {
                                    String email = me.optString("email");
                                    loginLocalByFb(email);
                                }
                            }
                        });
                addBundleToFbRequest(request);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login attempt canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(LoginActivity.this, "Login attempt canceled", Toast.LENGTH_SHORT).show();
            }

            private void addBundleToFbRequest(GraphRequest request) {
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }
        });
    }

    private void loginLocalByFb(String email) {
        //Execute async method for login.
        asyncTask = new UserServiceHelper(LoginActivity.this);
        asyncTask.delegate = LoginActivity.this;
        asyncTask.execute(ServiceHelper.POST_METHOD, asyncTask.getFacebookString(), email, "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * User login.
     */
    public void onClick_Login(View view) {
        //Retrive string for email.
        EditText editText_Email = (EditText) findViewById(R.id.editText_Email);
        String email = editText_Email.getText().toString().toLowerCase();
        //Retrive string for password.
        EditText editText_Password = (EditText) findViewById(R.id.editText_Password);
        String password = editText_Password.getText().toString().toLowerCase();
        //Execute async method for login.
        asyncTask = new UserServiceHelper(LoginActivity.this);
        asyncTask.delegate = this;
        asyncTask.execute(ServiceHelper.POST_METHOD, asyncTask.getLoginString(), email, password);
    }

    /**
     * User login callback result.
     */
    @Override
    public void userServiceProcess(String result) {
        try {
            JSONObject resultObject = new JSONObject(result);
            JSONObject userObject = resultObject.getJSONObject("user");
            String email;
            if (!userObject.isNull("local")) {
                JSONObject localUserObject = userObject.getJSONObject("local");
                email = localUserObject.getString("email");
            } else {
                JSONObject localUserObject = userObject.getJSONObject("facebook");
                email = localUserObject.getString("email");
            }

            String token = resultObject.getString("token");


            session.createLoginSession(email, token);
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Wrong login or password.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * User registration.
     */
    public void onClick_Register(View view) {
        Intent myIntent = new Intent(this, RegisterActivity.class);
        startActivity(myIntent);
    }



}
