package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by ${liushengping} on 2017/10/17.
 * 何人执笔？
 * 手术详情
 */

public class SurgeryDetailInfor implements Serializable{

    public String SurgeryName;//手术名称
    public String SurgeryIndications;//适应症
    public String SurgeryContraindications;//禁忌症
    public String SurgeryEffect;//手术效果
    public String SurgeryConsiderations;//手术注意事项
    public String SurgeryAfterHandling;//手术后处理
    public String SurgeryAnesthesia;//手术麻醉方法
    public String SurgeryAnesthesiaTaboo;//手术麻醉禁忌

    public String getSurgeryName() {
        return SurgeryName;
    }

    public String getSurgeryIndications() {
        return SurgeryIndications;
    }

    public String getSurgeryContraindications() {
        return SurgeryContraindications;
    }

    public String getSurgeryEffect() {
        return SurgeryEffect;
    }

    public String getSurgeryConsiderations() {
        return SurgeryConsiderations;
    }

    public String getSurgeryAfterHandling() {
        return SurgeryAfterHandling;
    }

    public String getSurgeryAnesthesia() {
        return SurgeryAnesthesia;
    }

    public String getSurgeryAnesthesiaTaboo() {
        return SurgeryAnesthesiaTaboo;
    }
}
