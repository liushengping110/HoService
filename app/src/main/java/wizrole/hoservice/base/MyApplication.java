package wizrole.hoservice.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by 何人执笔？ on 2018/4/10.
 * liushengping
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

    //返回
    public static Context getContextObject(){
        return context;
    }
}
