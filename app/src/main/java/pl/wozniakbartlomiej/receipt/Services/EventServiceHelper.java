package pl.wozniakbartlomiej.receipt.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.text.format.Time;

import java.util.HashMap;

import pl.wozniakbartlomiej.receipt.R;

/**
 * Helper for REST Service for event's methods
 * such as for ass new event, add users to event etx.
 */
public class EventServiceHelper  extends AsyncTask<String, Void, String> {

    public static String PARAMS_TITLE = "title";
    public static String PARAMS_DATE = "date";
    public static String PARAMS_DESCRIPTION = "description";
    public static String PARAMS_USER = "user";
    public static String PARAMS_EVENT_ID = "eventId";
    public static String PARAMS_USER_TO_ADD = "userToAdd";

    private ProgressDialog progressDialog;
    public IServiceHelper delegate;
    private Context applicationContext;
    private Resources applicationResources;
    private String api_link;
    private UserSessionManager session;

    public EventServiceHelper(Context context) {
        this.applicationContext = context;
        this.applicationResources = context.getResources();
        this.api_link = applicationResources.getString(R.string.api_link);
        this.session = new UserSessionManager(context);
    }


    /**
     * Initialize progress dialog with given title.
     */
    public void setProcessDialog(String title) {
        progressDialog=ProgressDialog.show(applicationContext,"",title,false);
    }

    @Override
    protected String doInBackground(String... params) {
        String requestMethod = params[0];
        String url = params[1];
        HashMap<String, String> requestParameters = null;
        if(requestMethod == ServiceHelper.POST_METHOD) {
            requestParameters = prepareRequestParams(params);
        }
        return new ServiceHelper(applicationContext).getJSON(requestMethod, url, requestParameters);
    }

    /**
     * Prepare request parameters for new event.
     */
    private HashMap<String, String> prepareRequestParams(String... params){
        String title = getParam(2, params);
        String description = getParam(3, params);
        String eventId = getParam(4, params);
        String userEmail = getParam(5, params);
        HashMap<String, String> requestParameters = new HashMap<>();
        requestParameters.put(PARAMS_TITLE, title);
        requestParameters.put(PARAMS_DESCRIPTION, description);
        requestParameters.put(PARAMS_DATE, getCurrentDate());
        requestParameters.put(PARAMS_EVENT_ID, eventId);
        requestParameters.put(PARAMS_USER_TO_ADD, userEmail);
        requestParameters.put(PARAMS_USER, session.getProperty(UserSessionManager.SessionKey.EMAIL));
        return requestParameters;
    }

    /**
     * Get parameters from param by given i.
     */
    private String getParam(int i,String... params){
        if(params.length>i)
            return params[i];
        else
            return "";
    }

    /**
     * Handle post execute async task.
    */
    @Override
    public void onPostExecute(String result) {
        //close progress dialog before executing
        if(progressDialog!=null)
            progressDialog.dismiss();
        //pass result through delegate
        if(delegate!=null)
            delegate.userServiceProcess(result);
    }

    /**
     * Return link for posting new event.
     */
    public String getPostEventString() {
        return api_link + applicationResources.getString(R.string.api_post_event);
    }

    /**
     * Return link for getting user's events.
     */
    public String getUserEventsString() {
        return api_link + applicationResources.getString(R.string.api_get_events);
    }

    /**
     * Return link for getting user event with siven id..
     */
    public String getEventString() {
        return api_link + applicationResources.getString(R.string.api_get_event);
    }

    /**
     * Return link for adding user to event.
     */
    public String getAddUserToEventString() {
        return api_link + applicationResources.getString(R.string.api_post_add_user_event);
    }



    private String getCurrentDate(){
        Time time = new Time();
        time.setToNow();
        return time.toString();
    }
}