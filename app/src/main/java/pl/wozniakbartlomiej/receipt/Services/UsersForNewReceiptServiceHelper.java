package pl.wozniakbartlomiej.receipt.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pl.wozniakbartlomiej.receipt.Fragments.UsersForEditReceiptFragment;
import pl.wozniakbartlomiej.receipt.Fragments.UsersForNewReceiptFragment;

/*
 * Helper for REST Service for event's methods
 * such as for ass new event, add users to event etx.
 */
public class UsersForNewReceiptServiceHelper extends AsyncTask<String, Void, String> {
    private ProgressDialog progressDialog;
    public IServiceHelper delegate;
    private Context applicationContext;
    private UserSessionManager session;

    public UsersForNewReceiptServiceHelper(Context context) {
        this.applicationContext = context;
        this.session = new UserSessionManager(context);
    }

    /**
     * Initialize progress dialog with given title.
     */
    public void setProcessDialog(String title) {
        progressDialog = ProgressDialog.show(applicationContext, "", title, false);
    }

    @Override
    protected String doInBackground(String... params) {
        String requestMethod = params[0];
        String url = params[1];
        HashMap<String, String> requestParameters = null;
        JSONObject parameters = null;
        if (requestMethod == ServiceHelper.POST_METHOD) {
            requestParameters = prepareRequestParams(params);
            parameters = ServiceHelper.putValuesIntoJSON(requestParameters);
            putSelectedUsersToJSON(parameters);
        }
        return new ServiceHelper(applicationContext).getJSON(requestMethod, url, parameters);
    }

    /**
     * Prepare request parameters for new event.
     */
    private HashMap<String, String> prepareRequestParams(String... params) {
        String title = ServiceHelper.getParam(2, params);
        String description = ServiceHelper.getParam(3, params);
        String eventId = ServiceHelper.getParam(4, params);
        String total = ServiceHelper.getParam(5, params);
        String receiptId = ServiceHelper.getParam(6, params);
        HashMap<String, String> requestParameters = new HashMap<>();
        requestParameters.put(ServiceHelper.PARAMS_TITLE, title);
        requestParameters.put(ServiceHelper.PARAMS_DESCRIPTION, description);
        requestParameters.put(ServiceHelper.PARAMS_DATE, ServiceHelper.getCurrentDate());
        requestParameters.put(ServiceHelper.PARAMS_EVENT_ID, eventId);
        requestParameters.put(ServiceHelper.PARAMS_TOTAL, total);
        requestParameters.put(ServiceHelper.PARAMS_RECEIPT_ID, receiptId);
        requestParameters.put(ServiceHelper.PARAMS_USER, session.getProperty(UserSessionManager.SessionKey.EMAIL));
        return requestParameters;
    }

    private void putSelectedUsersToJSON(JSONObject object) {
        HashMap<String, Boolean> map;
        if(UsersForNewReceiptFragment.getInstance()!=null)
            map = UsersForNewReceiptFragment.getInstance().getCheckedUsers();
        else
            map = UsersForEditReceiptFragment.getInstance().getCheckedUsers();
        try {
            object.put("users", putValuesIntoJSON(map));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Put hashmap into JSONArray
     */
    private JSONArray putValuesIntoJSON(HashMap<String, Boolean> values) {
        JSONArray postDataParams = new JSONArray();
        for (Map.Entry<String, Boolean> entry : values.entrySet()) {
            if (entry.getValue() == true)
                postDataParams.put(entry.getKey());
        }
        return postDataParams;
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