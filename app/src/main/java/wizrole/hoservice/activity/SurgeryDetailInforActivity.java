package wizrole.hoservice.activity;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.SurgeryDetailInfor;
import wizrole.hoservice.util.ImageLoading;

/**
 * Created by ${liushengping} on 2017/10/18.
 * 何人执笔？
 * 手术详细信息
 */

public class SurgeryDetailInforActivity extends BaseActivity {

    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.SurgeryEffect)LinearLayout SurgeryEffect;//手术效果
    @BindView(R.id.SurgeryAfterHandling)LinearLayout SurgeryAfterHandling;//手术后处理
    @BindView(R.id.SurgeryAnesthesia)LinearLayout SurgeryAnesthesia;//麻醉方法
    @BindView(R.id.SurgeryName)TextView SurgeryName;//手术名称
    @BindView(R.id.SurgeryIndications)LinearLayout SurgeryIndications;//适应症
    @BindView(R.id.SurgeryContraindications)LinearLayout SurgeryContraindications;//禁忌症
    @BindView(R.id.SurgeryConsiderations)TextView SurgeryConsiderations;//手术后注意事项
    @BindView(R.id.SurgeryAnesthesiaTaboo)TextView SurgeryAnesthesiaTaboo;//麻醉禁忌
    public  LinearLayout.LayoutParams params;
    public  LinearLayout.LayoutParams params_img;


    @Override
    protected int getLayout() {
        return R.layout.activity_surgerydetailinfor;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("详细信息");
        params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params_img=new LinearLayout.LayoutParams(600, 600);
        SurgeryDetailInfor infor=(SurgeryDetailInfor)getIntent().getSerializableExtra("surgeryDetail");
        //手术名称
        if(infor.getSurgeryName()!=null&&!infor.getSurgeryName().equals("")){
            SurgeryName.setText(Html.fromHtml(infor.getSurgeryName()));
        }
        //手术效果
        if(infor.getSurgeryEffect()!=null&&!infor.getSurgeryEffect().equals("")){
            String str=infor.getSurgeryEffect();
            if(str.indexOf("<img") != -1){//包含图片
                setView(str,SurgeryEffect);
            }else{//不包含，直接设置view
                TextView textView=new TextView(this);
                textView.setText(Html.fromHtml(str));
                textView.setLayoutParams(params);
                textView.setTextSize(22);
                SurgeryEffect.addView(textView);
            }
        }
        //禁忌症
        if(infor.getSurgeryContraindications()!=null&&!infor.getSurgeryContraindications().equals("")){
            String str=infor.getSurgeryContraindications();
            if(str.indexOf("<img") != -1){//包含图片
                setView(str,SurgeryContraindications);
            }else{//不包含，直接设置view
                TextView textView=new TextView(this);
                textView.setText(Html.fromHtml(str));
                textView.setLayoutParams(params);
                textView.setTextSize(22);
                SurgeryContraindications.addView(textView);
            }
        }
        //适应症
        if(infor.getSurgeryIndications()!=null&&!infor.getSurgeryIndications().equals("")){
            String str=infor.getSurgeryIndications();
            if(str.indexOf("<img") != -1){//包含图片
                setView(str,SurgeryIndications);
            }else{//不包含，直接设置view
                TextView textView=new TextView(this);
                textView.setText(Html.fromHtml(str));
                textView.setLayoutParams(params);
                textView.setTextSize(22);
                SurgeryIndications.addView(textView);
            }
        }
        //麻醉方法
        if(infor.getSurgeryAnesthesia()!=null&&!infor.getSurgeryAnesthesia().equals("")){
            String str=infor.getSurgeryAnesthesia();
            if(str.indexOf("<img") != -1){//包含图片
                setView(str,SurgeryAnesthesia);
            }else{//不包含，直接设置view
                TextView textView=new TextView(this);
                textView.setText(Html.fromHtml(str));
                textView.setLayoutParams(params);
                textView.setTextSize(22);
                SurgeryAnesthesia.addView(textView);
            }
        }
        //麻醉禁忌
        if(infor.getSurgeryAnesthesiaTaboo()!=null&&!infor.getSurgeryAnesthesiaTaboo().equals("")){
            SurgeryAnesthesiaTaboo.setText(Html.fromHtml(infor.getSurgeryAnesthesiaTaboo()));
        }
        //手术中注意事项
        if(infor.getSurgeryConsiderations()!=null&&!infor.getSurgeryConsiderations().equals("")){
            SurgeryConsiderations.setText(Html.fromHtml(infor.getSurgeryConsiderations()));
        }
        //手术后处理
        if(infor.getSurgeryAfterHandling()!=null&&!infor.getSurgeryAfterHandling().equals("")){
            String str=infor.getSurgeryAfterHandling();
            if(str.indexOf("<img") != -1){//包含图片
                setView(str,SurgeryAfterHandling);
            }else{//不包含，直接设置view
                TextView textView=new TextView(this);
                textView.setText(Html.fromHtml(str));
                textView.setLayoutParams(params);
                textView.setTextSize(22);
                SurgeryAfterHandling.addView(textView);
            }
        }
    }

    /**
     * 图文混排
     * @param content
     * @param linearLayout
     */
    public void setView(String content,LinearLayout linearLayout){
        List<String> list = new ArrayList<String>();
        String[] s1 = content.split("<img");
        if(!s1[0].equals("")){
            list.add(s1[0]);
        }else{

        }
        for (int i = 1; i < s1.length; i++) {
            String s2 = s1[i].split("img>")[0];
            list.add(s2.trim());
            if (s1[i].split("img>").length != 1) {
                String split = s1[i].split("img>")[1];
                list.add(split.trim());
            }
        }
        for(int j=0;j<list.size();j++){
            String msg=list.get(j);
            if(msg.substring(0,1).equals("/")){//如果是图片
                msg= Constant.ip+msg;//添加ip
                ImageView imageView=new ImageView(this);
                ImageLoading.common(this,msg,imageView,R.drawable.img_loadfail);
                imageView.setLayoutParams(params_img);
                imageView.setPadding(0,20,0,20);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setOnClickListener(new Listener(msg));
                linearLayout.addView(imageView);
            }else{//否则是文本
                TextView textView=new TextView(this);
                textView.setText(Html.fromHtml(msg));
                textView.setLayoutParams(params);
                textView.setTextSize(22);
                linearLayout.addView(textView);
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

    /**
     * 图片点击监听
     */
    class Listener implements View.OnClickListener{
        public String url;
        public Listener(String url){
            this.url=url;
        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(SurgeryDetailInforActivity.this,ImageLookActivity.class);
            intent.putExtra("url",url);
            startActivity(intent);
        }
    }
}
