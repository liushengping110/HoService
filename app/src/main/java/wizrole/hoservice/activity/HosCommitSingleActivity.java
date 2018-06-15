package wizrole.hoservice.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.ui.PopupWindowPotting;
import wizrole.hoservice.util.ImageLoading;

/**
 * Created by 何人执笔？ on 2018/4/12.
 * liushengping
 * 交流页面
 */

public class HosCommitSingleActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.lin_wenzi)LinearLayout lin_wenzi;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_shuohua)TextView text_shuohua;
    @BindView(R.id.img_yy_lt)ImageView img_yy_lt;
    @BindView(R.id.edit_msg)EditText edit_msg;
    @BindView(R.id.img_biaoqing)ImageView img_biaoqing;
    @BindView(R.id.img_more)ImageView img_more;

    @Override
    protected int getLayout() {
        return R.layout.activity_hoscommitusingle;
    }

    public String type;
    @Override
    protected void initData() {
        ButterKnife.bind(this);
        type=getIntent().getStringExtra("type");
        switch (type){
            case "1":
                text_title.setText("李医生");
                break;
            case "2":
                text_title.setText("王医生");
                break;
            case "3":
                text_title.setText("张护士");
                break;
            case "4":
                text_title.setText("刘护士");
                break;
        }
        initPopupWindow();
    }

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(this);
        img_yy_lt.setOnClickListener(this);
        img_biaoqing.setOnClickListener(this);
        img_more.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back:
                finish();
                break;
            case R.id.img_yy_lt:
                if(lin_wenzi.getVisibility()==View.VISIBLE){
                    lin_wenzi.setVisibility(View.INVISIBLE);
                    text_shuohua.setVisibility(View.VISIBLE);
                    ImageLoading.common(HosCommitSingleActivity.this,R.drawable.comm_yuyin,img_yy_lt,R.drawable.comm_yuyin);
                }else{
                    lin_wenzi.setVisibility(View.VISIBLE);
                    text_shuohua.setVisibility(View.INVISIBLE);
                    ImageLoading.common(HosCommitSingleActivity.this,R.drawable.comm_wenzi,img_yy_lt,R.drawable.comm_wenzi);
                }
                break;
            case R.id.text_shuohua:
                break;
            case R.id.img_biaoqing:

                break;
            case R.id.img_more:
                initPopupWindow();
                popupWindowPotting.Show(img_more);
                break;
        }
    }


    public PopupWindowPotting popupWindowPotting;
    public LinearLayout lin_yylt;
    public LinearLayout lin_splt;
    public LinearLayout lin_ps;
    public LinearLayout lin_xc;
    public ImageView img_yy_lt_pop;
    public ImageView img_more_pop;
    public EditText edit_msg_pop;
    public TextView text_shuohua_pop;
    public ImageView img_biaoqing_pop;
    public void initPopupWindow(){
        if (popupWindowPotting==null){
            popupWindowPotting=new PopupWindowPotting(HosCommitSingleActivity.this,4) {
                @Override
                protected int getLayout() {
                    return R.layout.pop_hos_comm;
                }
                @Override
                protected void initUI(){
                    text_shuohua_pop=$(R.id.text_shuohua);
                    img_yy_lt_pop=$(R.id.img_yy_lt);
                    edit_msg_pop=$(R.id.edit_msg);
                    img_biaoqing_pop=$(R.id.img_biaoqing);
                    img_more_pop=$(R.id.img_biaoqing);
                    lin_yylt=$(R.id.lin_yylt);
                    lin_splt=$(R.id.lin_splt);
                    lin_ps=$(R.id.lin_ps);
                    lin_xc=$(R.id.lin_xc);
                }
                @Override
                protected void setListener() {
                    img_yy_lt_pop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindowPotting.Hide();
                            if(lin_wenzi.getVisibility()==View.VISIBLE){
                                lin_wenzi.setVisibility(View.INVISIBLE);
                                text_shuohua.setVisibility(View.VISIBLE);
                                ImageLoading.common(HosCommitSingleActivity.this,R.drawable.comm_yuyin,img_yy_lt,R.drawable.comm_yuyin);
                            }else{
                                lin_wenzi.setVisibility(View.VISIBLE);
                                text_shuohua.setVisibility(View.INVISIBLE);
                                ImageLoading.common(HosCommitSingleActivity.this,R.drawable.comm_wenzi,img_yy_lt,R.drawable.comm_wenzi);
                            }
                        }
                    });
                    img_more_pop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindowPotting.Hide();
                            lin_wenzi.setVisibility(View.VISIBLE);
                            text_shuohua.setVisibility(View.INVISIBLE);
                            ImageLoading.common(HosCommitSingleActivity.this,R.drawable.comm_wenzi,img_yy_lt,R.drawable.comm_wenzi);
                        }
                    });
                    img_biaoqing_pop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindowPotting.Hide();
                        }
                    });
                }
            };
        }
    };
}
