package wizrole.hoservice.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;

/**
 * Created by a on 2017/9/4.
 */

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.lin_back)
    LinearLayout lin_back;
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.img_logo)ImageView img_logo;
    public int num;
    @Override
    protected int getLayout() {
        return R.layout.activity_aboutus;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("关于我们");

    }

    @Override
    protected void setListener() {
    }

    @OnClick(R.id.lin_back)
    public void onClick(View view){
        if (view.getId()== R.id.lin_back){
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
