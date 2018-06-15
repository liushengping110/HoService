package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by liushengping on 2017/11/8/008.
 * 何人执笔？
 * 自定义消息
 */

public class CustMess {

    public String ResultCode;
    public String ResultContent;
    public List<CustMessage> CustMessage;
    public int TotalNum;

    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<wizrole.hoservice.beam.CustMessage> getCustMessage() {
        return CustMessage;
    }

    public int getTotalNum() {
        return TotalNum;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public void setResultContent(String resultContent) {
        ResultContent = resultContent;
    }

    public void setCustMessage(List<wizrole.hoservice.beam.CustMessage> custMessage) {
        CustMessage = custMessage;
    }

    public void setTotalNum(int totalNum) {
        TotalNum = totalNum;
    }
}
