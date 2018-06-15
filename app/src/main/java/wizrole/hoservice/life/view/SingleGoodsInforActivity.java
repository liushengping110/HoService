package wizrole.hoservice.life.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.adapter.EvlautionAdapter;
import wizrole.hoservice.life.model.evaluation.CommentList;
import wizrole.hoservice.life.model.evaluation.EvaluationBack;
import wizrole.hoservice.life.model.getgoodtype.CommodityList;
import wizrole.hoservice.life.preserent.evaluation.EvaluationInterface;
import wizrole.hoservice.life.preserent.evaluation.EvaluationPreserent;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.view.DetailHeaderBehavior;
import wizrole.hoservice.view.LoadingView;

/**
 * Created by liushengping on 2018/1/11/011.
 * 何人执笔？
 * 单个商品详情页面
 */

public class SingleGoodsInforActivity extends BaseActivity implements EvaluationInterface,View.OnClickListener{


    public EvlautionAdapter adapter;
    public DetailHeaderBehavior dhb;
    private int page = 1;
    public CommodityList commodityList;
    public int TotalNum=0;
    @BindView(R.id.iv_detail)ImageView iv_detail;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.ic_close)ImageView ic_close;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.toolbar_title)TextView toolbar_title;
    @BindView(R.id.detail_name)TextView detail_name;
    @BindView(R.id.detail_sale)TextView detail_sale;
    @BindView(R.id.detail_price)TextView detail_price;
    @BindView(R.id.text_err_msg)TextView text_err_msg;
    @BindView(R.id.nes_err_msg)NestedScrollView nes_err_msg;
    @BindView(R.id.nes_load_view)NestedScrollView nes_load_view;
    @BindView(R.id.load_view)LoadingView load_view;
    @BindView(R.id.headerview)View headerView;
    @BindView(R.id.detail_recyclerView)RecyclerView recyclerView;
    public RelativeLayout rel_evlaution_lookmore;//查看更多
    public TextView goods_evlaution_num;//商品评价（好评率98%）
    public TextView goods_evlaution_totlanum;//共计15条评价
    public TextView text_evlaution_lookmore;//查看更多
    public EvaluationPreserent evaluationPreserent=new EvaluationPreserent(this);
    @Override
    protected int getLayout() {
        return R.layout.activity_singlegoodsdetail;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        commodityList=(CommodityList) getIntent().getSerializableExtra("singlegoods");
        initViews();
        initRecyclerView();
        getEvlaution();
    }
    public boolean wifi_err=false;//重试点击
    public void getEvlaution(){
        if(wifi_err){
            evaluationPreserent.getEvaluationScore(commodityList.getCommodityNo(),page);
        }else{
            //重试
            img_net_err.setVisibility(View.INVISIBLE);
            nes_load_view.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            nes_err_msg.setVisibility(View.INVISIBLE);
            evaluationPreserent.getEvaluationScore(commodityList.getCommodityNo(),page);
        }
    }
    //图片添加ip
    public static final String ip="http://39.107.75.214/";
    private void initViews() {
        final_commentLists=new ArrayList<>();
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) headerView.getLayoutParams();
        dhb = (DetailHeaderBehavior) lp.getBehavior();
        String ms=ip+commodityList.getCommodityPic();
        ImageLoading.common(SingleGoodsInforActivity.this,ip+commodityList.getCommodityPic(),iv_detail,R.drawable.img_loadfail);
        toolbar_title.setText("商品评价");
        detail_name.setText(commodityList.getCommodityName());
        detail_price.setText(commodityList.getCommodityAmt());
        detail_sale.setText("月售200" + " 好评率95%");
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    protected void setListener() {
        ic_close.setOnClickListener(this);
        text_err_msg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ic_close:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.text_err_msg://重试
                if(!wifi_err){
                    page=1;
                    getEvlaution();
                }
                break;
        }
    }


    public EvaluationBack evaluation;
    @Override
    public void getEvaluationSucc(EvaluationBack evaluationBack) {
        TotalNum=evaluationBack.getTotalNum();
        evaluation=evaluationBack;
        handler.sendEmptyMessage(0);
    }

    @Override
    public void getEvaluationFail(String msg) {
        if(msg.equals("")){
            handler.sendEmptyMessage(2);
        }else{
            handler.sendEmptyMessage(1);
        }
    }
    public List<CommentList> commentLists;
    public List<CommentList> final_commentLists;
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://成功
                    wifi_err=true;
                    img_net_err.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    nes_err_msg.setVisibility(View.INVISIBLE);
                    nes_load_view.setVisibility(View.INVISIBLE);
                    commentLists=evaluation.getCommentList();
                    final_commentLists.addAll(commentLists);
                    adapter=new EvlautionAdapter(SingleGoodsInforActivity.this,final_commentLists);
                    recyclerView.setAdapter(adapter);
                    setHeaderFooter();
                    break;
                case 1://无数据
                    wifi_err=true;
                    if(!status){
                        ImageLoading.common(SingleGoodsInforActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                    }
                    img_net_err.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    nes_err_msg.setVisibility(View.VISIBLE);
                    nes_load_view.setVisibility(View.INVISIBLE);
                    text_err_msg.setText("当前商品暂无评价");
                    ToastShow("当前商品暂无评价");
                    break;
                case 2://断网
                    wifi_err=false;
                    if(!status){
                        ImageLoading.common(SingleGoodsInforActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                    }
                    img_net_err.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    nes_err_msg.setVisibility(View.VISIBLE);
                    nes_load_view.setVisibility(View.INVISIBLE);
                    text_err_msg.setText("重新加载");
                    ToastShow("网络请求失败，请检查网络");
                    break;
            }
        }
    };
    public boolean headerfoot_status=false;//是否添加了头尾
    public void setHeaderFooter(){
        if(!headerfoot_status){
            View headerView= LayoutInflater.from(SingleGoodsInforActivity.this).inflate(R.layout.recy_evlaution_header,null);
            goods_evlaution_num=(TextView)headerView.findViewById(R.id.goods_evlaution_num) ;
            goods_evlaution_totlanum=(TextView)headerView.findViewById(R.id.goods_evlaution_totlanum) ;
            goods_evlaution_num.setText("商品评级(好评率98%)");
            if(TotalNum==0){
                goods_evlaution_totlanum.setText("暂无评价");
            }else{
                goods_evlaution_totlanum.setText("共计"+TotalNum+"条评价");
            }
            /**footerView**/
            View footerView= LayoutInflater.from(SingleGoodsInforActivity.this).inflate(R.layout.recy_evlaution_footer,null);
            rel_evlaution_lookmore=(RelativeLayout)footerView.findViewById(R.id.rel_evlaution_lookmore);
            text_evlaution_lookmore=(TextView) footerView.findViewById(R.id.text_evlaution_lookmore);
            if(TotalNum<=1){
                text_evlaution_lookmore.setText("没有更多商品评价了");
            }else{
                text_evlaution_lookmore.setText("点击查看更多商品评价");
            }
            rel_evlaution_lookmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TotalNum<=1){
                        ToastShow("已经没有更多商品评价了");
                    }else{
                        Intent intent=new Intent(SingleGoodsInforActivity.this,MoreEvlautListActivity.class);
                        intent.putExtra("CommodityNo",commodityList.getCommodityNo());//商品主键id
                        startActivity(intent);
                    }
                }
            });
            adapter.setFooterView(footerView);
            adapter.setHeaderView(headerView);
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    public boolean status=false;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        status=true;
    }
}
