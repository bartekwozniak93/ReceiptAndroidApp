package pl.wozniakbartlomiej.receipt.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Widgets.ReceiptsFragment;

public class ReceiptsActivity extends AppCompatActivity {

    private String eventId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId=getIntent().getExtras().getString("eventId");
        setContentView(R.layout.activity_receipts);
        addReceiptsFragment();
    }

    /**
     * Check if there's already ReceiptsFragment added.
     * If not, add.
     */
    private void addReceiptsFragment(){
        if (findViewById(R.id.frameReceiptsLayout) != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment receiptsFragment = new ReceiptsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("eventId", eventId);
            receiptsFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.frameReceiptsLayout, receiptsFragment);
            fragmentTransaction.commit();
        }
    }
}
