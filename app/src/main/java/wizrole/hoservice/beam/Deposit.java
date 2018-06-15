package wizrole.hoservice.beam;

/**
 * Created by liushengping on 2017/11/22/022.
 * 何人执笔？
 * 押金缴纳
 */

public class Deposit {

    public String ResultCode;
    public String ResultContent;
    public String TradeNum;//(流水号)
    public String PayUrl;//(地址)


    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public String getTradeNum() {
        return TradeNum;
    }

    public String getPayUrl() {
        return PayUrl;
    }
}
