package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by a on 2017/9/6.
 * 医院动态
 */

public class Dynamic {
    public String ResultCode;
    public String ResultContent;
    public List<DynamicMsg> DynamicMsg;
    public int TotalNun;

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public void setResultContent(String resultContent) {
        ResultContent = resultContent;
    }

    public List<DynamicMsg> getDynamicMsg() {
        return DynamicMsg;
    }

    public void setDynamicMsg(List<DynamicMsg> DynamicMsg) {
        DynamicMsg = DynamicMsg;
    }

    public void setTotalNun(int totalNun) {
        TotalNun = totalNun;
    }

    public String getResultCode() {

        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }



    public int getTotalNun() {
        return TotalNun;
    }
}
