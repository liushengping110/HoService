package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by a on 2017/5/22.
 */

public class Doc {

    public String ResultCode;
    public String ResultContent;
    public List<DoctorInfos> DoctorInfos;

    public List<DoctorInfos> getDoctorInfos() {
        return DoctorInfos;
    }

    public void setDoctorInfos(List<DoctorInfos> doctorInfos) {
        DoctorInfos = doctorInfos;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }
}
