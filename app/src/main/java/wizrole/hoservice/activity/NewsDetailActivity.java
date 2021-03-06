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
import wizrole.hoservice.beam.HosNews;
import wizrole.hoservice.util.ImageLoading;

/**
 * Created by Administrator on 2017/11/3/003.
 * 新闻详细信息页面
 *  院级新闻  科级新闻（顶部和中部） 生活级新闻
 */

public class NewsDetailActivity extends BaseActivity {

    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_right)TextView text_right;
    @BindView(R.id.text_news_title)TextView text_news_title;
    @BindView(R.id.text_news_content)LinearLayout text_news_content;
    public  LinearLayout.LayoutParams params;
    public  LinearLayout.LayoutParams params_img;
    public HosNews hosNews;
    public String type;
    @Override
    protected int getLayout() {
        return R.layout.activity_newsdetail;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.xxnr));
        hosNews=(HosNews) getIntent().getSerializableExtra("NewsDetail");
        type=getIntent().getStringExtra("type");
        if(type!=null){
            text_right.setText(getString(R.string.gdnr));
        }
        params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params_img=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1000);

        if(hosNews!=null){//院级新闻
            if(!hosNews.getNews_title().equals("")&&hosNews.getNews_title()!=null){
                text_news_title.setText(hosNews.getNews_title());
            }
            if (!hosNews.getNews_msg().equals("")){
                String str=hosNews.getNews_msg();
                if(str.indexOf("<img") != -1){//包含图片
                    setView(str,text_news_content);
                }else{//不包含，直接设置view
                    TextView textView=new TextView(this);
                    textView.setText(Html.fromHtml(str));
                    textView.setLayoutParams(params);
                    textView.setTextSize(22);
                    text_news_content.addView(textView);
                }
            }
        }
    }

    /**
     * ssssss<img><img>
     */
    /**
     * 图文混排
     * @param content
     * @param linearLayout
     */
    public void setView(String content,LinearLayout linearLayout){
        List<String> list = new ArrayList<String>();
        String[] s1 = content.split("<img");
        if(!s1[0].equals("")){//不是以图片开头
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
        //更多内容
        text_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type != null) {
                    if (type.equals("hos")) {//院级进入
                        Intent intent = new Intent(NewsDetailActivity.this, NewsListActivity.class);
                        intent.putExtra("newsType", "hos");
                        startActivity(intent);
                    } else if (type.equals("deparent")) {//科室级别进入
                        Intent intent = new Intent(NewsDetailActivity.this, NewsListActivity.class);
                        intent.putExtra("newsType", "deparent");
                        startActivity(intent);
                    } else if (type.equals("life")) {//生活级别进入
                        Intent intent = new Intent(NewsDetailActivity.this, NewsListActivity.class);
                        intent.putExtra("newsType", "life");
                        startActivity(intent);
                    } else if(type.equals("textScroll")){//滚动公告进入
                        Intent intent = new Intent(NewsDetailActivity.this, NewsListActivity.class);
                        intent.putExtra("newsType", "textScroll");
                        startActivity(intent);
                    }
                }
            }
        });

        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            Intent intent=new Intent(NewsDetailActivity.this,ImageLookActivity.class);
            intent.putExtra("url",url);
            startActivity(intent);
        }
    }
}
