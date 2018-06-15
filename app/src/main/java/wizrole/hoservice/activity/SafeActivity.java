package wizrole.hoservice.activity;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.util.SharedPreferenceUtil;
import wizrole.hoservice.util.Tol_util;

/**
 * 安全设置
 * Created by a on 2017/4/18.
 */

public class SafeActivity extends BaseActivity {
    @BindView(R.id.tol_turn)ToggleButton tol_turn;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_mima)TextView text_mima;
    private boolean isCheck;
    public String mima;
    @Override
    protected int getLayout() {
        return R.layout.activity_safe;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.safe_center));
        mima= SharedPreferenceUtil.getMama(SafeActivity.this);
        if(mima.equals("")){
            text_mima.setText(getString(R.string.set_hand_mima));
        }else{
            text_mima.setText(getString(R.string.change_hand_mima));
        }
        //获取开关的状态--每次进入程序，获取保存的上次的状态
        boolean isOpen = Tol_util.get(this, Tol_util.OPEN_MESS,
                true);
        tol_turn.setChecked(isOpen);
    }

    @Override
    protected void setListener() {
        /**开关监听*/
        tol_turn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isClick) {
                if (isClick) {  //打开
                    //保存开光状态
                    Tol_util.set(SafeActivity.this, Tol_util.OPEN_MESS, isClick);
                } else {        //关闭
                    //关闭开关  保存状态
                    Tol_util.set(SafeActivity.this,Tol_util.OPEN_MESS,isClick);
                }
            }
        });
    }

    @OnClick({R.id.lin_back, R.id.lin_mima, R.id.lin_forget})
    public void onClick(View view){
        Intent intent=null;
        switch (view.getId()){
            case R.id.lin_back:
                finish();
                break;
            case R.id.lin_mima: //新建--修改手势密码
                if(mima.equals("")){ //新建密码
                    intent=new Intent(SafeActivity.this,CreateGestureActivity.class);
                    intent.putExtra("change","change");
                    startActivity(intent);
                }else{      //修改密码--先验证密码
                    intent=new Intent(SafeActivity.this,GestureLoginActivity.class);
                    intent.putExtra("yanzheng","change");//修改密码先验证 为change
                    startActivity(intent);
                }
                break;
            case R.id.lin_forget: //忘记密码
                intent=new Intent(SafeActivity.this,LoginActivity.class);
                intent.putExtra("forgetLogin","forgetLogin");//同样通过重新登录--清除保存的密码
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String m= SharedPreferenceUtil.getMama(SafeActivity.this);
        if(m.equals("")){
            text_mima.setText(getString(R.string.set_hand_mima));
        }else{
            text_mima.setText(getString(R.string.change_hand_mima));
        }
    }
}
