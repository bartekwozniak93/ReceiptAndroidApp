package pl.wozniakbartlomiej.receipt.Widgets;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pl.wozniakbartlomiej.receipt.Activities.ReceiptActivity;
import pl.wozniakbartlomiej.receipt.Models.Receipt;
import pl.wozniakbartlomiej.receipt.R;

/**
 * Created by Bartek on 22/10/16.
 * Adpater is used for ListView.
 * Receipts elements will be added programmatically to ListView.
 */
public class ReceiptsAdapter extends BaseAdapter {

    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    public ReceiptsAdapter(Context context, ArrayList myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Receipt getItem(int position) {
        return (Receipt) myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Receipt currentReceipt = getItem(position);
        ListViewElement listViewElement;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_list_receipt_item, parent, false);
            listViewElement = new ListViewElement(convertView);
            convertView.setTag(listViewElement);
        } else {
            listViewElement = (ListViewElement) convertView.getTag();
        }

        listViewElement.getItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itent = new Intent(context, ReceiptActivity.class);
                itent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                itent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                itent.putExtra("receiptId", currentReceipt.getId());
                itent.putExtra("receiptTitle", currentReceipt.getTitle());
                itent.putExtra("receiptDescription", currentReceipt.getDescription());
                itent.putExtra("receiptTotal", currentReceipt.getTotal());
                itent.putExtra("eventId", currentReceipt.getEventId());
                context.startActivity(itent);
            }
        });
        listViewElement.setTitle("Title: "+currentReceipt.getTitle());
        listViewElement.setDescription("Description: "+currentReceipt.getDescription());
        listViewElement.setIcon(currentReceipt.getImageId());
        listViewElement.setTotal("Total: "+currentReceipt.getTotal());

        return convertView;
    }


    class ListViewElement {
        private LinearLayout layout_Item;
        private TextView textView_Title;
        private TextView textView_Description;
        private TextView textView_Total;
        private ImageView imageView_Icon;

        public ListViewElement(View item) {
            layout_Item = (LinearLayout) item.findViewById(R.id.item);
            textView_Title = (TextView) item.findViewById(R.id.title);
            textView_Description = (TextView) item.findViewById(R.id.description);
            textView_Total = (TextView) item.findViewById(R.id.total);
            imageView_Icon = (ImageView) item.findViewById(R.id.icon);
        }

        public void setTitle(String text) {
            textView_Title.setText(text);
        }

        public void setDescription(String text) {
            textView_Description.setText(text);
        }

        public void setTotal(String text) {
            textView_Total.setText(text);
        }

        public void setIcon(int imageId) {
            imageView_Icon.setImageResource(imageId);
        }

        public LinearLayout getItem() {
            return layout_Item;
        }
    }

}
