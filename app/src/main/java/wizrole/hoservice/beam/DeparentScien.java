package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by ${liushengping} on 2017/10/9.
 * 何人执笔？
 * 妇产科学
 */

public class DeparentScien {

    public String ResultCode;
    public String ResultContent;
    public List<ObstetricsGynecology> ObstetricsGynecology;

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public void setResultContent(String resultContent) {
        ResultContent = resultContent;
    }

    public void setObstetricsGynecology(List<wizrole.hoservice.beam.ObstetricsGynecology> obstetricsGynecology) {
        ObstetricsGynecology = obstetricsGynecology;
    }

    public String getResultCode() {

        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<wizrole.hoservice.beam.ObstetricsGynecology> getObstetricsGynecology() {
        return ObstetricsGynecology;
    }
}
