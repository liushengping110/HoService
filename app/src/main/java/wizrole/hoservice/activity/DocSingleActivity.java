package wizrole.hoservice.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.DoctorInfos;
import wizrole.hoservice.util.ImageLoading;

/**
 * Created by a on 2017/9/11.
 * 医生介绍详细信息
 */

public class DocSingleActivity extends BaseActivity {

    @BindView(R.id.doc_intr_content)TextView doc_intr_content;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_intr_vistime)TextView text_intr_vistime;
    @BindView(R.id.text_intr_position)TextView text_intr_position;
    @BindView(R.id.text_intr_school)TextView text_intr_school;
    @BindView(R.id.text_intr_deparent)TextView text_intr_deparent;
    @BindView(R.id.text_intr_title)TextView text_intr_title;
    @BindView(R.id.text_intr_name)TextView text_intr_name;
    @BindView(R.id.text_intr_img)ImageView text_intr_img;
    @BindView(R.id.lin_back)LinearLayout lin_back;

    public DoctorInfos doctorInfos;
    @Override
    protected int getLayout() {
        return R.layout.activity_docsingle;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.doc_center));
        doctorInfos=(DoctorInfos) getIntent().getSerializableExtra("intrduce");
        if(doctorInfos.getDocHeadImage()!=null){
            String waiWangIP="61.177.223.85";
            String neiWangIP="172.16.1.159";
            String url=doctorInfos.getDocHeadImage().replaceAll(neiWangIP,waiWangIP);
            ImageLoading.common(this,url,text_intr_img,R.drawable.per_infor);
        }
        if(doctorInfos.getDocName()!=null){
            text_intr_name.setText(doctorInfos.getDocName());
        }
        if(doctorInfos.getPosition()!=null){
            text_intr_position.setText(doctorInfos.getPosition());
        }
        if(doctorInfos.getRecordSchool()!=null){
            text_intr_school.setText(doctorInfos.getRecordSchool());
        }
        if(doctorInfos.getDepartment()!=null){
            text_intr_deparent.setText(doctorInfos.getDepartment());
        }else if(doctorInfos.getTitle()!=null){
            text_intr_title.setText(doctorInfos.getTitle());
        }
        if(doctorInfos.getVisitTime()!=null){
            text_intr_vistime.setText(doctorInfos.getVisitTime());
        }
        if(doctorInfos.getIntrduce()!=null){
            doc_intr_content.setText(doctorInfos.getIntrduce());
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
