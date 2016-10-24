package pl.wozniakbartlomiej.receipt.Widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.wozniakbartlomiej.receipt.Models.User;
import pl.wozniakbartlomiej.receipt.R;

/**
 * Created by Bartek on 16/10/16.
 */
public class UsersForReceiptAdapter extends BaseAdapter {
    List<User> myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    public UsersForReceiptAdapter(Context context, ArrayList myList) {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final User currentUser = getItem(position);
        final ListViewElement listViewElement;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_list_user_item_for_add_receipt, parent, false);
            listViewElement = new ListViewElement(convertView);
            convertView.setTag(listViewElement);
            convertView.setTag(R.id.checkBox, listViewElement.checkBox_IsChecked);
        } else {
            listViewElement = (ListViewElement) convertView.getTag();
        }
        listViewElement.checkBox_IsChecked.setTag(position);
        listViewElement.setEmail("Email: "+currentUser.getEmail());
        listViewElement.setCheckBox(currentUser.getIsChecked());
        return convertView;
    }


    class ListViewElement {
        private LinearLayout layout_Item;
        private TextView textView_Email;
        private CheckBox checkBox_IsChecked;


        public ListViewElement(View item) {
            layout_Item = (LinearLayout) item.findViewById(R.id.item);
            textView_Email = (TextView) item.findViewById(R.id.email);
            checkBox_IsChecked = (CheckBox) item.findViewById(R.id.checkBox);
            checkBox_IsChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int getPosition = (Integer) compoundButton.getTag();
                    myList.get(getPosition).setIsChecked(b);
                }
            });
        }

        public void setEmail(String text) {
            textView_Email.setText(text);
        }

        public void setCheckBox(Boolean checked) {
            checkBox_IsChecked.setChecked(checked);
        }

        public LinearLayout getItem() {
            return layout_Item;
        }
    }
}
