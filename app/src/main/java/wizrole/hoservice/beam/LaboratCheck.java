package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by ${liushengping} on 2017/10/13.
 * 何人执笔？
 */

public class LaboratCheck {

    public String ResultCode;
    public String ResultContent;
    public List<LaboratCheckCate> LaboratCheckCate;

    public String getResultContent() {
        return ResultContent;
    }

    public List<LaboratCheckCate> getLaboratCheckCate() {
        return LaboratCheckCate;
    }

    public String getResultCode() {
        return ResultCode;
    }
}
