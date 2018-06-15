package wizrole.hoservice.life.view;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wizrole.hoservice.R;
import wizrole.hoservice.activity.AddressListActivity;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.adapter.ListSureOrderAdapter;
import wizrole.hoservice.adapter.ListTimeSelAdapter;
import wizrole.hoservice.life.model.getgoodtype.CommodityList;
import wizrole.hoservice.beam.OrderTimeSel;
import wizrole.hoservice.beam.PerInforOrder;
import wizrole.hoservice.db.DbReader;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.ListUtil;
import wizrole.hoservice.util.SharedPreferenceUtil;

/**
 * Created by a on 2017/4/5.
 * 确认订单页面--支付
 */

public class SureOrderActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    @BindView(R.id.lin_address)LinearLayout lin_address;//地址选择
    @BindView(R.id.lin_order_other)LinearLayout lin_order_other;//地址选择
    @BindView(R.id.text_order_time)TextView text_order_time;//预计时间送达
    @BindView(R.id.list_sel_food)ListView list_sel_food;//预计时间送达
    @BindView(R.id.text_updata)TextView text_updata;
    @BindView(R.id.text_sure_name)TextView text_sure_name;
    @BindView(R.id.text_sure_tel)TextView text_sure_tel;
    @BindView(R.id.text_sure_add)TextView text_sure_add;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_all_pay)TextView text_all_pay;
    @BindView(R.id.text_psf)TextView text_psf;
    @BindView(R.id.text_d_pay)TextView text_d_pay;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_order_send)TextView text_order_send;
    @BindView(R.id.lin_alipay)LinearLayout lin_alipay;
    @BindView(R.id.lin_wx)LinearLayout lin_wx;
    @BindView(R.id.lin_yl)LinearLayout lin_yl;
    @BindView(R.id.img_alipay)ImageView img_alipay;
    @BindView(R.id.img_wx)ImageView img_wx;
    @BindView(R.id.img_yl)ImageView img_yl;
    public List<CommodityList> details;
    public List<PerInforOrder> orders;
    public final int REQUEST_CODE=1;
    public final int RESULT_CODE=2;
    public final int RESULT_CODE_BACK=3;
    public final int RESULT_CODE_BACK_NULL=4;
    public ListView list_pop_ordertime;
    public ListTimeSelAdapter listTimeadapter;
    public PopupWindow popupWindow;
    public String all_price;//所点商品总价
    public String all_psf;//配送费
    @Override
    protected int getLayout() {
        return R.layout.activity_sureorder;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.sure_order));
        details= (List<CommodityList>)getIntent().getSerializableExtra("selectAll");
        all_price=getIntent().getStringExtra("text_all_price");
        all_psf=getIntent().getStringExtra("text_all_peisong");
        text_all_pay.setText("￥"+(Double.parseDouble(all_price)+5)+"");
        text_d_pay.setText((Double.parseDouble(all_price)+5)+"");
        orders= DbReader.searchInfors(this);
        setAddress(orders); //设置地址
        if (details!=null){
            //设置适配器
            ListSureOrderAdapter adapter=new ListSureOrderAdapter(this,details);
            list_sel_food.setAdapter(adapter);
            ListUtil.setListViewHeightBasedOnChildren(list_sel_food);
        }else{
            ToastShow(getString(R.string.wifi_err));
            finish();
        }
    }
    public void setAddress(List<PerInforOrder> list){
        //设置地址
        if(list.size()==0){   //查询数据库中一个收货地址都没有的情况下
            text_sure_name.setText(getString(R.string.set_a_add));
            text_sure_name.setTextColor(getResources().getColor(R.color.text_bule));
            text_sure_tel.setVisibility(View.INVISIBLE);
            text_sure_add.setVisibility(View.INVISIBLE);
        }else{//从sharedpreference中查询
            PerInforOrder perI = (PerInforOrder) SharedPreferenceUtil.getAdd(SureOrderActivity.this);
            text_sure_name.setText(perI.getName()+perI.getSex());
            text_sure_tel.setText(perI.getTel());
            text_sure_add.setText(perI.getAddress());
        }
    }

    @Override
    protected void setListener() {
    }
    /******多个控件监听**/
    @OnClick({R.id.lin_address,R.id.text_order_time,R.id.lin_order_other,R.id.lin_back,R.id.text_updata,R.id.lin_wx,R.id.lin_alipay,R.id.lin_yl})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_address:  //选择地址---添加地址 上传服务器-绑定账号
                Intent intent=new Intent(this,AddressListActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                break;
            case R.id.text_order_time:  //预计时间送达选择---设置半个小时一个周期
                if(timeSels==null||timeSels.size()==0){
                    timeSels=getTime();
                }
                showPopupWindow(timeSels);
                setWindowAlpha(0.5f);
                break;
            case R.id.lin_order_other:  //订单备注---商家根据商品特性返回复选 单选条件
                Intent in=new Intent(this,OrderOtherActivity.class);
                startActivity(in);
                break;
            case R.id.lin_back:
                finish();
                break;
            case R.id.text_updata://tijiao
                intent=new Intent(SureOrderActivity.this,OrderPayActivity.class);
                intent.putExtra("type",pay_type);
                startActivity(intent);
                break;
            case R.id.lin_alipay://支付宝
                pay_type=1;
                ImageLoading.common(SureOrderActivity.this,R.drawable.sel_time,img_alipay,R.drawable.sel_time);
                ImageLoading.common(SureOrderActivity.this,R.drawable.no_sel_time,img_wx,R.drawable.no_sel_time);
                ImageLoading.common(SureOrderActivity.this,R.drawable.no_sel_time,img_yl,R.drawable.no_sel_time);
                break;
            case R.id.lin_wx://微信
                pay_type=2;
                ImageLoading.common(SureOrderActivity.this,R.drawable.sel_time,img_wx,R.drawable.sel_time);
                ImageLoading.common(SureOrderActivity.this,R.drawable.no_sel_time,img_alipay,R.drawable.no_sel_time);
                ImageLoading.common(SureOrderActivity.this,R.drawable.no_sel_time,img_yl,R.drawable.no_sel_time);
                break;
            case R.id.lin_yl://
                pay_type=3;
                ImageLoading.common(SureOrderActivity.this,R.drawable.sel_time,img_yl,R.drawable.sel_time);
                ImageLoading.common(SureOrderActivity.this,R.drawable.no_sel_time,img_wx,R.drawable.no_sel_time);
                ImageLoading.common(SureOrderActivity.this,R.drawable.no_sel_time,img_alipay,R.drawable.no_sel_time);
                break;

        }
    }
    public int pay_type=1;
    public List<OrderTimeSel> timeSels;
    public int time;
    public int a;
    /**获取时间集合**/
    public List<OrderTimeSel> getTime(){
        timeSels=new ArrayList<OrderTimeSel>();
        Date date=new Date();
        SimpleDateFormat format_hour= new SimpleDateFormat("HH");
        SimpleDateFormat format_minute= new SimpleDateFormat("mm");
        String h=format_hour.format(date);
        String m=format_minute.format(date);
        time= Integer.parseInt(h+m);//字符串拼接再转型
        String[] ots=getResources().getStringArray(R.array.order_time);
        //先拆分-=-转型 --比较大小--填充集合 --设置列表
        for(int j=0;j<ots.length;j++){
            OrderTimeSel timeSel=new OrderTimeSel();
            if(j>0){    //【尽快送达】--例外处理--因为要进行数据转换
                String[]  strs=ots[j].split(":");
                try {
                    int local_time= Integer.parseInt(strs[0]+strs[0]);//拼接后转型
                    if(time<local_time){    //如果下订单的时间在当前时间区间内
                        a=j+1;
                        if(j==ots.length){
                            timeSel.setTime(ots[j]);
                            timeSel.setSelect(0);
                            timeSels.add(timeSel);
                            timeSels.remove(ots[(j-1)]);
                        }else{
                            timeSel.setTime(ots[j]);
                            timeSel.setSelect(0);
                            timeSels.add(timeSel);
                            a=0;
                        }
                    }
                }catch (NumberFormatException e){
                    ToastShow(getString(R.string.sorry_sjzhsb));
                }
            }else{      //添加第一条【尽快送达】
                timeSel.setTime(ots[0]);
                timeSel.setSelect(1);
                timeSels.add(timeSel);
            }
        }
        return  timeSels;
    }
    /**
     * 显示订单配送时间选择弹窗
     * @param orderTimeSels
     */
    public void showPopupWindow(List<OrderTimeSel> orderTimeSels){
        View v= LayoutInflater.from(this).inflate(R.layout.pop_listview_ordertime,null);
        list_pop_ordertime=(ListView)v.findViewById(R.id.list_pop_ordertime);
        popupWindow = new PopupWindow(v,
                LinearLayoutCompat.LayoutParams.MATCH_PARENT, 700, true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
                popupWindow.setAnimationStyle(R.style.PopupAnimation);
                setWindowAlpha(1.0f);
            }
        });
        listTimeadapter=new ListTimeSelAdapter(SureOrderActivity.this,orderTimeSels);
        list_pop_ordertime.setAdapter(listTimeadapter);
        list_pop_ordertime.setOnItemClickListener(this);//选择预送达时间监听
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0,0);
    }

    /***---各种按钮的数据--更新收货地址*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_CODE){    //item点击接受数据
                PerInforOrder getData=(PerInforOrder) data.getSerializableExtra("pi");
                text_sure_tel.setVisibility(View.VISIBLE);
                text_sure_add.setVisibility(View.VISIBLE);
                text_sure_name.setText(getData.getName()+getData.getSex());
                text_sure_tel.setText(getData.getTel());
                text_sure_add.setText(getData.getAddress());
            }else if(resultCode==RESULT_CODE_BACK){     //返回按钮接受数据
                PerInforOrder getData_back=(PerInforOrder) data.getSerializableExtra("pi_back");
                text_sure_tel.setVisibility(View.VISIBLE);
                text_sure_add.setVisibility(View.VISIBLE);
                text_sure_name.setText(getData_back.getName()+getData_back.getSex());
                text_sure_tel.setText(getData_back.getTel());
                text_sure_add.setText(getData_back.getAddress());
            }else if(resultCode==RESULT_CODE_BACK_NULL){        //当数据全部删除后
                text_sure_name.setText(getString(R.string.set_a_add));
                text_sure_name.setTextColor(getResources().getColor(R.color.text_bule));
                text_sure_tel.setVisibility(View.INVISIBLE);
                text_sure_add.setVisibility(View.INVISIBLE);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 设置窗体透明度
     *
     * @param alpha
     *            透明度值
     */
    private WindowManager.LayoutParams lp;
    private void setWindowAlpha(float alpha) {
        lp = getWindow().getAttributes();
        // 重新设定窗体透明度
        lp.alpha = alpha;
        // 重新设定窗体布局参数
        getWindow().setAttributes(lp);
    }
    /**
     * 时间选择item点击监听
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        OrderTimeSel orderTimeSel=timeSels.get(i);
        for (int a=0;a<timeSels.size();a++){
            if(a==i){
                orderTimeSel.setSelect(1);
            }else{
                timeSels.get(a).setSelect(0);
            }
        }
        listTimeadapter.notifyDataSetChanged();
        popupWindow.dismiss();
        text_order_send.setText(orderTimeSel.getTime());
    }
}
