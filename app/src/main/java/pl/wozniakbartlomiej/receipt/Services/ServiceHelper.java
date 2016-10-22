package pl.wozniakbartlomiej.receipt.Services;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.ReceiptResources;

/**
 * Created by Bartek on 03/10/16.
 */
public class ServiceHelper {

    public static String PARAMS_TITLE = "title";
    public static String PARAMS_DATE = "date";
    public static String PARAMS_DESCRIPTION = "description";
    public static String PARAMS_TOTAL = "total";
    public static String PARAMS_USER = "user";
    public static String PARAMS_EVENT_ID = "eventId";
    public static String PARAMS_USER_TO_ADD = "userToAdd";
    public static String PARAMS_EMAIL = "email";
    public static String PARAMS_PASSWORD = "password";

    public static String POST_METHOD = "POST";
    public static String GET_METHOD = "GET";
    private UserSessionManager session;
    private static Resources resources = ReceiptResources.getResourcesStatic();
    private static String api_link = resources.getString(R.string.api_link);

    public ServiceHelper(Context context){
        this.session = new UserSessionManager(context);
    }

    /**
     * The following code getPostDataString is from
     * https://www.studytutorial.in/android-httpurlconnection-post-and-get-request-tutorial
     * Add params JSONObject to POST Request.
     */
    public static String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();
        while (itr.hasNext()) {
            String key = itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }

    /**
     * The following code convertToUrl is from
     * http://fancifulandroid.blogspot.sg/2013/07/android-convert-string-to-valid-url.html
     */
    public static URL convertToUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                    url.getHost(), url.getPort(), url.getPath(),
                    url.getQuery(), url.getRef());
            url = uri.toURL();
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * The following code convertToUrl is from
     * http://fancifulandroid.blogspot.sg/2013/07/android-convert-string-to-valid-url.html
     */
    public static void addParamsToRequestBody(HttpURLConnection httpURLConnection, HashMap<String, String> values) {
        if(values == null || values.isEmpty())
            return;
        try {
            JSONObject postDataParams = putValuesIntoJSON(values);
            OutputStream streamm = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(streamm, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            streamm.close();
        } catch (Exception e) {
            Log.d("ServiceHelper", e.getMessage());
        }
    }

    /**
     * Put hashmap into JSONObject
     */
    private static JSONObject putValuesIntoJSON(HashMap<String, String> values){
        JSONObject postDataParams = new JSONObject();
        Iterator it = values.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            try {
                postDataParams.put(pair.getKey().toString(), pair.getValue().toString());
            } catch (JSONException e) {
                Log.d("ServiceHelper", e.getMessage());
            }
            it.remove();
        }
        return postDataParams;
    }

    /**
     * Get result from service for authorization methods
     * as a JSON object.
     */
    public String getJSON(String requestMethod, String url, HashMap<String, String> requestParameters) {
        URL serviceUrl = convertToUrl(url);
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
     * Get parameters from param by given i.
     */
    public static String getParam(int i, String... params) {
        if (params.length > i)
            return params[i];
        else
            return "";
    }

    /**
     * Return token of the user
     */
    public String getAuthorizationToken() {
        String jwtToken = session.getProperty(UserSessionManager.SessionKey.TOKEN);
        return jwtToken;
    }

    public static String getCurrentDate(){
        Time time = new Time();
        time.setToNow();
        return time.toString();
    }

    /**
     * Return link for posting new event.
     */
    public static String getPostEventString() {
        return api_link + resources.getString(R.string.api_post_event);
    }

    /**
     * Return link for getting user's events.
     */
    public static String getUserEventsString() {
        return api_link + resources.getString(R.string.api_get_events);
    }

    /**
     * Return link for getting user event with siven id..
     */
    public static String getEventString() {
        return api_link + resources.getString(R.string.api_get_event);
    }

    /**
     * Return link for adding user to event.
     */
    public static String getAddUserToEventString() {
        return api_link + resources.getString(R.string.api_post_add_user_event);
    }

    /**
     * Return link for login.
     */
    public static String getLoginString() {
        return api_link + resources.getString(R.string.api_login);
    }

    /**
     * Return link for logout.
     */
    public static String getLogoutString() {
        return api_link + resources.getString(R.string.api_logout);
    }

    /**
     * Return link for posting new user or geting info about user.
     */
    public static String getUserString() {
        return api_link + resources.getString(R.string.api_users);
    }

    /**
     * Return link for Facebook login.
     */
    public static String getFacebookString() {
        return api_link + resources.getString(R.string.api_facebook);
    }

    /**
     * Return link for find users name
     */
    public static String getUserFindString() {
        return api_link + resources.getString(R.string.api_users_find);
    }

    /**
     * Return link for add new Receipt.
     */
    public static String getNewReceiptString() {
        return api_link + resources.getString(R.string.api_post_receipt);
    }

    /**
     * Return link for get Receipts.
     */
    public static String getReceiptsString() {
        return api_link + resources.getString(R.string.api_post_get_receipts);
    }
}
