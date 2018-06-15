package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by ${liushengping} on 2017/10/17.
 * 何人执笔？
 */

public class Surgery {
    public String ResultCode;
    public String ResultContent;
    public List<SurgeryDeparent> SurgeryDeparent;

    public String getResultContent() {
        return ResultContent;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public List<wizrole.hoservice.beam.SurgeryDeparent> getSurgeryDeparent() {
        return SurgeryDeparent;
    }
}
