package wizrole.hoservice.life.model.evaluation;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hoservice.Constant;
import wizrole.hoservice.util.RxJavaOkPotting;

/**
 * Created by liushengping on 2018/1/11/011.
 * 何人执笔？
 */

public class EvaluationHttp {

    public EvaluationBackInterface evaluationBackInterface;
    public EvaluationHttp(EvaluationBackInterface evaluationBackInterface){
        this.evaluationBackInterface=evaluationBackInterface;
    }

    public void getEvaluation(String CommodityId,int PageNo ){
        try {
            JSONObject object=new JSONObject();
            object.put("TradeCode","Y021");
            object.put("PageNo",PageNo);
            object.put("CommodityId",CommodityId);
            RxJavaOkPotting.getInstance(Constant.life_url).Ask(Constant.life_url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    evaluationBackInterface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        evaluationBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        EvaluationBack back=new EvaluationBack();
                        back=gson.fromJson(o.toString(),EvaluationBack.class);
                        if(back.getResultCode().equals("0")){
                            evaluationBackInterface.Succ(back);
                        }else{
                            evaluationBackInterface.Fail("null");
                        }
                    }
                }
            });
        }catch (JSONException e){
            evaluationBackInterface.Fail("");
        }
    }
}
