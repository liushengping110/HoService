package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by ${liushengping} on 2017/10/13.
 * 何人执笔？
 */

public class LabCheckName implements Serializable{

    public String LabCheckName;//化验检查名称
    public String LabCheckNormal;//正常值
    public String LabCheckClinicalSignificance;//临床意义

    public String getLabCheckClinicalSignificance() {
        return LabCheckClinicalSignificance;
    }

    public String getLabCheckName() {
        return LabCheckName;
    }

    public String getLabCheckNormal() {
        return LabCheckNormal;
    }
}
