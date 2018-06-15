package wizrole.hoservice.ui;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import wizrole.hoservice.R;


/**
 * Created by a on 2017/6/13.
 */

public class Dialog_zhuxiao extends BaseDialog{

    public Dialog_zhuxiao(Context context, int themeResId ) {
        super(context, R.style.dialog);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_zhuxiao;
    }

    private OnDialogListener listener=null;

    public void setOnDialogListener(OnDialogListener listener){
        this.listener=listener;
    }

    public OnDialogListener getOnDialogListener(){
        return listener;
    }

    @Override
    protected void initUI() {
        TextView text_cancle = $(R.id.text_cancle);
        TextView text_sure = $(R.id.text_sure);
        text_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null)listener.Dialog(view);
            }
        });
        text_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null)listener.Dialog(view);
            }
        });
    }
}
