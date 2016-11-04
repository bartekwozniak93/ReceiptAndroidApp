package pl.wozniakbartlomiej.receipt.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pl.wozniakbartlomiej.receipt.Activities.ReceiptEditActivity;
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
            convertView = inflater.inflate(R.layout.layout_receipt_list_item, parent, false);
            listViewElement = new ListViewElement(convertView);
            convertView.setTag(listViewElement);
        } else {
            listViewElement = (ListViewElement) convertView.getTag();
        }

        listViewElement.getItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReceiptEditActivity.class);
                intent.putExtra("receiptId", currentReceipt.getId());
                intent.putExtra("receiptTitle", currentReceipt.getTitle());
                intent.putExtra("receiptDescription", currentReceipt.getDescription());
                intent.putExtra("receiptTotal", currentReceipt.getTotal());
                intent.putExtra("eventId", currentReceipt.getEventId());
                intent.putExtra("eventTitle", currentReceipt.getEventTitle());
                intent.putExtra("eventDescription", currentReceipt.getDescription());
                context.startActivity(intent);
            }
        });
        listViewElement.setTitle("Title: "+currentReceipt.getTitle());
        listViewElement.setDescription("Description: "+currentReceipt.getDescription());
        listViewElement.setTotal("Total: "+currentReceipt.getTotal());

        return convertView;
    }


    class ListViewElement {
        private LinearLayout layout_Item;
        private TextView textView_Title;
        private TextView textView_Description;
        private TextView textView_Total;

        public ListViewElement(View item) {
            layout_Item = (LinearLayout) item.findViewById(R.id.item);
            textView_Title = (TextView) item.findViewById(R.id.title);
            textView_Description = (TextView) item.findViewById(R.id.description);
            textView_Total = (TextView) item.findViewById(R.id.total);
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


        public LinearLayout getItem() {
            return layout_Item;
        }
    }

}