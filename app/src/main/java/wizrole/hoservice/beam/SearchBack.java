package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by a on 2017/9/11.
 */

public class SearchBack {
    public String ResultCode;
    public String ResultContent;
    public List<DoctorInfos> DoctorInfos;
    public List<DeparentIntrduce> DeparentIntrduce;

    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<wizrole.hoservice.beam.DoctorInfos> getDoctorInfos() {
        return DoctorInfos;
    }

    public List<DeparentIntrduce> getDeparentIntrduce() {
        return DeparentIntrduce;
    }

    public void setResultCode(String resultCode) {

        ResultCode = resultCode;
    }

    public void setResultContent(String resultContent) {
        ResultContent = resultContent;
    }

    public void setDoctorInfos(List<wizrole.hoservice.beam.DoctorInfos> doctorInfos) {
        DoctorInfos = doctorInfos;
    }

    public void setDeparentIntrduce(List<DeparentIntrduce> DeparentIntrduce) {
        DeparentIntrduce = DeparentIntrduce;
    }
}
