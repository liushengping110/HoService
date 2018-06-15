package wizrole.hoservice.life.preserent.goodssearch;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wizrole.hoservice.life.model.getgoodtype.CommodityList;


/**
 * Created by liushengping on 2018/1/25.
 * 何人执笔？
 */

public class GoodsSearchPreserent {
    public String str;
    public GoodsSearchInterface goodsSearchInterface;
    public Context context;
    public GoodsSearchPreserent(Context context,GoodsSearchInterface goodsSearchInterface){
        this.goodsSearchInterface=goodsSearchInterface;
        this.context=context;
    }
    public void selGoodsSearch(List<CommodityList> commodityLists, String name){
        List<CommodityList> lists=new ArrayList<>();
        //检测字母
        Pattern p = Pattern.compile("[a-zA-z]");
        if(p.matcher(name).find()){//含有英文
            str = name.replaceAll("[a-zA-Z]","" );
//            Toast.makeText(context,"含有英文",Toast.LENGTH_LONG).show();
        }else{
            str=name;
        }
        //检测数字
        Pattern pt = Pattern.compile(".*\\d+.*");
        Matcher m = pt.matcher(str);
        if (m.matches()) {
            str=str.replaceAll("\\d+","");
//            Toast.makeText(context,"含有数字",Toast.LENGTH_LONG).show();
        }

        //检测符号
        Pattern patPunc =Pattern.compile("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");
        if(patPunc.matcher(str).find()){//含有符号
//            Toast.makeText(context,"含有符号",Toast.LENGTH_LONG).show();
            str=str.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
        }

//        Toast.makeText(context,str,Toast.LENGTH_LONG).show();
//        Toast.makeText(context,str.length()+"",Toast.LENGTH_LONG).show();
        char[] a=str.toCharArray();
        for (int g=0;g<a.length;g++){
            for(int w=0;w<commodityLists.size();w++){
                int result1 = commodityLists.get(w).getCommodityName().indexOf(a[g]);
                if(result1 != -1){//包含，添加
                    lists.add(commodityLists.get(w));
                    commodityLists.remove(commodityLists.get(w));//添加后，移除，否则多次添加
                }
            }
        }
        goodsSearchInterface.getSearchGoods(lists);
    }
}
