package wizrole.hoservice.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;

/**
 * Created by ${liushengping} on 2017/10/9.
 * 何人执笔？
 * 健康工具
 *
 * 飞华健康网----------http://test.fh21.com.cn/
 */

public class HealToolActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.lin_daozhen)LinearLayout lin_daozhen;

    @Override
    protected int getLayout() {
        return R.layout.activity_healtool;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.heal_gongju));
    }
    @Override
    protected void setListener() {
        lin_back.setOnClickListener(this);
        lin_daozhen.setOnClickListener(this);
    }

    public Intent intent;
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.lin_back://返回
                finish();
                break;
            case R.id.lin_daozhen://智能导诊
                intent=new Intent(HealToolActivity.this,HealTestActivity.class);
                startActivity(intent);
                break;
        }
    }
}
