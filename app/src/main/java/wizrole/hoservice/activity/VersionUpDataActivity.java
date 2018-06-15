package wizrole.hoservice.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.util.UpdateManager;

/**
 * Created by a on 2017/9/4.
 * 版本更新
 */

public class VersionUpDataActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_version_update)TextView text_version_update;
    @BindView(R.id.text_version_num)TextView text_version_num;

    @Override
    protected int getLayout() {
        return R.layout.activity_versionup;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.version_update));
        text_version_num.setText(getString(R.string.version_num)+getVersion());
    }
    /***获取当前应用版本号*/
    public String getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "ERR";
        }
    }

    @Override
    protected void setListener() {
        text_version_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /***校验版本号*/
                UpdateManager updateManager=new UpdateManager(VersionUpDataActivity.this,true);
                updateManager.downloadTxt();
            }
        });

        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
