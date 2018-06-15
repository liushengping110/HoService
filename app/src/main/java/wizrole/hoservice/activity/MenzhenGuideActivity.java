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
 * Created by a on 2017/8/30.
 * 门诊须知
 * ====门诊流程
 * =====门诊预约挂号须知
 */

public class MenzhenGuideActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_mzgh)LinearLayout lin_mzgh;
    @BindView(R.id.lin_mzlc)LinearLayout lin_mzlc;

    @Override
    protected int getLayout() {
        return R.layout.activity_menzhen;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.mzxz));
    }

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //预约挂号须知
        lin_mzgh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenzhenGuideActivity.this,MenzhenGhActivity.class);
                startActivity(intent);
            }
        });
        //门诊流程
        lin_mzlc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenzhenGuideActivity.this,MenzhenLcActivity.class);
                startActivity(intent);
            }
        });
    }
}
