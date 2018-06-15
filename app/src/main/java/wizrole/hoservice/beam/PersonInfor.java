package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * 个人基本信息实体类
 * Created by a on 2016/12/8.
 */

public class PersonInfor implements Serializable {
    public String resultCode;   //处理结果编码
    public String resultContent;    //处理结果
    private String name;    //姓名
    private String patNo;    //病人id
    private String sex; //性别
    private String telNo; //手机号码
    public String  DOB;   //出生日期
    public String zyno;         //住院号
    public String diagnose;     //诊断
    public String ctLoc;        //住院科室
    public String docMedUnit;   //医疗单元
    public String ward ;        //病区
    public String feeSelf;      //余额
    public String  admDate;     //入院日期
    public String admNo;        //就诊号
    public String address;
    public String IDTypeDesc;

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultContent(String resultContent) {
        this.resultContent = resultContent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatNo(String patNo) {
        this.patNo = patNo;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setZyno(String zyno) {
        this.zyno = zyno;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public void setCtLoc(String ctLoc) {
        this.ctLoc = ctLoc;
    }

    public void setDocMedUnit(String docMedUnit) {
        this.docMedUnit = docMedUnit;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public void setFeeSelf(String feeSelf) {
        this.feeSelf = feeSelf;
    }

    public void setAdmDate(String admDate) {
        this.admDate = admDate;
    }

    public void setAdmNo(String admNo) {
        this.admNo = admNo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIDTypeDesc(String IDTypeDesc) {
        this.IDTypeDesc = IDTypeDesc;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getResultContent() {
        return resultContent;
    }

    public String getName() {
        return name;
    }

    public String getPatNo() {
        return patNo;
    }

    public String getSex() {
        return sex;
    }

    public String getTelNo() {
        return telNo;
    }

    public String getDOB() {
        return DOB;
    }

    public String getZyno() {
        return zyno;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public String getCtLoc() {
        return ctLoc;
    }

    public String getDocMedUnit() {
        return docMedUnit;
    }

    public String getWard() {
        return ward;
    }

    public String getFeeSelf() {
        return feeSelf;
    }

    public String getAdmDate() {
        return admDate;
    }

    public String getAdmNo() {
        return admNo;
    }

    public String getAddress() {
        return address;
    }

    public String getIDTypeDesc() {
        return IDTypeDesc;
    }
}