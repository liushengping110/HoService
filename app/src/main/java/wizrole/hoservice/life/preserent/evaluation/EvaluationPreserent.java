package wizrole.hoservice.life.preserent.evaluation;


import wizrole.hoservice.life.model.evaluation.EvaluationBack;
import wizrole.hoservice.life.model.evaluation.EvaluationBackInterface;
import wizrole.hoservice.life.model.evaluation.EvaluationHttp;

/**
 * Created by liushengping on 2018/1/11/011.
 * 何人执笔？
 */

public class EvaluationPreserent implements EvaluationBackInterface {

    public EvaluationInterface evaluationInterface;
    public EvaluationHttp evaluationHttp;

    public EvaluationPreserent( EvaluationInterface evaluationInterface){
        this.evaluationInterface=evaluationInterface;
        evaluationHttp=new EvaluationHttp(this);
    }
    public void getEvaluationScore(String id,int page){
        evaluationHttp.getEvaluation(id,page);
    }

    @Override
    public void Succ(Object o) {
        evaluationInterface.getEvaluationSucc((EvaluationBack)o);
    }

    @Override
    public void Fail(String msg) {
        evaluationInterface.getEvaluationFail(msg);
    }
}
