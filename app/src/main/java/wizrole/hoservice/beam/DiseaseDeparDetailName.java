package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by ${liushengping} on 2017/10/11.
 * 何人执笔？
 * 消化内科 心内科  血液科等等
 */

public class DiseaseDeparDetailName implements Serializable {
    public String DiseaseDeparDetailName;//科室名

    public String getDiseaseDeparDetailName() {
        return DiseaseDeparDetailName;
    }

    public void setDiseaseDeparDetailName(String diseaseDeparDetailName) {
        DiseaseDeparDetailName = diseaseDeparDetailName;
    }
}
