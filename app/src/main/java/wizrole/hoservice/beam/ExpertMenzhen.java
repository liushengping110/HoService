package wizrole.hoservice.beam;


import java.util.List;

/**
 * Created by ${liushengping} on 2017/10/13.
 * 何人执笔？
 * 专家门诊时间安排表
 */

public class ExpertMenzhen {

    public String ResultCode;
    public String ResultContent;
    public List<String> ExpertVisit;

    public List<String> getExpertVisit() {
        return ExpertVisit;
    }

    public void setExpertVisit(List<String> expertVisit) {
        ExpertVisit = expertVisit;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public void setResultContent(String resultContent) {
        ResultContent = resultContent;
    }



    public String getResultCode() {

        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

}
