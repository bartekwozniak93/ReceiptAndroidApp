package pl.wozniakbartlomiej.receipt.Widgets;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.CheckBox;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import java.util.ArrayList;

        import pl.wozniakbartlomiej.receipt.Models.User;
        import pl.wozniakbartlomiej.receipt.R;

/**
 * Created by Bartek on 16/10/16.
 */
public class UsersForNewReceiptAdapter extends BaseAdapter {
    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    public UsersForNewReceiptAdapter(Context context, ArrayList myList) {
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
            convertView = inflater.inflate(R.layout.layout_list_user_item_for_add_receipt, parent, false);
            listViewElement = new ListViewElement(convertView);
            convertView.setTag(listViewElement);
        } else {
            listViewElement = (ListViewElement) convertView.getTag();
        }

        //listViewElement.getItem().setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        //Intent itent = new Intent(context, EventActivity.class);
        //        //itent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //        //itent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //        //itent.putExtra("id", currentListData.getId());
        //        //itent.putExtra("email", currentListData.getEmail());
        //        //itent.putExtra("description", currentListData.getDescription());
        //        //context.startActivity(itent);
//
        //    }
        //});
        listViewElement.setEmail(currentUser.getEmail());
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
