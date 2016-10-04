package pl.wozniakbartlomiej.receipt;

import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by Bartek on 03/10/16.
 */
public class ServiceHelper {

    public static String POST_METHOD = "POST";
    public static String GET_METHOD = "GET";

    /**
     * The following code getPostDataString is from
     * https://www.studytutorial.in/android-httpurlconnection-post-and-get-request-tutorial
     * Add params JSONObject to POST Request.
     */
    public String getPostDataString(JSONObject params) throws Exception {

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
    public URL convertToUrl(String urlStr) {
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
}
