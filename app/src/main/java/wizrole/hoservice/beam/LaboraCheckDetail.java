package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by ${liushengping} on 2017/10/13.
 * 何人执笔？
 * 化验检查
 */

public class LaboraCheckDetail {
    public String ResultCode;
    public String ResultContent;
    public int TotalNum;
    public List<LabCheckName> LabCheckName;

    public int getTotalNum() {
        return TotalNum;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<LabCheckName> getLabCheckName() {
        return LabCheckName;
    }
}
