package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by ${liushengping} on 2017/9/27.
 * 何人执笔？
 */

public class HealEdu {
    public String ResultCode;
    public String ResultContent;
    public int TotalNun;//总页数
    public List<HealEduDatail> HealEduDatail;

    public int getTotalNun() {
        return TotalNun;
    }

    public void setTotalNun(int totalNun) {
        TotalNun = totalNun;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public void setResultContent(String resultContent) {
        ResultContent = resultContent;
    }

    public void setHealEduDatail(List<wizrole.hoservice.beam.HealEduDatail> healEduDatail) {
        HealEduDatail = healEduDatail;
    }

    public String getResultCode() {

        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<HealEduDatail> getHealEduDatail() {
        return HealEduDatail;
    }
}
