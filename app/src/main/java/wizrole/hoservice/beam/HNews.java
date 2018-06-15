package wizrole.hoservice.beam;

import java.util.List;

import wizrole.hoservice.adapter.ListAdAdapter;

/**
 * Created by a on 2017/9/14.
 * 院级新闻类
 */

public class HNews {

    public String ResultCode;
    public String ResultContent;
    public int TotalNum;
    public List<HosNews> HosNews;

    public int getTotalNum() {
        return TotalNum;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public void setResultContent(String resultContent) {
        ResultContent = resultContent;
    }

    public void setHosNews(List<HosNews> hosNews) {
        HosNews = hosNews;
    }

    public String getResultCode() {

        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<HosNews> getHosNews() {
        return HosNews;
    }
}
