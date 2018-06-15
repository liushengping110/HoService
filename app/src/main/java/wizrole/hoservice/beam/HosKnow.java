package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by liushengping on 2017/12/5/005.
 * 何人执笔？
 */

public class HosKnow {

    public String ResultCode;
    public String ResultContent;
    public List<HospNotesList> HospNotesList;//（数组）

    public String getResultCode() {
        return ResultCode;
    }

    public List<wizrole.hoservice.beam.HospNotesList> getHospNotesList() {
        return HospNotesList;
    }

    public String getResultContent() {
        return ResultContent;
    }
}
