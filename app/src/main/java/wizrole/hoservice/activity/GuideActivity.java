package wizrole.hoservice.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;

/**
 * Created by a on 2017/8/29.
 * 就医指南
 */

public class GuideActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @Override
    protected int getLayout() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.hos_guide));
    }

    @Override
    protected void setListener() {

    }

    public Intent intent;
    @OnClick({R.id.lin_back,R.id.lin_yibao,R.id.lin_menzhen})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_back://返回
                finish();
                break;
            case R.id.lin_menzhen://门诊
                intent=new Intent(this,MenzhenGuideActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_yibao://医保
                intent=new Intent(this,YiBaoGuideActivity.class);
                startActivity(intent);
                break;
        }
    }
}
