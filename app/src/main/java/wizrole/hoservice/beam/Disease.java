package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by ${liushengping} on 2017/10/11.
 * 何人执笔？
 */

public class Disease {

    public String ResultCode;
    public String ResultContent;
    public List<DiseaseDeparent> DiseaseDeparent;//科室列表

    public List<DiseaseDeparent> getDiseaseDeparent() {
        return DiseaseDeparent;
    }

    public void setDiseaseDeparent(List<DiseaseDeparent> diseaseDeparent) {
        DiseaseDeparent = diseaseDeparent;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public void setResultContent(String resultContent) {
        ResultContent = resultContent;
    }
}
