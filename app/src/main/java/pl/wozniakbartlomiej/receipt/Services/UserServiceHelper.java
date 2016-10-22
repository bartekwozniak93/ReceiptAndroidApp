package pl.wozniakbartlomiej.receipt.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Helper for REST Service for user's methods
 * such as for login, logout etc.
 */
public class UserServiceHelper extends AsyncTask<String, Void, String> {
    private ProgressDialog progressDialog;
    public IServiceHelper delegate;
    private Context applicationContext;


    public UserServiceHelper(Context context) {
        this.applicationContext = context;
    }


    public void setProcessDialog(String title) {
        //initialize progress dialog
        progressDialog = ProgressDialog.show(applicationContext, "", title, false);
    }

    @Override
    protected String doInBackground(String... params) {
        String requestMethod = params[0];
        String url = params[1];
        HashMap<String, String> requestParameters = null;
        JSONObject parameters= null;
        if (requestMethod == ServiceHelper.POST_METHOD) {
            requestParameters = prepareRequestParams(params);
            parameters = ServiceHelper.putValuesIntoJSON(requestParameters);
        }
        return new ServiceHelper(applicationContext).getJSON(requestMethod, url, parameters);
    }

    private HashMap<String, String> prepareRequestParams(String... params) {
        String email = ServiceHelper.getParam(2, params);
        String password = ServiceHelper.getParam(3, params);
        String eventId = ServiceHelper.getParam(4, params);
        HashMap<String, String> requestParameters = new HashMap<>();
        requestParameters.put(ServiceHelper.PARAMS_EMAIL, email);
        requestParameters.put(ServiceHelper.PARAMS_PASSWORD, password);
        requestParameters.put(ServiceHelper.PARAMS_EVENT_ID, eventId);
        return requestParameters;
    }

    /**
     * Handle post execute async task.
     */
    @Override
    public void onPostExecute(String result) {
        //close progress dialog before executing
        if (progressDialog != null)
            progressDialog.dismiss();
        //pass result through delegate
        if (delegate != null)
            delegate.userServiceProcess(result);
    }
}

