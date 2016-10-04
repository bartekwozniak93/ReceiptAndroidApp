package pl.wozniakbartlomiej.receipt;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private SessionManager session;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        session = new SessionManager(getActivity().getApplicationContext());

        TextView textView_LoggedAs = (TextView) view.findViewById(R.id.textView_LoggedAs);
        textView_LoggedAs.setText("You are logged as "+session.getUserEmail());

        Button b = (Button) view.findViewById(R.id.btn_logout);
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    LoginManager.getInstance().logOut();
                }
                catch(Exception e){};
                session.logoutUser();
                session.checkLogin();
            }
        });
        return view;
    }


}
