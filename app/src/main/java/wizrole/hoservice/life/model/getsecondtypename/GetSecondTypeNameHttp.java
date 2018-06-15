package wizrole.hoservice.life.model.getsecondtypename;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hoservice.Constant;
import wizrole.hoservice.util.RxJavaOkPotting;

/**
 * Created by liushengping on 2018/1/28.
 * 何人执笔？
 */

public class GetSecondTypeNameHttp {

    public GetSecondTypeNameBackInterface getSecondTypeNameBackInterface;
    public GetSecondTypeNameHttp(GetSecondTypeNameBackInterface getSecondTypeNameBackInterface){
        this.getSecondTypeNameBackInterface=getSecondTypeNameBackInterface;
    }

    public void getSecondTypeName(String name){
        try{
            JSONObject object=new JSONObject();
            object.put("TradeCode","Y022");
            object.put("StoreType",name);
            RxJavaOkPotting.getInstance(Constant.life_url).Ask(Constant.life_url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    getSecondTypeNameBackInterface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        getSecondTypeNameBackInterface.Fail("");
                    }else{
                        GetSecondTypeNameBack back=new GetSecondTypeNameBack();
                        Gson gson=new Gson();
                        back=gson.fromJson(o.toString(),GetSecondTypeNameBack.class);
                        if(back.getResultCode().equals("0")){
                            getSecondTypeNameBackInterface.Succ(back);
                        }else{
                            getSecondTypeNameBackInterface.Fail("null");
                        }
                    }
                }
            });
        }catch (JSONException e){
            getSecondTypeNameBackInterface.Fail("");
        }
    }
}
