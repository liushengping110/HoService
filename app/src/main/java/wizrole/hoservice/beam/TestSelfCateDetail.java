package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by liushengping on 2017/11/8/008.
 * 何人执笔？
 *
 */

public class TestSelfCateDetail implements Serializable{
    public String TestCateDetailName;//症状名称
    public String TestCateDetailIntrduce;//概述
    public String TestCateDetailCause;//症状起因
    public String TestCateDetailIdentify;//鉴别诊断
    public String TestCateDetailProphylaxis;//如何预防
    public String TestCateDetailDiet;//饮食宜忌
    public String TestCateDetailTreatment;//治疗方法


    public String getTestCateDetailName() {
        return TestCateDetailName;
    }

    public String getTestCateDetailIntrduce() {
        return TestCateDetailIntrduce;
    }

    public String getTestCateDetailCause() {
        return TestCateDetailCause;
    }

    public String getTestCateDetailIdentify() {
        return TestCateDetailIdentify;
    }

    public String getTestCateDetailProphylaxis() {
        return TestCateDetailProphylaxis;
    }

    public String getTestCateDetailDiet() {
        return TestCateDetailDiet;
    }

    public String getTestCateDetailTreatment() {
        return TestCateDetailTreatment;
    }
}
