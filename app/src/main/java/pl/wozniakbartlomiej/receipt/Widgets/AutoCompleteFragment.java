package pl.wozniakbartlomiej.receipt.Widgets;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import pl.wozniakbartlomiej.receipt.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AutoCompleteFragment extends Fragment {

    private AutoCompleteDelayTextView bookTitle;

    public AutoCompleteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auto_complete, container, false);

        bookTitle = (AutoCompleteDelayTextView) view.findViewById(R.id.et_book_title);
        bookTitle.setThreshold(1);
        bookTitle.setAdapter(new AutoCompleteUsersAdapter(this.getActivity())); // 'this' is Activity instance
        bookTitle.setLoadingIndicator(
                (android.widget.ProgressBar) view.findViewById(R.id.pb_loading_indicator));
        bookTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String book = (String) adapterView.getItemAtPosition(position);
                bookTitle.setText(book);
            }
        });

        return view;
    }
}


