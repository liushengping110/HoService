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
import wizrole.hoservice.beam.HealEduDatail;
import wizrole.hoservice.util.ImageLoading;

/**
 * Created by ${liushengping} on 2017/9/28.
 * 何人执笔？
 * 健康教育详情页面
 */
public class HealEduDetailActivity extends BaseActivity {
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_healedu_title)TextView text_healedu_title;
    @BindView(R.id.text_healedu_content)LinearLayout text_healedu_content;
    public HealEduDatail datail;
    public  LinearLayout.LayoutParams params;
    public  LinearLayout.LayoutParams params_img;
    @Override
    protected int getLayout() {
        return R.layout.activity_healedudetail;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.heal_edu));
        params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params_img=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 800);
        datail=(HealEduDatail) getIntent().getSerializableExtra("healEduDetail");
        if(!datail.getHeal_title().equals("")&&datail.getHeal_title()!=null){
            text_healedu_title.setText(Html.fromHtml(datail.getHeal_title()));
        }

        if(!datail.getHeal_content().equals("")&&datail.getHeal_content()!=null){
            String str=datail.getHeal_content();
            if(str.indexOf("<img") != -1){//包含图片
                setView(str,text_healedu_content);
            }else{//不包含，直接设置view
                TextView textView=new TextView(this);
                textView.setText(Html.fromHtml(str));
                textView.setLayoutParams(params);
                textView.setTextSize(22);
                text_healedu_content.addView(textView);
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
            Intent intent=new Intent(HealEduDetailActivity.this,ImageLookActivity.class);
            intent.putExtra("url",url);
            startActivity(intent);
        }
    }
}
