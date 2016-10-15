package pl.wozniakbartlomiej.receipt.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import pl.wozniakbartlomiej.receipt.R;
import pl.wozniakbartlomiej.receipt.Widgets.EventUsersFragment;

public class ItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft;


        ft = fm.beginTransaction();
        Fragment lu = new EventUsersFragment();
        ft.add(R.id.listUsers, lu);

        //work on dynamically creating textview onto fragment...


        ft.commit();

    }


    public void methodInEventUsersFragmentInterface(String str){

    }
}