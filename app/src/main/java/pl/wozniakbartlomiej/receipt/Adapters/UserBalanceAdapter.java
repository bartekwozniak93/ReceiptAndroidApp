package pl.wozniakbartlomiej.receipt.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pl.wozniakbartlomiej.receipt.Models.UserBalance;
import pl.wozniakbartlomiej.receipt.R;

/**
 * Created by Bartek on 04/11/16.
 */
public class UserBalanceAdapter extends BaseAdapter {

    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    public UserBalanceAdapter(Context context, ArrayList myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public UserBalance getItem(int position) {
        return (UserBalance) myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final UserBalance currentBalance = getItem(position);
        ListViewElement listViewElement;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_event_balance_cost_list_item, parent, false);
            listViewElement = new ListViewElement(convertView);
            convertView.setTag(listViewElement);
        } else {
            listViewElement = (ListViewElement) convertView.getTag();
        }

        listViewElement.setEventTitle("Title: "+currentBalance.getEventTitle());
        listViewElement.setEventDescription("Description: "+currentBalance.getEventDescription());
        listViewElement.setBalance("Balance: "+currentBalance.getBalance());

        return convertView;
    }


    class ListViewElement {
        private LinearLayout layout_Item;
        private TextView textView_EventTitle;
        private TextView textView_EventDescription;
        private TextView textView_Balance;

        public ListViewElement(View item) {
            layout_Item = (LinearLayout) item.findViewById(R.id.item);
            textView_EventTitle = (TextView) item.findViewById(R.id.eventTitle);
            textView_EventDescription = (TextView) item.findViewById(R.id.eventDescription);
            textView_Balance = (TextView) item.findViewById(R.id.balance);
        }

        public void setEventTitle(String text) {
            textView_EventTitle.setText(text);
        }

        public void setEventDescription(String text) {
            textView_EventDescription.setText(text);
        }

        public void setBalance(String text) {
            textView_Balance.setText(text);
        }

        public LinearLayout getItem() {
            return layout_Item;
        }
    }

}
