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
import wizrole.hoservice.beam.AnnounceMsg;
import wizrole.hoservice.beam.CustMessage;
import wizrole.hoservice.beam.DynamicMsg;
import wizrole.hoservice.util.ImageLoading;

/**
 * Created by a on 2017/9/14.
 * 医院动态  最新消息 自定义消息---详细页面（公告、动态、自定义消息复用）
 * 自定义消息复用
 */

public class DynamicDetailActivity extends BaseActivity {

    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_dynadetail_title)TextView text_dynadetail_title;
    @BindView(R.id.text_dynadetail_content)LinearLayout text_dynadetail_content;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    public AnnounceMsg announceMsg;
    public DynamicMsg dynamicMsg;
    public CustMessage custMessage;
    public  LinearLayout.LayoutParams params;
    public  LinearLayout.LayoutParams params_img;
    @Override
    protected int getLayout() {
        return R.layout.activity_dynamicdetail;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
         announceMsg=(AnnounceMsg) getIntent().getSerializableExtra("announMsg");
        dynamicMsg=(DynamicMsg) getIntent().getSerializableExtra("dynamicMsg");
        custMessage=(CustMessage) getIntent().getSerializableExtra("custMessage");
        params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params_img=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 600);
        if(announceMsg!=null){
            text_title.setText(getString(R.string.gonggao));
            if(!announceMsg.getAnnounce_title().equals("")){
                text_dynadetail_title.setText(Html.fromHtml(announceMsg.getAnnounce_title()));
            }
            if(!announceMsg.getAnnounce_msg().equals("")&&announceMsg.getAnnounce_msg()!=null){
                String str=announceMsg.getAnnounce_msg();
                if(str.indexOf("<img") != -1){//包含图片
                    setView(str,text_dynadetail_content);
                }else{//不包含，直接设置view
                    TextView textView=new TextView(this);
                    textView.setText(Html.fromHtml(str));
                    textView.setLayoutParams(params);
                    textView.setTextSize(22);
                    text_dynadetail_content.addView(textView);
                }
            }
        }else if(dynamicMsg!=null){
            text_title.setText(getString(R.string.dongtai));
            if(!dynamicMsg.getDynamic_title().equals("")){
                text_dynadetail_title.setText(Html.fromHtml(dynamicMsg.getDynamic_title()));
            }
            if(!dynamicMsg.getDynamic_msg().equals("")&&dynamicMsg.getDynamic_msg()!=null){
                String str=dynamicMsg.getDynamic_msg();
                if(str.indexOf("<img") != -1){//包含图片
                    setView(str,text_dynadetail_content);
                }else{//不包含，直接设置view
                    TextView textView=new TextView(this);
                    textView.setText(Html.fromHtml(str));
                    textView.setLayoutParams(params);
                    textView.setTextSize(22);
                    text_dynadetail_content.addView(textView);
                }
            }
        }else if(custMessage!=null){
            text_title.setText(getString(R.string.tongzhi));
            if(!custMessage.getCustMess_title().equals("")){
                text_dynadetail_title.setText(Html.fromHtml(custMessage.getCustMess_title()));
            }
            if(!custMessage.getCustMess_content().equals("")&&custMessage.getCustMess_content()!=null){
                String str=custMessage.getCustMess_content();
                if(str.indexOf("<img") != -1){//包含图片
                    setView(str,text_dynadetail_content);
                }else{//不包含，直接设置view
                    TextView textView=new TextView(this);
                    textView.setText(Html.fromHtml(str));
                    textView.setLayoutParams(params);
                    textView.setTextSize(22);
                    text_dynadetail_content.addView(textView);
                }
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
            Intent intent=new Intent(DynamicDetailActivity.this,ImageLookActivity.class);
            intent.putExtra("url",url);
            startActivity(intent);
        }
    }
}
