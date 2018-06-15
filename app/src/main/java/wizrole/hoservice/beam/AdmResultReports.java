package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by liushengping on 2017/11/15/015.
 * 何人执笔？
 */

public class AdmResultReports {

    public String ArcDesc; //项目名称
    public String SpecName; //检查类型(血、尿)
    public String RecDateTime; //时间
    public List<ReportMsgDetail> ReportMsg; //检验结果

    public String getArcDesc() {
        return ArcDesc;
    }

    public String getSpecName() {
        return SpecName;
    }

    public String getRecDateTime() {
        return RecDateTime;
    }

    public List<ReportMsgDetail> getReportMsg() {
        return ReportMsg;
    }
}
