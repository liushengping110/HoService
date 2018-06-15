package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by a on 2017/5/22.
 */

public class Heal {

    public String ResultCode;
    public String ResultContent;
    public List<HealthKnow>  healthKnows;

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public void setResultContent(String resultContent) {
        ResultContent = resultContent;
    }

    public void setHealthKnows(List<HealthKnow> healthKnows) {
        this.healthKnows = healthKnows;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<HealthKnow> getHealthKnows() {
        return healthKnows;
    }
}
