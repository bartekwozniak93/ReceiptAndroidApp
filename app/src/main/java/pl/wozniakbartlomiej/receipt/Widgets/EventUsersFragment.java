package pl.wozniakbartlomiej.receipt.Widgets;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.wozniakbartlomiej.receipt.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventUsersFragment extends Fragment {


    public EventUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_users, container, false);
    }
}
