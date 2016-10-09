package pl.wozniakbartlomiej.receipt;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindUsersFragment extends Fragment implements IServiceHelper {
    private View view;
    private UserServiceHelper asyncTask;
    private EditText editText;
    public FindUsersFragment() {
        // Required empty public constructor
    }
    DelayAutoCompleteTextView bookTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_find_users, container, false);
        addEditTextListnerForFindUsers();







        bookTitle = (DelayAutoCompleteTextView) view.findViewById(R.id.et_book_title);
        bookTitle.setThreshold(1);
        bookTitle.setAdapter(new BookAutoCompleteAdapter(view.getContext())); // 'this' is Activity instance
        bookTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String book = (String) adapterView.getItemAtPosition(position);
                bookTitle.setText(book);
            }
        });





        return view;
    }

    /**
     * Add listener to button for log out event.
     */
    private void addEditTextListnerForFindUsers(){
        editText = (EditText) view.findViewById(R.id.editText_FindUsers);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                asyncTask =new UserServiceHelper(getContext());
                asyncTask.delegate = FindUsersFragment.this;
                asyncTask.execute(ServiceHelper.POST_METHOD, asyncTask.getUserFindString(), editText.getText().toString(), "");
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {


            }
        });


    }

    @Override
    public void processService(String result) {
        TextView editText = (TextView) view.findViewById(R.id.textView_FindUsers);
        editText.setText(result);
    }
}
