package pl.wozniakbartlomiej.receipt.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import java.util.HashMap;

import pl.wozniakbartlomiej.receipt.R;

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
        String email = params[2];
        String password = params[3];
        HashMap<String, String> requestParameters = new HashMap<>();
        requestParameters.put(PARAMS_EMAIL, email);
        requestParameters.put(PARAMS_PASSWORD, password);
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
     * Return link for find users name
     */
    public String getUserFindString() {
        return api_link + applicationResources.getString(R.string.api_users_find);
    }


}

