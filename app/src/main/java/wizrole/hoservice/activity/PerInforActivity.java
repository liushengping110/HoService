package wizrole.hoservice.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.PersonInfor;
import wizrole.hoservice.util.SharedPreferenceUtil;

/**个人信息页面
 * Created by a on 2016/11/22.
 *
 * 根据接口获取后台服务器数据  进行个人信息展示
 */

public class PerInforActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_per_name)TextView text_per_name;    //姓名
    @BindView(R.id.text_per_sex)TextView text_per_sex;  //性别
    @BindView(R.id.text_per_date)TextView text_per_date;    //出生日期
    @BindView(R.id.text_hos_num)TextView text_hos_num;  //入院号
    @BindView(R.id.text_per_tel)TextView text_per_tel;  //电话
    @BindView(R.id.text_per_docMedUnit)TextView text_per_docMedUnit;    //医疗单元
    @BindView(R.id.text_per_diagnosis)TextView text_per_diagnosis;      //诊断
    @BindView(R.id.text_per_ward)TextView text_per_ward;    //病区
    @BindView(R.id.text_per_fr)TextView text_per_fr;    //余额
    @BindView(R.id.text_per_Indate)TextView text_per_Indate;    //入院日期
    @BindView(R.id.text_per_hos_jz)TextView text_per_hos_jz;    //就诊号
    @BindView(R.id.text_per_ctloc)TextView text_per_ctloc;  //CtLoc
    public PersonInfor person;
    @Override
    protected int getLayout() {
        return R.layout.activity_perinfor;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        person=(PersonInfor) SharedPreferenceUtil.getBean(this);
        text_title.setText(getString(R.string.person_infor));
        setData();
    }

    @Override
    protected void setListener() {

    }

    /**设置数据*/
        public void setData(){
            if(person!=null){
                text_per_name.setText(person.getName());
                text_per_sex.setText(person.getSex());
                text_per_date.setText(person.getDOB());
                text_hos_num.setText(person.getZyno());
                text_per_tel.setText(person.getTelNo());
                text_per_docMedUnit.setText(person.getDocMedUnit());
                text_per_diagnosis.setText(person.getDiagnose());
                text_per_ward.setText(person.getWard());
                text_per_fr.setText(person.getFeeSelf());
                text_per_Indate.setText(person.getAdmDate());
                text_per_hos_jz.setText(person.getAdmNo());
                text_per_ctloc.setText(person.getCtLoc());
            }else{
                MyToast(getString(R.string.base_infor_fail));
                finish();
            }
        }

    /**返回监听*/
    @OnClick(R.id.lin_back)
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.lin_back:
                finish();
                break;
        }
    }
}

