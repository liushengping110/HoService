package wizrole.hoservice.life.model.getsecondtypename;

import java.util.List;

/**
 * Created by liushengping on 2018/1/28.
 * 何人执笔？
 */

public class GetSecondTypeNameBack {

    public String  ResultCode;
    public String ResultContent;
    public List<TwoTypeList> TwoTypeList;

    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<TwoTypeList> getTwoTypeList() {
        return TwoTypeList;
    }
}
