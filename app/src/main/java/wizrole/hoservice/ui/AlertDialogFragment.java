package wizrole.hoservice.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * 页面谈弹出框
 * Created by a on 2016/12/5.
 */

public class AlertDialogFragment extends DialogFragment {

    private static final String TITLE="title";
    private static final String MESSAGE="message";

    /**
     * 创建当前AlertDialogFragment的一个实例
     *
     * @param resTile 标题的字符串资源id
     * @param msg     描述信息
     * @return AlertDialogFragment对象
     */
    public static AlertDialogFragment newInstance(int resTile, String msg){
        AlertDialogFragment fragment=new AlertDialogFragment();
        Bundle args=new Bundle();
        args.putInt(TITLE,resTile);
        args.putString(MESSAGE,msg);
        fragment.setArguments(args);
        return  fragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title=getArguments().getInt(TITLE);
        String msg=getArguments().getString(MESSAGE);
        return  new AlertDialog.Builder(getActivity(),getTheme())
                .setTitle(title)
                .setMessage(msg)
                .setNeutralButton("确定",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        }).create();
    }
}
