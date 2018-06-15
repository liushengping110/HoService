package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by ${liushengping} on 2017/10/11.
 * 何人执笔？
 */

public class DiseasInfor {

    public String ResultCode;
    public String ResultContent;
    public List<DiseaseName> DiseaseName;
    public int TotalNum;

    public int getTotalNum() {
        return TotalNum;
    }

    public void setTotalNum(int totalNum) {
        TotalNum = totalNum;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public void setResultContent(String resultContent) {
        ResultContent = resultContent;
    }

    public void setDiseaseName(List<DiseaseName> diseaseName) {
        DiseaseName = diseaseName;
    }

    public String getResultCode() {

        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<DiseaseName> getDiseaseName() {
        return DiseaseName;
    }
}
