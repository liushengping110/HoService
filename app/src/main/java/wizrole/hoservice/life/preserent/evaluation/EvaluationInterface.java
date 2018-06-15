package wizrole.hoservice.life.preserent.evaluation;


import wizrole.hoservice.life.model.evaluation.EvaluationBack;

/**
 * Created by liushengping on 2018/1/11/011.
 * 何人执笔？
 */

public interface EvaluationInterface {

    void getEvaluationSucc(EvaluationBack evaluationBack);
    void getEvaluationFail(String msg);
}
