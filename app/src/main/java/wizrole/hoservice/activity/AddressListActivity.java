package wizrole.hoservice.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.ListAddAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.PerInforOrder;
import wizrole.hoservice.db.DbReader;
import wizrole.hoservice.util.SharedPreferenceUtil;

/**
 * Created by a on 2017/4/5.
 */

public class AddressListActivity extends BaseActivity {

    @BindView(R.id.list_address)ListView list_address;  //列表控件
    @BindView(R.id.rel_add_address)RelativeLayout rel_add_address;//添加地址
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    public ListAddAdapter adapter;
    public List<PerInforOrder> inforOrders;
    public final int RESULT_CODE=2;  //回调信息--item回调
    public final int RESULT_CODE_BACK=3;  //回调信息--返回回调
    public final int RESULT_CODE_BACK_NULL=4;  //回调信息--返回回调--信息全部删除
    @Override
    protected int getLayout() {
        return R.layout.activity_addreelist;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("收货地址");
        inforOrders= DbReader.searchInfors(this);    //数据库查询出的结果--《集合》
        if(inforOrders!=null){
            if(inforOrders.size()==0){
                ToastShow("当前暂无收货地址，请添加");
            }else{
                adapter=new ListAddAdapter(this,inforOrders);
                list_address.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void setListener() {
        //返回按钮监听---如果没执行item点击的话--要将修改或者新建的item（已设置了默认选中）发送到订单确认页面--更新地址
        //如果执行的是修改和新建操作--订单确认页面显示的还是之前没有更新的地址
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inforOrders=DbReader.searchInfors(AddressListActivity.this);    //数据库查询出的结果--《集合》
                if(inforOrders.size()==0){
                    Intent intent=new Intent();
                    intent.putExtra("pi_back","list_null");
                    setResult(RESULT_CODE_BACK_NULL,intent);
                    SharedPreferenceUtil.clearAdd(AddressListActivity.this);   //清除
                    finish();
                }else{
                    for(int a=0;a<inforOrders.size();a++){
                        if(inforOrders.get(a).isSelect()==1){
                            Intent intent=new Intent();
                            intent.putExtra("pi_back",inforOrders.get(a));
                            setResult(RESULT_CODE_BACK,intent);
                            SharedPreferenceUtil.putAdd(AddressListActivity.this,inforOrders.get(a));//存储--
                            finish();
                        }
                    }
                }
            }
        });
        //添加按钮监听
        rel_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddressListActivity.this,EditAddressActivity.class);
                startActivity(intent);
            }
        });
        //item监听--点击则执行选中当前position的订单地址
        list_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PerInforOrder pi=inforOrders.get(i);
                pi.setSelect(1);
                for (int a=0;a<inforOrders.size();a++){
                    if(a==i){
                        inforOrders.get(a).setSelect(1);//修改选中状态--存储到数据库
                        DbReader.editInfors(inforOrders.get(a),AddressListActivity.this);
                        SharedPreferenceUtil.putAdd(AddressListActivity.this,inforOrders.get(a));
                    }else{  //只选中一个，其余的设置为0
                        inforOrders.get(a).setSelect(0);//修改选中状态--存储到数据库
                        DbReader.editInfors(inforOrders.get(a),AddressListActivity.this);
                    }
                }
                adapter.notifyDataSetChanged();
                Intent intent=new Intent();
                intent.putExtra("pi",pi);
                setResult(RESULT_CODE,intent);
                finish();
            }
        });
    }

    /**新建或者修改后 返回页面 重新刷新视图**/
    @Override
    protected void onResume() {
        super.onResume();
        inforOrders=DbReader.searchInfors(this);
        if(inforOrders!=null){
            if(inforOrders.size()==0){
                ToastShow("当前暂无收货地址，请添加");
            }
                adapter=new ListAddAdapter(this,inforOrders);
                list_address.setAdapter(adapter);
        }
    }

    /**物理按钮返回--更新地址*/
    @Override
    public void onBackPressed() {
        inforOrders=DbReader.searchInfors(AddressListActivity.this);    //数据库查询出的结果--《集合》
        if(inforOrders.size()==0){      //假如数据全部删除
            Intent intent=new Intent();
            intent.putExtra("pi_back","list_null");
            setResult(RESULT_CODE_BACK_NULL,intent);
            SharedPreferenceUtil.clearAdd(AddressListActivity.this);   //清除
            finish();
        }else{
            for(int a=0;a<inforOrders.size();a++){
                if(inforOrders.get(a).isSelect()==1){
                    Intent intent=new Intent();
                    intent.putExtra("pi_back",inforOrders.get(a));
                    setResult(RESULT_CODE_BACK,intent);
                    SharedPreferenceUtil.putAdd(AddressListActivity.this,inforOrders.get(a));
                    finish();
                }
            }
        }

        super.onBackPressed();
    }
}
