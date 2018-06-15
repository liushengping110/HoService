package wizrole.hoservice.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;

import wizrole.hoservice.R;


/**
 * Created by Administrator on 2017/3/29.
 */

public abstract class BaseDialog extends Dialog {

    public BaseDialog(Context context, int themeResId  ) {
        super(context, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initUI();
    }

    private SparseArray<View> sparseArray = new SparseArray<>();

    protected <T extends View> T $(int rid) {
        if (sparseArray.get(rid) == null) {
            View view = findViewById(rid);
            sparseArray.append(rid, view);
            return (T) view;
        } else {
            return (T) sparseArray.get(rid);
        }
    }

    protected abstract int getLayout();

    protected abstract void initUI();

    public interface OnDialogListener {
        void Dialog(View view);
    }

    private OnDialogListener onDialogListener;

    public OnDialogListener getOnDialogListener() {
        return onDialogListener;
    }

    public void setOnDialogListener(OnDialogListener onDialogListener) {
        this.onDialogListener = onDialogListener;
    }
}