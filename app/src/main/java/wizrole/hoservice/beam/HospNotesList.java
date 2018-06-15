package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by liushengping on 2017/12/5/005.
 * 何人执笔？
 */

public class HospNotesList implements Serializable{

    public String  HospNotesTitle;//（标题）
    public String HospNotesContext;//（内容）

    public String getHospNotesContext() {
        return HospNotesContext;
    }

    public String getHospNotesTitle() {
        return HospNotesTitle;
    }
}
