package pl.wozniakbartlomiej.receipt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Helper for REST Service.
 */
public class UserServiceHelper extends AsyncTask<String, Void, String> {

    public static String PARAMS_EMAIL = "email";
    public static String PARAMS_PASSWORD = "password";

    private ProgressDialog progressDialog;

    public IUserServiceHelper delegate;
    private Context applicationContext;
    private Resources applicationResources;
    private String api_link;

    public UserServiceHelper(Context context) {
        this.applicationContext = context;
        this.applicationResources = context.getResources();
        this.api_link = applicationResources.getString(R.string.api_link);
    }

    @Override
    protected void onPreExecute() {
        //initialize progress dialog before executing
        progressDialog=ProgressDialog.show(applicationContext,"",applicationResources.getString(R.string.progress_dialog_header),false);
    }

    @Override
    protected String doInBackground(String... params) {
        String requestMethod = params[0];
        String url = params[1];
        String email = params[2];
        String password = params[3];
        HashMap<String, String> requestParameters = new HashMap<>();
        requestParameters.put(PARAMS_EMAIL, email);
        requestParameters.put(PARAMS_PASSWORD, password);
        return getJSON(requestMethod, url, requestParameters);
    }

    @Override
    public void onPostExecute(String result) {
        //close progress dialog before executing
        progressDialog.dismiss();
        //pass result through delegate
        delegate.userServiceProcess(result);
    }

    /**
     * Get result from service for authorization methods
     * as a JSON object.
     */
    public String getJSON(String requestMethod, String url, HashMap<String, String> requestParameters) {
        URL serviceUrl = new ServiceHelper().convertToUrl(url);
        HttpURLConnection httpURLConnection = null;
        int responseCode = -1;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //Prepare Request Before Send
            httpURLConnection = (HttpURLConnection) serviceUrl.openConnection();
            httpURLConnection.setRequestMethod(requestMethod);
            httpURLConnection.setRequestProperty("Authorization", getAuthorizationToken());
            httpURLConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            //Add parameters to Request Body.
            ServiceHelper.addParamsToRequestBody(httpURLConnection, requestParameters);
            //Send request
            httpURLConnection.connect();
            responseCode = httpURLConnection.getResponseCode();
            if (responseCode == httpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }

        return stringBuilder.toString();
    }

    /**
     * Return link for login.
     */
    public String getLoginString() {
        return api_link + applicationResources.getString(R.string.api_login);
    }

    /**
     * Return link for logout.
     */
    public String getLogoutString() {
        return api_link + applicationResources.getString(R.string.api_logout);
    }

    /**
     * Return link for posting new user or geting info about user.
     */
    public String getUserString() {
        return api_link + applicationResources.getString(R.string.api_users);
    }

    /**
     * Return link for Facebook login.
     */
    public String getFacebookString() {
        return api_link + applicationResources.getString(R.string.api_facebook);
    }

    /**
     * Return token of the user
     */
    public String getAuthorizationToken() {
        String jwtToken = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1N2YxMDhiNzMzOTlmZGJhMTIyMjA3ZTEiLCJqdGkiOiJjODEyM2YyYy0wZjJlLTRlYWYtOGViYi0yMGQ5ZTllZDg1NWYiLCJpYXQiOjE0NzU0MjQxMTgsImV4cCI6MTQ3NTQyNzExOH0.kgixc896yTCFTQ05gqgo0GvOvH9GlH1fkXof0bYrrvw";
        return jwtToken;
    }

}

