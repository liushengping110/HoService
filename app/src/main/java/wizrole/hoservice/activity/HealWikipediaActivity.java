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
 * Created by ${liushengping} on 2017/9/27.
 * 何人执笔？
 * 健康百科
 * ---健康教育
 * ---健康视频
 * ---中成药
 * ---疾病知识
 * ---疾病库
 * ---读懂检验单
 */

public class HealWikipediaActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.lin_edu)LinearLayout lin_edu;
    @BindView(R.id.lin_vedio)LinearLayout lin_vedio;
    @BindView(R.id.lin_jbzs)LinearLayout lin_jbzs;
    @BindView(R.id.lin_xmed)LinearLayout lin_xmed;
    @BindView(R.id.lin_sszs)LinearLayout lin_sszs;
    @BindView(R.id.lin_jyd)LinearLayout lin_jyd;
    @Override
    protected int getLayout() {
        return R.layout.activity_healwikiped;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.heal_baike));
    }

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(this);
        lin_edu.setOnClickListener(this);
        lin_vedio.setOnClickListener(this);
        lin_jbzs.setOnClickListener(this);
        lin_xmed.setOnClickListener(this);
        lin_sszs.setOnClickListener(this);
        lin_jyd.setOnClickListener(this);
    }

    public Intent  intent;
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_back://返回
                finish();
                break;
            case R.id.lin_edu://健康教育
                intent=new Intent(this, HealEduActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_vedio://健康视频
                intent=new Intent(this, HealVedioActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_jbzs://疾病库
                intent=new Intent(this,DiseaseActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_xmed://西药
                intent=new Intent(this,WestMedActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_sszs://手术知识
                intent=new Intent(this,SurgeryActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_jyd://化验检查
                intent=new Intent(this,LaboratoryCheckActivity.class);
                startActivity(intent);
                break;
        }
    }
}
