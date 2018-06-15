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
 * Created by a on 2017/9/4.
 * 院内导航
 */

public class HosnavigaActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;

    @Override
    protected int getLayout() {
        return R.layout.activity_hosnavigation;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.hos_in_guide));
    }

    @Override
    protected void setListener() {

    }

    public Intent intent;
    @OnClick({R.id.lin_back,R.id.lin_jianzhu,R.id.lin_keshi})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_back:
                finish();
                break;
            case R.id.lin_jianzhu://建筑分布  楼层索引
                intent=new Intent(this,BulidMapActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_keshi://门诊科室
                intent=new Intent(this,MenzhenMapActivity.class);
                startActivity(intent);
                break;
        }
    }
}
