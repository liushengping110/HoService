package wizrole.hoservice.life.model.evaluation;

import java.util.List;

/**
 * Created by liushengping on 2018/1/11/011.
 * 何人执笔？
 */

public class EvaluationBack {
    public String ResultCode;
    public String    ResultContent;
    public int TotalNum;
    public List<CommentList> CommentList;	          //      - 评价列表

    public int getTotalNum() {
        return TotalNum;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<CommentList> getCommentList() {
        return CommentList;
    }
}
