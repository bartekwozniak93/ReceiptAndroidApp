package pl.wozniakbartlomiej.receipt.Widgets;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Services.ServiceHelper;
import pl.wozniakbartlomiej.receipt.Services.UserServiceHelper;

/*
 * Created by Bartek on 10/10/16.
 * Based on http://makovkastar.github.io/blog/2014/04/12/android-autocompletetextview-with-suggestions-from-a-web-service/
 */
public class AutoCompleteUsersAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private List<String> resultList = new ArrayList<String>();
    private UserServiceHelper asyncTask;
    private List<String> users;
    private String eventId;

    public AutoCompleteUsersAdapter(Context context, String eventId) {
        this.mContext = context;
        this.users = new ArrayList<String>();
        this.eventId = eventId;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_dropdown_list_item, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.text1)).setText(getItem(position));
        ((TextView) convertView.findViewById(R.id.text2)).setText(getItem(position));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<String> books = findUsers(mContext, constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = books;
                    filterResults.count = books.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<String>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    /**
     * Returns a search result for the given book title.
     */
    private List<String> findUsers(Context context, String userValue) {
        asyncTask =new UserServiceHelper(context);
        users.clear();
        try {
            retrieveUserInformationFromJSON(asyncTask.execute(ServiceHelper.POST_METHOD, asyncTask.getUserFindString(), userValue, "", eventId).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return users;
    }


    /**
     * Retrieve users information from result(JSON)
     */
    private void retrieveUserInformationFromJSON(String result) throws JSONException {
        try {
            JSONArray resultObject = new JSONArray(result);
            for (int i = 0; i < resultObject.length(); i++) {
                JSONObject localUserObject = resultObject.getJSONObject(i).getJSONObject("local");
                String email = localUserObject.getString("email");
                users.add(email);
            }
        } catch (JSONException e) {
            Log.d("AutoCompleteAdapter", e.getMessage());
        }
    }
}

