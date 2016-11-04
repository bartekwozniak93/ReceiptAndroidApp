package pl.wozniakbartlomiej.receipt.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pl.wozniakbartlomiej.receipt.Models.User;
import pl.wozniakbartlomiej.receipt.R;

/**
 * Created by Bartek on 16/10/16.
 */
public class UsersAdapter extends BaseAdapter {
    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    public UsersAdapter(Context context, ArrayList myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public User getItem(int position) {
        return (User) myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final User currentUser = getItem(position);
        ListViewElement listViewElement;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_add_user_list_item, parent, false);
            listViewElement = new ListViewElement(convertView);
            convertView.setTag(listViewElement);
        } else {
            listViewElement = (ListViewElement) convertView.getTag();
        }


        listViewElement.setEmail(currentUser.getEmail());

        return convertView;
    }


    class ListViewElement {
        private LinearLayout layout_Item;
        private TextView textView_Email;

        public ListViewElement(View item) {
            layout_Item = (LinearLayout) item.findViewById(R.id.item);
            textView_Email = (TextView) item.findViewById(R.id.email);
        }

        public void setEmail(String text) {
            textView_Email.setText(text);
        }

        public LinearLayout getItem() {
            return layout_Item;
        }
    }
}
