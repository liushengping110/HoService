package wizrole.hoservice.activity;

import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.TestSelfCateDetail;

/**
 * Created by liushengping on 2017/11/9/009.
 * 何人执笔？
 * 智能导诊--详情页面
 */

public class TestSelfDetailInfoActivity extends BaseActivity {
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.test_bfz)TextView test_bfz;
    @BindView(R.id.test_zl)TextView test_zl;
    @BindView(R.id.test_jc)TextView test_jc;
    @BindView(R.id.test_zz)TextView test_zz;
    @BindView(R.id.test_by)TextView test_by;
    @BindView(R.id.test_gs)TextView test_gs;
    @BindView(R.id.test_name)TextView test_name;
    @Override
    protected int getLayout() {
        return R.layout.activity_testselfdetailinfor;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.xmxq));
        TestSelfCateDetail detail=(TestSelfCateDetail)getIntent().getSerializableExtra("TestSelfDetail");

        if(!detail.getTestCateDetailName().equals("")&&detail.getTestCateDetailName()!=null){
            test_name.setText(Html.fromHtml(detail.getTestCateDetailName()));
        }
        if(!detail.getTestCateDetailIntrduce().equals("")&&detail.getTestCateDetailIntrduce()!=null){
            test_gs.setText(Html.fromHtml(detail.getTestCateDetailIntrduce()));
        }
        if(!detail.getTestCateDetailCause().equals("")&&detail.getTestCateDetailCause()!=null){
            test_by.setText(Html.fromHtml(detail.getTestCateDetailCause()));
        }
        if(!detail.getTestCateDetailIdentify().equals("")&&detail.getTestCateDetailIdentify()!=null){
            test_zz.setText(Html.fromHtml(detail.getTestCateDetailIdentify()));
        }
        if(!detail.getTestCateDetailProphylaxis().equals("")&&detail.getTestCateDetailProphylaxis()!=null){
            test_jc.setText(Html.fromHtml(detail.getTestCateDetailProphylaxis()));
        }
        if(!detail.getTestCateDetailDiet().equals("")&&detail.getTestCateDetailDiet()!=null){
            test_zl.setText(Html.fromHtml(detail.getTestCateDetailDiet()));
        }
        if(!detail.getTestCateDetailTreatment().equals("")&&detail.getTestCateDetailTreatment()!=null){
            test_bfz.setText(Html.fromHtml(detail.getTestCateDetailTreatment()));
        }
    }

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
