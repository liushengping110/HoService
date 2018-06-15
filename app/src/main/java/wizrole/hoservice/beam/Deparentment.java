package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by a on 2017/8/28.
 * 科室列表---科室介绍--科室对应医生
 */

public class Deparentment {

    public String ResultCode;
    public String ResultContent;
    public List<DeparentIntrduce> DeparentIntrduce;//科室信息--科室介绍-

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public void setResultContent(String resultContent) {
        ResultContent = resultContent;
    }


    public String getResultCode() {

        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<wizrole.hoservice.beam.DeparentIntrduce> getDeparentIntrduce() {
        return DeparentIntrduce;
    }

    public void setDeparentIntrduce(List<wizrole.hoservice.beam.DeparentIntrduce> deparentIntrduce) {
        DeparentIntrduce = deparentIntrduce;
    }
}
