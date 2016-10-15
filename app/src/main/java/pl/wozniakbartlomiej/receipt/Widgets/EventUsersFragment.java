package pl.wozniakbartlomiej.receipt.Widgets;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.wozniakbartlomiej.receipt.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventUsersFragment extends Fragment {

    private EventUsersFragmentInterface inter;

    public EventUsersFragment() {
        // Required empty public constructor
    }

    public interface EventUsersFragmentInterface{
        void methodInEventUsersFragmentInterface(String str);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_users, container, false);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof EventUsersFragmentInterface){
            inter = (EventUsersFragmentInterface) context;
        }
        else{
            throw new ClassCastException(context.toString() + "You must implement Fragment51Interface");
        }
    }
}
