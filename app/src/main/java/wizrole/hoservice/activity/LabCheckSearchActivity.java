package wizrole.hoservice.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.LabCheckEditAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.LabCheckHostry;
import wizrole.hoservice.db.SearchLabCheckReader;
import wizrole.hoservice.view.EditTextView;

/**
 * 化验检查搜索
 */
public class LabCheckSearchActivity extends BaseActivity {

    @BindView(R.id.edit_dis_search)EditTextView editText;
    @BindView(R.id.btn_dis_search)TextView btn_hos_search;
    @BindView(R.id.list_hostroy)ListView list_hostroy;
    @BindView(R.id.text_del)TextView text_del;
    @BindView(R.id.img_del)ImageView img_del;
    public LabCheckEditAdapter adapter;
    public SharedPreferences sp;
    public boolean status=false;//历史纪录集合是为0状态
    public boolean btn_status=false;//搜索  取消 按钮状态
    @Override
    protected int getLayout() {
        return R.layout.activity_diseasesearch;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        editText.setHint(getString(R.string.edit_hyjcxm));
        list=getHostry();
        //设置适配器
        adapter=new LabCheckEditAdapter(this,list,R.layout.list_item_edithostry);
        list_hostroy.setAdapter(adapter);
    }



    /***获取存储的集合*/
    public List<LabCheckHostry> list;
    public List<LabCheckHostry> getHostry() {
        if(SearchLabCheckReader.searchInfors(this).size()>0){
            list=SearchLabCheckReader.searchInfors(this);
        }else{//数据库为空
            status=true;
            list=new ArrayList<LabCheckHostry>();
            LabCheckHostry hostry=new LabCheckHostry();
            hostry.setContent(getString(R.string.null_data));
            list.add(hostry);
        }
        return list;
    }


    @Override
    protected void setListener() {
        btn_hos_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().length()==0){
                    finish();
                }else{
                    if(status){//如果是添加第一条记录，就先删除那个【无】
                        list.clear();
                        status=false;
                    }
                    LabCheckHostry hostry=new LabCheckHostry();
                    hostry.setContent(editText.getText().toString());
                    list.add(hostry);
                    adapter.notifyDataSetChanged();//刷新适配器
                    SearchLabCheckReader.addInfors(hostry,LabCheckSearchActivity.this);//存储
                    Intent  intent=new Intent(LabCheckSearchActivity.this,ShowLabCheckSearchActivity.class);
                    intent.putExtra("search",editText.getText().toString());
                    startActivity(intent);
                    //关闭软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0) ;

                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText.getText().toString().length()>0){
                    btn_hos_search.setText(getString(R.string.search));
                    btn_status=true;
                }else{
                    btn_hos_search.setText(getString(R.string.cancle));
                    btn_status=false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    /**删除搜索历史记录**/
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delHostroy();
            }
        });
        //删除搜索历史记录
        text_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delHostroy();
            }
        });

        list_hostroy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(status){//数据为空  【无】
                    //点击则不请求数据
                }else{
                    LabCheckHostry hostry=list.get(position);
                    String content=hostry.getContent();
                    Intent  intent=new Intent(LabCheckSearchActivity.this,ShowLabCheckSearchActivity.class);
                    intent.putExtra("search",content);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 删除历史记录
     */
    public void delHostroy(){
        if(list.size()>0){
            if(list.size()>1){
                list.clear();
                LabCheckHostry h=new LabCheckHostry();
                h.setContent(getString(R.string.null_data));
                list.add(h);
                adapter.notifyDataSetChanged();
                list=SearchLabCheckReader.searchInfors(LabCheckSearchActivity.this);
                for(int i=0;i<list.size();i++){
                    LabCheckHostry hostry=list.get(i);
                    SearchLabCheckReader.deleteInfors(hostry,LabCheckSearchActivity.this);
                }
            }else{
                String name=list.get(0).getContent();
                if(!name.equals(getString(R.string.null_data))){
                    list.clear();
                    LabCheckHostry h=new LabCheckHostry();
                    h.setContent(getString(R.string.null_data));
                    list.add(h);
                    adapter.notifyDataSetChanged();
                    list=SearchLabCheckReader.searchInfors(LabCheckSearchActivity.this);
                    for(int i=0;i<list.size();i++){
                        LabCheckHostry hostry=list.get(i);
                        SearchLabCheckReader.deleteInfors(hostry,LabCheckSearchActivity.this);
                    }
                }
            }
        }
    }
}