package wizrole.hoservice.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import wizrole.hoservice.R;

/**
 * Created by a on 2017/8/15.
 */

public class ToastUtil {

    public static void MyToast(Context context, String message){
        if(context!=null){
            Toast toast=null;
            View view = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
            TextView textView=(TextView)view.findViewById(R.id.text_toast);
            textView.setText(message);
            toast=new Toast(context);
            toast.setGravity( Gravity.CENTER_VERTICAL  , 0, 700);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
        }
    }
}
