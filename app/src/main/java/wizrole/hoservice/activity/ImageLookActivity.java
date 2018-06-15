package wizrole.hoservice.activity;

import android.view.View;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;

/**
 * Created by liushengping on 2017/12/5/005.
 * 何人执笔？
 * 图片预览
 */

public class ImageLookActivity extends BaseActivity {
    @BindView(R.id.img_look)PhotoView img_look;
    public String url;

    @Override
    protected int getLayout() {
        return R.layout.activity_imagelook;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        url=getIntent().getStringExtra("url");
        setView();
    }

    public void setView(){
        img_look.enable();
        Glide.with(this)
                .load(url)
                .crossFade()
                .placeholder(R.drawable.img_loadfail)
                .into(img_look);
    }

    @Override
    protected void setListener() {
        img_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
