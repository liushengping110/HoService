package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by ${liushengping} on 2017/10/10.
 * 何人执笔？
 * 视频
 */

public class Vedio {


    public String ResultCode;   //处理结果编码
    public String ResultContent;    //处理结果
    public int TotalNum;//最大页码
    public List<VedioList> VedioList;

    public int getTotalNum() {
        return TotalNum;
    }

    public void setTotalNum(int totalNum) {
        TotalNum = totalNum;
    }


    public void setVedioList(List<VedioList> vedioList) {
        VedioList = vedioList;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public void setResultContent(String resultContent) {
        ResultContent = resultContent;
    }

    public List<VedioList> getVedioList() {
        return VedioList;
    }
}
