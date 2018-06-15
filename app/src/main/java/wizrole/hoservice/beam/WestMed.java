package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by ${liushengping} on 2017/10/12.
 * 何人执笔？
 * 西药
 */

public class WestMed {

    public String ResultCode;   //处理结果编码
    public String ResultContent;    //处理结果
    public int TotalNum;
    public List<WestMedInfor> WestMedInfor;

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public void setResultContent(String resultContent) {
        ResultContent = resultContent;
    }

    public void setTotalNum(int totalNum) {
        TotalNum = totalNum;
    }

    public void setWestMedInfor(List<WestMedInfor> westMedInfor) {
        WestMedInfor = westMedInfor;
    }

    public String getResultCode() {

        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public int getTotalNum() {
        return TotalNum;
    }

    public List<WestMedInfor> getWestMedInfor() {
        return WestMedInfor;
    }
}
