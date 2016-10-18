package pl.wozniakbartlomiej.receipt.Widgets;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.EventServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.IServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class AutoCompleteFragment extends Fragment implements IServiceHelper {

    private AutoCompleteDelayTextView autoCompleteUser;
    private Button btn_addUser;
    private String eventId;
    private EventServiceHelper asyncTask;

    public AutoCompleteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_complete, container, false);
        assignViewElements(view);
        getArgumentsFromActivity();
        initAutoComplete(view);
        addButtonListnerForLogOut();
        return view;
    }

    /**
     * Assign view elements.
     */
    private void assignViewElements(View view) {
        autoCompleteUser = (AutoCompleteDelayTextView) view.findViewById(R.id.autocomplete_addUser);
        btn_addUser = (Button) view.findViewById(R.id.btn_addUser);
    }


    /**
     * Init autocomplete.
     */
    private void initAutoComplete(View view){
        autoCompleteUser.setThreshold(1);
        autoCompleteUser.setAdapter(new AutoCompleteUsersAdapter(this.getActivity(), eventId));
        autoCompleteUser.setLoadingIndicator(
                (android.widget.ProgressBar) view.findViewById(R.id.pb_loading_indicator));
        autoCompleteUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String user = (String) adapterView.getItemAtPosition(position);
                autoCompleteUser.setText(user);
            }
        });
    }

    /**
     * Get arguments from Activity.
     */
    private void getArgumentsFromActivity(){
        eventId = getArguments().getString("eventId");
    }

    /**
     * Add listener to button for log out event.
     */
    private void addButtonListnerForLogOut(){
        btn_addUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                asyncTask = new EventServiceHelper(getActivity().getApplicationContext());
                asyncTask.delegate = AutoCompleteFragment.this;
                asyncTask.execute(ServiceHelper.POST_METHOD, ServiceHelper.getAddUserToEventString(), "", "", eventId, autoCompleteUser.getText().toString());
                autoCompleteUser.setText("");
                UsersFragment.getInstance().getUsersForEvent();
            }
        });
    }

    /**
     * Handle callbck for async method to get user's events.
     */
    @Override
    public void userServiceProcess(String result) {
        //extractJson(result);
        //listView.setAdapter(new UsersAdapter(getActivity().getApplicationContext(), usersList));
    }
}


