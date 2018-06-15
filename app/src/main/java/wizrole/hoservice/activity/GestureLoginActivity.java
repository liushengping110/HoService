package wizrole.hoservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.star.lockpattern.widget.LockPatternView;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.util.SharedPreferenceUtil;
import wizrole.hoservice.util.cache.ACache;

import static com.star.lockpattern.util.LockPatternUtil.patternToHash;

/**
 * Created by Sym on 2015/12/24.
 */
public class GestureLoginActivity extends BaseActivity {

    private static final String TAG = "LoginGestureActivity";

    @BindView(R.id.lockPatternView) LockPatternView lockPatternView;
    @BindView(R.id.messageTv) TextView messageTv;
    @BindView(R.id.forgetGestureBtn) TextView forgetGestureBtn;

    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.lin_all)LinearLayout lin_all;

    private ACache aCache;
    private static final long DELAYTIME = 600l;
    private byte[] gesturePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesturelogin);
        ButterKnife.bind(this);
        this.init();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_gesturelogin;
    }
    public String yanzheng;//修改密码进入验证
    @Override
    protected void initData() {
        ButterKnife.bind(this);
        yanzheng=getIntent().getStringExtra("yanzheng");
        if(yanzheng.equals("yanzheng")){   //锁屏验证解锁
            lin_all.setVisibility(View.INVISIBLE);
        }else if(yanzheng.equals("change")){  //修改手势密码验证解锁
            lin_all.setVisibility(View.VISIBLE);
            text_title.setText(getString(R.string.yzssmm));
        }else if(yanzheng.equals("splash")){//自动登录--验证解锁
            lin_all.setVisibility(View.INVISIBLE);
        }
        init();
    }

    @Override
    protected void setListener() {
        forgetGestureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GestureLoginActivity.this,LoginActivity.class);
                intent.putExtra("newLogin","newLogin");
                startActivity(intent);
            }
        });
        lin_back.setOnClickListener(new View.OnClickListener() {//此处如果锁屏解锁后，隐藏--只在修改密码显示
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init() {
        aCache = ACache.get(GestureLoginActivity.this);
        //得到当前用户的手势密码
//        gesturePassword = aCache.getAsBinary(Constant.GESTURE_PASSWORD);
        String str= SharedPreferenceUtil.getMama(GestureLoginActivity.this);
        try {
            gesturePassword=str.getBytes("UTF-8");
            lockPatternView.setOnPatternListener(patternListener);
            updateStatus(Status.DEFAULT);
        }catch (UnsupportedEncodingException e){

        }
    }

    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if(pattern != null){
                byte[] bytes2 = patternToHash(pattern);
                try{
                    String str = new String(bytes2,"UTF-8");
                    byte[] temp=str.getBytes("UTF-8");
                    if(Arrays.equals(gesturePassword, temp)){
                        updateStatus(Status.CORRECT);
                    }else{
                        updateStatus(Status.ERROR);
                    }
                }catch (UnsupportedEncodingException e){

                }
                //Hash
//                if(LockPatternUtil.checkPattern(pattern, gesturePassword)) {
//                    updateStatus(Status.CORRECT);
//                } else {
//                    updateStatus(Status.ERROR);
//                }
            }
        }
    };

    /**
     * 更新状态
     * @param status
     */
    private void updateStatus(Status status) {
        messageTv.setText(status.strId);
        messageTv.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                loginGestureSuccess();
                break;
        }
    }

    /**
     * 手势成功
     */
    private void loginGestureSuccess() {
        MyToast("密码正确");
        if(yanzheng.equals("yanzheng")){   //锁屏验证解锁
            MyToast(getString(R.string.mmzq));
            finish();
        }else if(yanzheng.equals("change")){  //修改手势密码验证解锁
            MyToast(getString(R.string.new_set_mm));
            Intent intent=new Intent(GestureLoginActivity.this,CreateGestureActivity.class);
            intent.putExtra("change","change");
            startActivity(intent);
            finish();
        }else if(yanzheng.equals("splash")){//自动登录--验证解锁
            MyToast(getString(R.string.mmzq));
            if(SharedPreferenceUtil.getNotidic(GestureLoginActivity.this)==1) {//从通知栏进入
                Intent intent=new Intent(GestureLoginActivity.this,HosMessageActivity.class);//就去住院消息
                startActivity(intent);
                finish();
            }else{
                Intent intent=new Intent(GestureLoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, R.color.grey_a5a5a5),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.red_f4333c),
        //密码输入正确
        CORRECT(R.string.gesture_correct, R.color.grey_a5a5a5);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            if(yanzheng.equals("yanzheng")){   //锁屏验证解锁
                MyToast(getString(R.string.please_yzssmm));
                return false;
            }else if(yanzheng.equals("change")){  //修改手势密码验证解锁
                finish();
            }else if(yanzheng.equals("splash")){//自动登录--验证解锁
                MyToast(getString(R.string.please_yzssmm));
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
