package pl.wozniakbartlomiej.receipt.Services;

import android.content.Context;
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

/**
 * Created by Bartek on 03/10/16.
 */
public class ServiceHelper {

    public static String POST_METHOD = "POST";
    public static String GET_METHOD = "GET";
    private SessionManager session;

    public ServiceHelper(Context context){
        this.session = new SessionManager(context);
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
            Log.d("UserServiceHelper", e.getMessage());
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
                Log.d("UserServiceHelper", e.getMessage());
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
     * Return token of the user
     */
    public String getAuthorizationToken() {
        String jwtToken = session.getProperty(SessionManager.SessionKey.TOKEN);
        return jwtToken;
    }
}
