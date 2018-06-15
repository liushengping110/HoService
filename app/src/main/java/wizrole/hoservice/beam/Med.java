package wizrole.hoservice.beam;

/**
 * Created by a on 2017/9/19.
 */

public class Med {

    public String ResultCode;
    public String ErrorMsg;
    public MedItemS MedItemS;

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public void setMedItemS(MedItemS medItemS) {
        this.MedItemS = medItemS;
    }

    public String getResultCode() {

        return ResultCode;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public MedItemS getMedItemS() {
        return MedItemS;
    }
}
