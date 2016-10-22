package pl.wozniakbartlomiej.receipt.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import java.util.HashMap;

/**
 * Created by Bartek on 19/10/16.
 */
public class ReceiptServiceHelper extends AsyncTask<String, Void, String> {
    private ProgressDialog progressDialog;
    public IServiceHelper delegate;
    private Context applicationContext;
    private Resources applicationResources;
    private UserSessionManager session;

    public ReceiptServiceHelper(Context context) {
        this.applicationContext = context;
        this.session = new UserSessionManager(context);
        this.applicationResources = context.getResources();
    }

    /**
     * Initialize progress dialog with given title.
     */
    public void setProcessDialog(String title) {
        progressDialog= ProgressDialog.show(applicationContext,"",title,false);
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
        String title = ServiceHelper.getParam(2, params);
        String description = ServiceHelper.getParam(3, params);
        String eventId = ServiceHelper.getParam(4, params);
        String total = ServiceHelper.getParam(5, params);
        HashMap<String, String> requestParameters = new HashMap<>();
        requestParameters.put(ServiceHelper.PARAMS_TITLE, title);
        requestParameters.put(ServiceHelper.PARAMS_DESCRIPTION, description);
        requestParameters.put(ServiceHelper.PARAMS_DATE, ServiceHelper.getCurrentDate());
        requestParameters.put(ServiceHelper.PARAMS_EVENT_ID, eventId);
        requestParameters.put(ServiceHelper.PARAMS_TOTAL, total);
        requestParameters.put(ServiceHelper.PARAMS_USER, session.getProperty(UserSessionManager.SessionKey.EMAIL));
        return requestParameters;
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
}
