package wizrole.hoservice.activity;

import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.DiseaseName;

/**
 * Created by ${liushengping} on 2017/10/12.
 * 何人执笔？
 * 疾病知识，详细信息
 */

public class DiseaseDetailInforActivity extends BaseActivity {
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.dis_gs)TextView dis_gs;
    @BindView(R.id.dis_by)TextView dis_by;
    @BindView(R.id.dis_zz)TextView dis_zz;
    @BindView(R.id.dis_jc)TextView dis_jc;
    @BindView(R.id.dis_zl)TextView dis_zl;
    @BindView(R.id.dis_bfz)TextView dis_bfz;
    @BindView(R.id.dis_name)TextView dis_name;
    @BindView(R.id.dis_yf)TextView dis_yf;
    @BindView(R.id.dis_ys)TextView dis_ys;
    public DiseaseName name;
    @Override
    protected int getLayout() {
        return R.layout.activity_diseaedetailinfor;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.dis_knowledge));
        name=(DiseaseName) getIntent().getSerializableExtra("infor");
        if(name!=null) {
            dis_name.setText(name.getDiseaseDetailName());
            if(!name.getDisOverview().equals("")&&name.getDisOverview()!=null){
                dis_gs.setText(Html.fromHtml(name.getDisOverview()));
            }
            if(!name.getDisCause().equals("")&&name.getDisCause()!=null){
                dis_by.setText(Html.fromHtml(name.getDisCause()));
            }
            if(!name.getDisSymptom().equals("")&&name.getDisSymptom()!=null){
                dis_zz.setText(Html.fromHtml(name.getDisSymptom()));
            }
            if(!name.getDisCheck().equals("")&&name.getDisCheck()!=null){
                dis_jc.setText(Html.fromHtml(name.getDisCheck()));
            }
            if(!name.getDisTreatment().equals("")&&name.getDisTreatment()!=null){
                dis_zl.setText(Html.fromHtml(name.getDisTreatment()));
            }
            if(!name.getDisComplications().equals("")&&name.getDisComplications()!=null){
                dis_bfz.setText(Html.fromHtml(name.getDisComplications()));
            }
            if(!name.getDisProphylaxis().equals("")&&name.getDisProphylaxis()!=null){
                dis_yf.setText(Html.fromHtml(name.getDisProphylaxis()));
            }
            if(!name.getDisDiet().equals("")&&name.getDisDiet()!=null){
                dis_ys.setText(Html.fromHtml(name.getDisDiet()));
            }
        }
    }

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
