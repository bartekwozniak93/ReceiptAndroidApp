package pl.wozniakbartlomiej.receipt;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Bartek on 19/10/16.
 */

public class Receipt extends Application {
    private static Context context;

    public static Resources getResourcesStatic() {
        return context.getResources();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

}