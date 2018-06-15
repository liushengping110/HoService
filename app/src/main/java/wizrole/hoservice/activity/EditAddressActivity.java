package wizrole.hoservice.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.PerInforOrder;
import wizrole.hoservice.db.DbReader;
import wizrole.hoservice.util.SharedPreferenceUtil;

/**
 * Created by a on 2017/4/5.
 * 编辑地址页面
 */

public class EditAddressActivity extends BaseActivity {

    @BindView(R.id.edit_order_name)EditText edit_order_name;    //姓名
    @BindView(R.id.text_order_tel)EditText edit_order_tel;  //电话
    @BindView(R.id.text_order_add)EditText edit_order_add;  //地址
    @BindView(R.id.text_men)TextView text_men;  //男
    @BindView(R.id.text_women)TextView text_women;  //女
    @BindView(R.id.text_order_sure)TextView text_order_sure;  //确定
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.rel_del)RelativeLayout rel_del;
    public PerInforOrder order;
    public boolean editOrAdd;   //判断是否是修改还是新建
    @Override
    protected int getLayout() {
        return R.layout.activity_editaddress;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        order=(PerInforOrder) getIntent().getSerializableExtra("inforOrder");
        if (order!=null){   //接收上一个页面的item点击修改--设置控件初始化值为之前的收货地址信息
            editOrAdd=false;
            text_title.setText(getString(R.string.change_add));
            edit_order_name.setText(order.getName());
            if(order.getSex().equals(getString(R.string.man))){
                sex=getString(R.string.man);
                text_men.setTextColor(getResources().getColor(R.color.text_bule));
            }else{
                sex=getString(R.string.woman);
                text_women.setTextColor(getResources().getColor(R.color.text_bule));
            }
            edit_order_tel.setText(order.getTel());
            edit_order_add.setText(order.getAddress());
            rel_del.setVisibility(View.VISIBLE);
        }else{  //新建进入的     标记--确认按钮中执行不一样的操作
            text_title.setText(getString(R.string.add_address));
            editOrAdd=true;
            rel_del.setVisibility(View.INVISIBLE);
        }
    }

    public String sex=null;
    @Override
    protected void setListener() {
        //返回按钮监听
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        text_men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_men.setTextColor(getResources().getColor(R.color.text_bule));
                text_women.setTextColor(getResources().getColor(R.color.black));
                sex=getString(R.string.man);
            }
        });
        text_women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_men.setTextColor(getResources().getColor(R.color.black));
                text_women.setTextColor(getResources().getColor(R.color.text_bule));
                sex=getString(R.string.woman);
            }
        });
        //删除按钮监听
        rel_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(order!=null){
                    DbReader.deleteInfors(order,EditAddressActivity.this);
                    finish();
                }
            }
        });
        text_order_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=edit_order_name.getText().toString();
                String tel=edit_order_tel.getText().toString();
                String add=edit_order_add.getText().toString();
                if (name.length()==0){
                    ToastShow(getString(R.string.getbaby_name));
                }else if(sex==null){
                    ToastShow(getString(R.string.sel_sex));
                }else if(tel.length()==0){
                    ToastShow(getString(R.string.getbaby_tel));
                }else if(add.length()==0){
                    ToastShow(getString(R.string.getbaby_add));
                }else{
                    if(editOrAdd){  //EditOrAdd==true的时候为新建--插入数据库
                        PerInforOrder inforOrder= new PerInforOrder(name,sex,tel,add,1);
                        List<PerInforOrder> orders=DbReader.searchInfors(EditAddressActivity.this);
                        if(orders.size()>0){    //如果数据库中有其他数据--就把所有的设置为未选中--只设置新建的为选中
                            for (int i=0;i<orders.size();i++){
                                orders.get(i).setSelect(0);
                                DbReader.editInfors(orders.get(i),EditAddressActivity.this);//先把所有的地址改为不选中
                            }
                            DbReader.addInfors(inforOrder,EditAddressActivity.this);//再把新建的改为选中
                        }else{      //如果数据库数据为空--则直接添加
                            DbReader.addInfors(inforOrder,EditAddressActivity.this);//再把新建的改为选中
                        }
                        SharedPreferenceUtil.putAdd(EditAddressActivity.this,inforOrder);//存储--为订单确认页面初始化使用
                    }else{  //否则就是修改 ---修改数据库
                        PerInforOrder inforOrder= new PerInforOrder(order.getId(),name,sex,tel,add,1);
                        List<PerInforOrder> orders=DbReader.searchInfors(EditAddressActivity.this);
                        for (int i=0;i<orders.size();i++){  //修改数据库--同样设置其他未选中
                            orders.get(i).setSelect(0);
                            DbReader.editInfors(orders.get(i),EditAddressActivity.this);//先把所有的地址改为不选中
                        }
                        DbReader.editInfors(inforOrder,EditAddressActivity.this);//再把修改的改为选中
                        SharedPreferenceUtil.putAdd(EditAddressActivity.this,inforOrder);
                    }
                    finish();
                }
            }
        });
    }
}
