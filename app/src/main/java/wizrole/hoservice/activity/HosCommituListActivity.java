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
 * Created by 何人执笔？ on 2018/4/12.
 * liushengping
 * 医患交流列表页面
 */

public class HosCommituListActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_li)LinearLayout lin_li;
    @BindView(R.id.lin_wang)LinearLayout lin_wang;
    @BindView(R.id.lin_zhang)LinearLayout lin_zhang;
    @BindView(R.id.lin_liu)LinearLayout lin_liu;
    @Override
    protected int getLayout() {
        return R.layout.activity_hoscommitulist;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("医护列表");
    }

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(this);
        lin_li.setOnClickListener(this);
        lin_wang.setOnClickListener(this);
        lin_zhang.setOnClickListener(this);
        lin_liu.setOnClickListener(this);
    }

    public Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back:
                finish();
                break;
            case R.id.lin_li:
                intent=new Intent(HosCommituListActivity.this,HosCommitSingleActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                break;
            case R.id.lin_wang:
                intent=new Intent(HosCommituListActivity.this,HosCommitSingleActivity.class);
                intent.putExtra("type","2");
                startActivity(intent);
                break;
            case R.id.lin_zhang:
                intent=new Intent(HosCommituListActivity.this,HosCommitSingleActivity.class);
                intent.putExtra("type","3");
                startActivity(intent);
                break;
            case R.id.lin_liu:
                intent=new Intent(HosCommituListActivity.this,HosCommitSingleActivity.class);
                intent.putExtra("type","4");
                startActivity(intent);
                break;
        }
    }
}
