package wizrole.hoservice.beam;

/**
 * Created by a on 2017/9/14.
 */

public class UserAdvice {

    public String ResultCode;
    public String ResultContent;
    public String ResultStatus;

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public void setResultContent(String resultContent) {
        ResultContent = resultContent;
    }

    public void setResultStatus(String resultStatus) {
        ResultStatus = resultStatus;
    }

    public String getResultCode() {

        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public String getResultStatus() {
        return ResultStatus;
    }
}
