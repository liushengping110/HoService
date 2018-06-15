package wizrole.hoservice.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import wizrole.hoservice.R;
import wizrole.hoservice.interface_base.Pop_DissListener;


/**
 * Created by Administrator on 2017/3/21.
 */

public abstract class PopupWindowPotting {

    private View view;
    private Activity activity;
    private PopupWindow popupWindow;
    public int type;
    public Pop_DissListener pop_dissListener;


    public PopupWindowPotting(Activity activity,int type) {
        this.activity = activity;
        this.type=type;
        if(type==2){//科室选择，点击接口

        }else if(type==3){//商家列表--弹窗
            pop_dissListener=(Pop_DissListener)activity;
        }else if(type==4){//医患交流

        }
        onCreate(0, 0);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    protected void onCreate(int width, int height) {
        WindowManager manager=activity.getWindowManager();
        int wid_final=manager.getDefaultDisplay().getWidth();
        if (width == 0) width = WindowManager.LayoutParams.MATCH_PARENT;
        if (height == 0) height = WindowManager.LayoutParams.WRAP_CONTENT;
        if (view == null) view = LayoutInflater.from(activity).inflate(getLayout(), null);
        if(type==1){//头像上传pop
            popupWindow = new PopupWindow(view, wid_final-80, height);
            popupWindow.setFocusable(true);
        }else if(type==2){//全部科室选择
            popupWindow= new PopupWindow(view,LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(false);
        }else if(type==3){//商家列表 二级分类
            popupWindow= new PopupWindow(view,LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
        }else  if(type==4){//医患交流
            popupWindow = new PopupWindow(view, wid_final, height);
            popupWindow.setFocusable(true);
        }
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(type==2){//科室选择

                }else if(type==3){//商家列表 二级分类
                    pop_dissListener.onDiss();
                }
                Hide();
            }
        });
        initUI();
        setListener();
    }

    public void Show(View view) {
        if(type==1){//头像上传
            WindowManager.LayoutParams lp = (activity).getWindow().getAttributes();
            lp.alpha = 0.6f;
            (activity).getWindow().setAttributes(lp);
            popupWindow.setAnimationStyle(R.style.PopupAnimation);
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }else if(type==2){//全部科室选择
            popupWindow.showAsDropDown(view, 0,-20);
        }else if(type==3){//商家列表 二级分类
            popupWindow.showAsDropDown(view,0,0);
        }else if(type==4){//医患交流
            popupWindow.setAnimationStyle(R.style.PopupAnimation);
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    }

    public void Hide() {
        WindowManager.LayoutParams lp = (activity).getWindow().getAttributes();
        lp.alpha = 1f;
        (activity).getWindow().setAttributes(lp);
        popupWindow.dismiss();
    }

    protected abstract int getLayout();

    protected abstract void initUI();

    protected abstract void setListener();

    private SparseArray<View> sparseArray = new SparseArray<>();

    protected <T extends View> T $(int rid) {
        if (sparseArray.get(rid) == null) {
            View viewgrounp = view.findViewById(rid);
            sparseArray.append(rid, viewgrounp);
            return (T) viewgrounp;
        } else {
            return (T) sparseArray.get(rid);
        }
    }

    private Intent intent;
    private Toast toast;

    protected void Skip(Class cla) {
        intent = new Intent(activity, cla);
        activity.startActivity(intent);
    }

    protected void ToastShow(String result) {
        if (toast == null) {
            toast = Toast.makeText(activity, result, Toast.LENGTH_SHORT);
        } else {
            toast.setText(result);
        }
        toast.show();
    }
}