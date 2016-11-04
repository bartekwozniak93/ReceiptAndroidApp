package pl.wozniakbartlomiej.receipt.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import pl.wozniakbartlomiej.receipt.Activities.LoginActivity;

/**
 * Created by Bartek on 03/10/16.
 * Store user login data in app session
 * using SharedPreferences.
 */
public class UserSessionManager {

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    static final String PREF_NAME = "ApplicationPreferences";
    static final String TOKEN_PREFIX = "JWT ";
    /**
     * Enum keys for properties.
     * */
    public enum SessionKey {
        IS_LOGGED_IN,
        TOKEN,
        EMAIL
    }

    public UserSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Get session property.
     * */
    public String getProperty(SessionKey key){
        return pref.getString(key.name(), null);
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String email, String token){
        editor.putBoolean(SessionKey.IS_LOGGED_IN.name(), true);
        editor.putString(SessionKey.TOKEN.name(), TOKEN_PREFIX+token);
        editor.putString(SessionKey.EMAIL.name(), email);
        editor.commit();
    }

    /**
     *
     * if not redirect to LoginActivity.
     * */
    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            // Close all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Start Login Activity
            _context.startActivity(i);
        }
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Loing Activity
        goToLoginActivity();
    }

    private void goToLoginActivity(){
        Intent intent = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(intent);
    }

    /**
     * Check is user logged in
     **/
    public boolean isLoggedIn(){
        return pref.getBoolean(SessionKey.IS_LOGGED_IN.name(), false);
    }
}
