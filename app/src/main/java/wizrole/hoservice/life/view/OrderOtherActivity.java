package wizrole.hoservice.life.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;

/**
 * Created by a on 2017/5/2.
 */

public class OrderOtherActivity extends BaseActivity{

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_no_spicy)TextView text_no_spicy;//不要辣
    @BindView(R.id.text_little_spicy)TextView text_little_spicy;//少辣
    @BindView(R.id.text_more_spicy)TextView text_more_spicy;//多辣
    @BindView(R.id.text_parsley)TextView text_parsley;//不要香菜
    @BindView(R.id.text_Onions)TextView text_Onions;//不要洋葱
    @BindView(R.id.text_vinegar)TextView text_vinegar;//多醋
    @BindView(R.id.text_onion)TextView text_onion;//多葱
    @BindView(R.id.text_textnum)TextView text_textnum;//多葱
    @BindView(R.id.edit_other)EditText edit_other;//其他备注
    @BindView(R.id.btn_other_sure)Button btn_other_sure;//确定

    @Override
    protected int getLayout() {
        return R.layout.activity_orderother;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_textnum.setText(0+"/"+50);
        text_title.setText(getString(R.string.order_bz));
    }

    @Override
    protected void setListener() {
        edit_other.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String content = edit_other.getText().toString();
                text_textnum.setText(content.length() + "/"
                        + 50);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(edit_other.getText().toString().length()>=50){
                    ToastShow(getString(R.string.order_bznum));
                }
            }
        });
    }

    public boolean no_spicy=false;//不辣
    public boolean little_spicy=false;//少辣
    public boolean more_spicy=false;//多辣
    public boolean parsley=false;//香菜
    public boolean Onions=false;//洋葱
    public boolean vinegar=false;//醋
    public boolean onion=false;//葱
    @OnClick({R.id.lin_back,R.id.btn_other_sure,R.id.text_no_spicy,R.id.text_little_spicy,R.id.text_more_spicy,
    R.id.text_parsley,R.id.text_Onions,R.id.text_vinegar,R.id.text_onion})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_back:
                finish();
                break;
            case R.id.text_no_spicy://不要辣
                if(!no_spicy){
                    no_spicy=true;
                    little_spicy=false;
                    more_spicy=false;
                    text_no_spicy.setBackgroundResource(R.drawable.yuanjiao_lock);
                    text_little_spicy.setBackgroundResource(R.drawable.yuanjiao);
                    text_more_spicy.setBackgroundResource(R.drawable.yuanjiao);
                }else{  //如果选中状态下 继续点击 则取消
                    no_spicy=false;
                    little_spicy=false;
                    more_spicy=false;
                    text_no_spicy.setBackgroundResource(R.drawable.yuanjiao);
                    text_little_spicy.setBackgroundResource(R.drawable.yuanjiao);
                    text_more_spicy.setBackgroundResource(R.drawable.yuanjiao);
                }
                break;
            case R.id.text_little_spicy://少辣
                if(!little_spicy){
                    little_spicy=true;
                    no_spicy=false;
                    more_spicy=false;
                    text_no_spicy.setBackgroundResource(R.drawable.yuanjiao);
                    text_little_spicy.setBackgroundResource(R.drawable.yuanjiao_lock);
                    text_more_spicy.setBackgroundResource(R.drawable.yuanjiao);
                }else{  //如果选中状态下 继续点击 则取消
                    no_spicy=false;
                    little_spicy=false;
                    more_spicy=false;
                    text_no_spicy.setBackgroundResource(R.drawable.yuanjiao);
                    text_little_spicy.setBackgroundResource(R.drawable.yuanjiao);
                    text_more_spicy.setBackgroundResource(R.drawable.yuanjiao);
                }
                break;
            case R.id.text_more_spicy://多辣
                if(!more_spicy){
                    more_spicy=true;
                    no_spicy=false;
                    little_spicy=false;
                    text_no_spicy.setBackgroundResource(R.drawable.yuanjiao);
                    text_little_spicy.setBackgroundResource(R.drawable.yuanjiao);
                    text_more_spicy.setBackgroundResource(R.drawable.yuanjiao_lock);
                }else{  //如果选中状态下 继续点击 则取消
                    no_spicy=false;
                    little_spicy=false;
                    more_spicy=false;
                    text_no_spicy.setBackgroundResource(R.drawable.yuanjiao);
                    text_little_spicy.setBackgroundResource(R.drawable.yuanjiao);
                    text_more_spicy.setBackgroundResource(R.drawable.yuanjiao);
                }
                break;
            case R.id.text_parsley:
                if(!parsley){
                    parsley=true;
                    text_parsley.setBackgroundResource(R.drawable.yuanjiao_lock);
                }else{
                    text_parsley.setBackgroundResource(R.drawable.yuanjiao);
                    parsley=false;
                }
                break;
            case R.id.text_Onions:
                if(!Onions){
                    Onions=true;
                    text_Onions.setBackgroundResource(R.drawable.yuanjiao_lock);
                }else{
                    text_Onions.setBackgroundResource(R.drawable.yuanjiao);
                    Onions=false;
                }
                break;
            case R.id.text_vinegar:
                if(!vinegar){
                    vinegar=true;
                    text_vinegar.setBackgroundResource(R.drawable.yuanjiao_lock);
                }else{
                    text_vinegar.setBackgroundResource(R.drawable.yuanjiao);
                    vinegar=false;
                }
                break;
            case R.id.text_onion:
                if(!onion){
                    onion=true;
                    text_onion.setBackgroundResource(R.drawable.yuanjiao_lock);
                }else{
                    text_onion.setBackgroundResource(R.drawable.yuanjiao);
                    onion=false;
                }
                break;
            case R.id.btn_other_sure:
                finish();
                break;
        }
    }
}
