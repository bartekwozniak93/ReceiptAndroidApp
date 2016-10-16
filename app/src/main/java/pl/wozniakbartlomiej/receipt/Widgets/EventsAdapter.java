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

import pl.wozniakbartlomiej.receipt.Activities.EventActivity;
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
        final Event currentListData = getItem(position);
        ListViewElement listViewElement;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_list_item, parent, false);
            listViewElement = new ListViewElement(convertView);
            convertView.setTag(listViewElement);
        } else {
            listViewElement = (ListViewElement) convertView.getTag();
        }

        listViewElement.getItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itent = new Intent(context, EventActivity.class);
                itent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                itent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                itent.putExtra("id", currentListData.getId());
                itent.putExtra("title", currentListData.getTitle());
                itent.putExtra("description", currentListData.getDescription());
                context.startActivity(itent);

            }
        });
        listViewElement.setTitle(currentListData.getTitle());
        listViewElement.setDescription(currentListData.getDescription());
        listViewElement.setIcon(currentListData.getImageId());

        return convertView;
    }


    class ListViewElement {
        private LinearLayout layout_Item;
        private TextView textView_Title;
        private TextView textView_Description;
        private ImageView imageView_Icon;

        public ListViewElement(View item) {
            layout_Item = (LinearLayout) item.findViewById(R.id.item);
            textView_Title = (TextView) item.findViewById(R.id.title);
            textView_Description = (TextView) item.findViewById(R.id.description);
            imageView_Icon = (ImageView) item.findViewById(R.id.icon);
        }

        public void setTitle(String text) {
            textView_Title.setText(text);
        }

        public void setDescription(String text) {
            textView_Description.setText(text);
        }

        public void setIcon(int imageId) {
            imageView_Icon.setImageResource(imageId);
        }

        public LinearLayout getItem() {
            return layout_Item;
        }
    }

}
