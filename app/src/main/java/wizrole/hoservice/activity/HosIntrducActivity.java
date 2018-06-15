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
 * Created by a on 2017/8/25.
 * 医院概况
 */

public class HosIntrducActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.lin_intr_hosintr)LinearLayout lin_intr_hosintr;
    @BindView(R.id.lin_intr_docintr)LinearLayout lin_intr_docintr;
    @BindView(R.id.lin_intr_deparintr)LinearLayout lin_intr_deparintr;
    @BindView(R.id.text_title)TextView text_title;

    @Override
    protected int getLayout() {
        return R.layout.activity_hosintrduce;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.hos_gk));
    }

    @Override
    protected void setListener() {

    }

    public Intent intent;
    @OnClick({R.id.lin_back,R.id.lin_intr_hosintr,R.id.lin_intr_deparintr,R.id.lin_intr_docintr})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_back:
                finish();
                break;
            case R.id.lin_intr_hosintr://医院介绍
                intent=new Intent(this,HosIntrSinglActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_intr_deparintr://科室介绍
                intent=new Intent(this,DeparentActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_intr_docintr://医生介绍
                intent=new Intent(this,DocIntrduceActivity.class);
                intent.putExtra("name","");
                startActivity(intent);
                break;
        }
    }
}
