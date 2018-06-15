package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by liushengping on 2017/11/15/015.
 * 何人执笔？
 */

public class Inspection {


    public String ResultCode;
    public String ResultContent;
    public List<AdmResultReports> AdmResultReports;

    public String getResultContent() {
        return ResultContent;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public List<AdmResultReports> getAdmResultReports() {
        return AdmResultReports;
    }
}
