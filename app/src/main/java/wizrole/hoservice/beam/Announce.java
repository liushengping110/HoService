package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by a on 2017/9/6.
 *最新公告
 */

public class Announce {

    public String ResultCode;
    public String ResultContent;
    public int TotalNun;//总页数
    public List<AnnounceMsg> AnnounceMsg;

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

    public List<AnnounceMsg> getAnnounceMsg() {
        return AnnounceMsg;
    }

    public void setAnnounceMsg(List<AnnounceMsg> announceMsg) {
        AnnounceMsg = announceMsg;
    }

    public String getResultCode() {

        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

}
