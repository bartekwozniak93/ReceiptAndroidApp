package pl.wozniakbartlomiej.receipt.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pl.wozniakbartlomiej.receipt.Models.Cost;
import pl.wozniakbartlomiej.receipt.R;

/**
 * Created by Bartek on 04/11/16.
 */
public class EventCostsAdapter extends BaseAdapter {

    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    public EventCostsAdapter(Context context, ArrayList myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Cost getItem(int position) {
        return (Cost) myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Cost currentCost = getItem(position);
        ListViewElement listViewElement;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_event_balance_list_item, parent, false);
            listViewElement = new ListViewElement(convertView);
            convertView.setTag(listViewElement);
        } else {
            listViewElement = (ListViewElement) convertView.getTag();
        }

        listViewElement.setUserForCost(currentCost.getUserForCost().substring(0, 1).toUpperCase() + currentCost.getUserForCost().substring(1));
        listViewElement.setValueForCost("owes "+currentCost.getValueForCost());
        listViewElement.setUser("to "+currentCost.getUser()+".");

        return convertView;
    }


    class ListViewElement {
        private LinearLayout layout_Item;
        private TextView textView_User;
        private TextView textView_UserForCost;
        private TextView textView_ValueForCost;

        public ListViewElement(View item) {
            layout_Item = (LinearLayout) item.findViewById(R.id.item);
            textView_User = (TextView) item.findViewById(R.id.user);
            textView_UserForCost = (TextView) item.findViewById(R.id.userForCost);
            textView_ValueForCost = (TextView) item.findViewById(R.id.valueForCost);
        }

        public void setUser(String text) {
            textView_User.setText(text);
        }

        public void setUserForCost(String text) {
            textView_UserForCost.setText(text);
        }

        public void setValueForCost(String text) {
            textView_ValueForCost.setText(text);
        }

        public LinearLayout getItem() {
            return layout_Item;
        }
    }

}
