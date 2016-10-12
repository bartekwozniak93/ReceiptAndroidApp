package pl.wozniakbartlomiej.receipt.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.text.format.Time;

import java.util.HashMap;

import pl.wozniakbartlomiej.receipt.R;

/**
 * Created by Bartek on 10/10/16.
 */
public class EventServiceHelper  extends AsyncTask<String, Void, String> {

    public static String PARAMS_TITLE = "title";
    public static String PARAMS_DATE = "date";
    public static String PARAMS_DESCRIPTION = "description";
    public static String PARAMS_USER = "user";

    private ProgressDialog progressDialog;
    public IUserServiceHelper delegate;
    private Context applicationContext;
    private Resources applicationResources;
    private String api_link;
    private SessionManager session;

    public EventServiceHelper(Context context) {
        this.applicationContext = context;
        this.applicationResources = context.getResources();
        this.api_link = applicationResources.getString(R.string.api_link);
        this.session = new SessionManager(context);
    }


    public void setProcessDialog(String title) {
        //initialize progress dialog
        progressDialog=ProgressDialog.show(applicationContext,"",title,false);
    }

    @Override
    protected String doInBackground(String... params) {
        String requestMethod = params[0];
        String url = params[1];
        HashMap<String, String> requestParameters = prepareRequestParams(params);
        return new ServiceHelper(applicationContext).getJSON(requestMethod, url, requestParameters);
    }

    private HashMap<String, String> prepareRequestParams(String... params){
        String title = params[2];
        String description = params[3];
        HashMap<String, String> requestParameters = new HashMap<>();
        requestParameters.put(PARAMS_TITLE, title);
        requestParameters.put(PARAMS_DESCRIPTION, description);
        Time time = new Time();
        time.setToNow();
        requestParameters.put(PARAMS_DATE, time.toString());
        requestParameters.put(PARAMS_USER, session.getProperty(SessionManager.SessionKey.EMAIL));
        return requestParameters;
    }

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
     * Return link for login.
     */
    public String getPostEventString() {
        return api_link + applicationResources.getString(R.string.api_post_event);
    }




}