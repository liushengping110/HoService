package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by ${liushengping} on 2017/10/17.
 * 何人执笔？
 */

public class SurgeryDetail {

    public String ResultCode;
    public String ResultContent;
    public int TotalNum;
    public List<SurgeryDetailInfor> SurgeryDetailInfor;

    public int getTotalNum() {
        return TotalNum;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<SurgeryDetailInfor> getSurgeryDetailInfor() {
        return SurgeryDetailInfor;
    }
}
