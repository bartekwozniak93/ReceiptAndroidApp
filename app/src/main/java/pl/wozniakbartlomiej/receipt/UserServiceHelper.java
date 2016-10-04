package pl.wozniakbartlomiej.receipt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Helper for REST Service
 */
public class UserServiceHelper extends AsyncTask<String, Void, String> {
    public ProgressDialog pd;

    public IUserServiceHelper delegate = null;
    private Context applicationContext;
    private Resources applicationResources;
    private String api_link;

    public UserServiceHelper(Context context) {
        this.applicationContext = context;
        this.applicationResources = context.getResources();
        this.api_link = applicationResources.getString(R.string.api_link);
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


    protected String doInBackground(String... strings) {
        return getJSON(strings[0], strings[1], strings[2], strings[3]);
    }

    @Override
    protected void onPreExecute() {
        pd=ProgressDialog.show(applicationContext,"","Please Wait",false);
    }

    public String getJSON(String requestMethod, String url, String email, String password) {


        URL serviceUrl = new ServiceHelper().convertToUrl(url);
        HttpURLConnection httpURLConnection = null;
        int responseCode = -1;
        StringBuilder stringBuilder = new StringBuilder();


        try {
            httpURLConnection = (HttpURLConnection) serviceUrl.openConnection();
            httpURLConnection.setRequestMethod(requestMethod);
            httpURLConnection.setRequestProperty("Authorization", getAuthorizationToken());
            httpURLConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            addParamsToRequestBody(httpURLConnection, email, password);

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

    public void onPostExecute(String result) {
        pd.dismiss();
        delegate.userServiceProcess(result);

    }

    private void addParamsToRequestBody(HttpURLConnection httpURLConnection, String email, String password) {
        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("email", email);
            postDataParams.put("password", password);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(new ServiceHelper().getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

