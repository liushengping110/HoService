package wizrole.hoservice.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;

/**
 * Created by ${liushengping} on 2017/10/9.
 * 何人执笔？
 * 健康工具--智能导诊
 *
 * 飞华健康网----------http://test.fh21.com.cn/
 */

public class HealTestActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_all_body)TextView text_all_body;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.lin_left)LinearLayout lin_left;
    @BindView(R.id.lin_right)LinearLayout lin_right;
    @BindView(R.id.img_transt)ImageView img_transt;
    @BindView(R.id.img_body_font)ImageView img_body_font;
    @BindView(R.id.img_body_after)ImageView img_body_after;
    public LinearLayout.LayoutParams params;
    public boolean header_status=false;//前后标记位  false  前  true--后
    public String msg;
    @Override
    protected int getLayout() {
        return R.layout.activity_healtest;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.heal_tool));
        if(!header_status){
            text_all_body.setText(getString(R.string.header));
        }
        params=new LinearLayout.LayoutParams(120, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,20,0,20);
        setLeftView(true);
        setRightView(true);
    }

    public void setLeftView(boolean status_left){
        if(status_left){//设置【前左】
            String[] font_left=getResources().getStringArray(R.array.font_left);
            getLeftView(font_left);
        }else{//设置【后左】
            String[] after_left=getResources().getStringArray(R.array.after_left);
            getLeftView(after_left);
        }
    }
    public void setRightView(boolean status_right){
        if(status_right){//设置【前右】
            String[] font_right=getResources().getStringArray(R.array.font_right);
            getRightView(font_right);
        }else{//设置【后右】
            String[] after_right=getResources().getStringArray(R.array.after_right);
            getRightView(after_right);
        }
    }

    public void getLeftView(String[] str){
        for (int i=0;i<str.length;i++){
            TextView textView=new TextView(this);
            textView.setText(str[i]);
            textView.setMinHeight(40);
            textView.setMinWidth(60);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(params);
            textView.setBackgroundResource(R.drawable.yuanjiao_item_lock);
            textView.setOnClickListener(new MyTextViewListener(i,"left"));
            lin_left.addView(textView);
        }
    }

    public void getRightView(String[] str){
        for (int i=0;i<str.length;i++){
            TextView textView=new TextView(this);
            textView.setText(str[i]);
            textView.setMinHeight(40);
            textView.setMinWidth(60);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(params);
            textView.setBackgroundResource(R.drawable.yuanjiao_item_lock);
            textView.setOnClickListener(new MyTextViewListener(i,"right"));
            lin_right.addView(textView);
        }
    }

    class MyTextViewListener implements View.OnClickListener{
        public int position;
        public String type;
        public MyTextViewListener(int position,String type){
            this.position=position;
            this.type=type;
        }
        @Override
        public void onClick(View v) {
            if(type.equals("right")){
                TextView textView=(TextView) lin_right.getChildAt(position);
                msg=textView.getText().toString();
            }else{
                TextView textView=(TextView) lin_left.getChildAt(position);
                msg=textView.getText().toString();
            }
            Intent intent=new Intent(HealTestActivity.this,TestSelfListActivity.class);
            intent.putExtra("TestSelType",msg);
            startActivity(intent);
        }
    }
    @Override
    protected void setListener() {
        lin_back.setOnClickListener(this);
        img_transt.setOnClickListener(this);
        text_all_body.setOnClickListener(this);
    }

    public Intent intent;
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.lin_back://返回
                finish();
                break;
            case R.id.img_transt://前后---------转换
                lin_right.removeAllViews();
                lin_left.removeAllViews();
                if(!header_status){//后
                    img_body_font.setVisibility(View.INVISIBLE);
                    img_body_after.setVisibility(View.VISIBLE);
                    header_status=true;
                    text_all_body.setText(getString(R.string.all_body));
                    setLeftView(false);
                    setRightView(false);
                }else{//前
                    header_status=false;
                    text_all_body.setText(getString(R.string.header));
                    img_body_font.setVisibility(View.VISIBLE);
                    img_body_after.setVisibility(View.INVISIBLE);
                    setLeftView(true);
                    setRightView(true);
                }
                break;
            case R.id.text_all_body://全身--头
                Intent intent=new Intent(HealTestActivity.this,TestSelfListActivity.class);
                intent.putExtra("TestSelType",text_all_body.getText().toString());
                startActivity(intent);
                break;
        }
    }
}
