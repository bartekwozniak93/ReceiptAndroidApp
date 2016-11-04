package pl.wozniakbartlomiej.receipt.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.UserSessionManager;

/**
 * Fragment for user loggout button
 * and info who is logged in.
 */
public class UserLogOutFragment extends Fragment {

    private UserSessionManager session;
    private View view;

    public UserLogOutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Initialize fragment to find its elements.
        view = inflater.inflate(R.layout.fragment_logout, container, false);
        //Initialize session to get user information.
        session = new UserSessionManager(getActivity().getApplicationContext());

        setInfoAboutLoggedUser();
        addButtonListnerForLogOut();

        return view;
    }

    /**
     * Set information (email) of the user
     * in TextView.
     */
    private void setInfoAboutLoggedUser(){
        String loggedAsText = getContext().getString(R.string.textview_logged_as);
        String userEmail = session.getProperty(UserSessionManager.SessionKey.EMAIL);
        TextView textView_LoggedAs = (TextView) view.findViewById(R.id.textView_LoggedAs);
        textView_LoggedAs.setText(loggedAsText+" "+userEmail);
    }

    /**
     * Add listener to button for log out event.
     */
    private void addButtonListnerForLogOut(){
        Button button = (Button) view.findViewById(R.id.btn_logout);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    LoginManager.getInstance().logOut();
                }
                catch(Exception e){
                    Log.d("Button_Fragment","Problem with loging out user.");
                };
                session.logoutUser();
                session.checkLogin();
            }
        });
    }


}
