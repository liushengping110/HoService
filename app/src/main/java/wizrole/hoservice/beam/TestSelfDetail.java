package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by liushengping on 2017/11/8/008.
 * 何人执笔？
 */

public class TestSelfDetail {
    public String ResultCode;   //处理结果编码
    public String ResultContent;    //处理结果
    public List<TestSelfCateDetail> TestSelfCateDetail;
    public int TotalNum;

    public int getTotalNum() {
        return TotalNum;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<wizrole.hoservice.beam.TestSelfCateDetail> getTestSelfCateDetail() {
        return TestSelfCateDetail;
    }
}
