package wizrole.hoservice.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.LabCheckName;

/**
 * Created by ${liushengping} on 2017/10/13.
 * 何人执笔？
 * 化验检查详细页面
 */

public class LabCheckDetailInforActivity extends BaseActivity {
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.labcheck_lcyy)TextView labcheck_lcyy;
    @BindView(R.id.labcheck_name)TextView labcheck_name;
    @BindView(R.id.labcheck_normal)TextView labcheck_normal;

    @Override
    protected int getLayout() {
        return R.layout.activity_labcheckdetailinfor;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.hyjcxm));
        LabCheckName name=(LabCheckName)getIntent().getSerializableExtra("infor");
        if(!name.getLabCheckName().equals("")){
            labcheck_name.setText(name.getLabCheckName());
        }
        if (!name.getLabCheckClinicalSignificance().equals("")){
            labcheck_lcyy.setText(name.getLabCheckClinicalSignificance());
        }
        if(!name.getLabCheckNormal().equals("")){
            labcheck_normal.setText(name.getLabCheckNormal());
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
