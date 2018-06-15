package wizrole.hoservice.beam;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${liushengping} on 2017/10/11.
 * 何人执笔？
 * 疾病科室
 */

public class DiseaseDeparent  implements Serializable{

    public String DiseaseDeparName;//内科 外科 。。。
    public List<DiseaseDeparDetailName> DiseaseDeparDetailName;//内科 外科下面的 科室

    public List<DiseaseDeparDetailName> getDiseaseDeparDetailName() {
        return DiseaseDeparDetailName;
    }

    public void setDiseaseDeparDetailName(List<DiseaseDeparDetailName> diseaseDeparDetailName) {
        DiseaseDeparDetailName = diseaseDeparDetailName;
    }

    public String getDiseaseDeparName() {
        return DiseaseDeparName;
    }

    public void setDiseaseDeparName(String diseaseDeparName) {
        DiseaseDeparName = diseaseDeparName;
    }
}
