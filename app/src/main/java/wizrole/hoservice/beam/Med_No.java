package wizrole.hoservice.beam;

/**
 * Created by a on 2017/9/20.
 * 非药品信息类
 */

public class Med_No {

    public String ResultCode;
    public String ErrorMsg;
    public TarItemS TarItemS;

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public void setTarItemS(TarItemS tarItemS) {
        TarItemS = tarItemS;
    }

    public String getResultCode() {

        return ResultCode;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public TarItemS getTarItemS() {
        return TarItemS;
    }
}
