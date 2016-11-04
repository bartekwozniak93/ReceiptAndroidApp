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

import pl.wozniakbartlomiej.receipt.Activities.EventBalanceActivity;
import pl.wozniakbartlomiej.receipt.Models.Event;
import pl.wozniakbartlomiej.receipt.R;

/**
 * Created by Bartek on 16/10/16.
 * Adpater is used for ListView.
 * Event elements will be added programmatically to ListView.
 */
public class EventsAdapter extends BaseAdapter {

    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    public EventsAdapter(Context context, ArrayList myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Event getItem(int position) {
        return (Event) myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Event currentEvent = getItem(position);
        ListViewElement listViewElement;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_user_events_list_item, parent, false);
            listViewElement = new ListViewElement(convertView);
            convertView.setTag(listViewElement);
        } else {
            listViewElement = (ListViewElement) convertView.getTag();
        }

        listViewElement.getItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itent = new Intent(context, EventBalanceActivity.class);
                itent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                itent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                itent.putExtra("eventId", currentEvent.getId());
                itent.putExtra("eventTitle", currentEvent.getTitle());
                itent.putExtra("eventDescription", currentEvent.getDescription());
                context.startActivity(itent);

            }
        });
        listViewElement.setTitle("Title: "+currentEvent.getTitle());
        listViewElement.setDescription("Description: "+currentEvent.getDescription());

        return convertView;
    }


    class ListViewElement {
        private LinearLayout layout_Item;
        private TextView textView_Title;
        private TextView textView_Description;

        public ListViewElement(View item) {
            layout_Item = (LinearLayout) item.findViewById(R.id.item);
            textView_Title = (TextView) item.findViewById(R.id.title);
            textView_Description = (TextView) item.findViewById(R.id.description);
        }

        public void setTitle(String text) {
            textView_Title.setText(text);
        }

        public void setDescription(String text) {
            textView_Description.setText(text);
        }

        public LinearLayout getItem() {
            return layout_Item;
        }
    }

}
