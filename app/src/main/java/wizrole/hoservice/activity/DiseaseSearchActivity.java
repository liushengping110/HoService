package wizrole.hoservice.activity;

import android.content.Context;
import android.content.Intent;
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
import wizrole.hoservice.adapter.DiseaseEditAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.DisHostry;
import wizrole.hoservice.db.SearchDisReader;
import wizrole.hoservice.view.EditTextView;

/**
 * 疾病搜索
 */
public class DiseaseSearchActivity extends BaseActivity {

    @BindView(R.id.edit_dis_search)EditTextView editText;
    @BindView(R.id.btn_dis_search)TextView btn_hos_search;
    @BindView(R.id.list_hostroy)ListView list_hostroy;
    @BindView(R.id.text_del)TextView text_del;
    @BindView(R.id.img_del)ImageView img_del;
    public DiseaseEditAdapter adapter;
    public boolean status=false;//历史纪录集合是为0状态
    public boolean btn_status=false;//搜索  取消 按钮状态
    @Override
    protected int getLayout() {
        return R.layout.activity_diseasesearch;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        editText.setHint(getString(R.string.write_dis));
        list=getHostry();
        //设置适配器
        adapter=new DiseaseEditAdapter(this,list,R.layout.list_item_edithostry);
        list_hostroy.setAdapter(adapter);
    }



    /***获取存储的集合*/
    public List<DisHostry> list;
    public List<DisHostry> getHostry() {
        if(SearchDisReader.searchInfors(this).size()>0){
            list=SearchDisReader.searchInfors(this);
        }else{//数据库为空
            status=true;
            list=new ArrayList<DisHostry>();
            DisHostry hostry=new DisHostry();
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
                    DisHostry hostry=new DisHostry();
                    hostry.setContent(editText.getText().toString());
                    list.add(hostry);
                    adapter.notifyDataSetChanged();//刷新适配器
                    SearchDisReader.addInfors(hostry,DiseaseSearchActivity.this);//存储
                    Intent  intent=new Intent(DiseaseSearchActivity.this,ShowDisSearchActivity.class);
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
                    DisHostry hostry=list.get(position);
                    String content=hostry.getContent();
                    Intent  intent=new Intent(DiseaseSearchActivity.this,ShowDisSearchActivity.class);
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
                DisHostry h=new DisHostry();
                h.setContent("无");
                list.add(h);
                adapter.notifyDataSetChanged();
                list=SearchDisReader.searchInfors(DiseaseSearchActivity.this);
                for(int i=0;i<list.size();i++){
                    DisHostry hostry=list.get(i);
                    SearchDisReader.deleteInfors(hostry,DiseaseSearchActivity.this);
                }
            }else{
                String name=list.get(0).getContent();
                if(!name.equals(getString(R.string.null_data))){
                    list.clear();
                    DisHostry h=new DisHostry();
                    h.setContent(getString(R.string.null_data));
                    list.add(h);
                    adapter.notifyDataSetChanged();
                    list=SearchDisReader.searchInfors(DiseaseSearchActivity.this);
                    for(int i=0;i<list.size();i++){
                        DisHostry hostry=list.get(i);
                        SearchDisReader.deleteInfors(hostry,DiseaseSearchActivity.this);
                    }
                }
            }
        }
    }
}
