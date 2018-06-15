package wizrole.hoservice.beam;

import java.util.List;

/**
 * Created by a on 2017/3/9.
 * 费用清单信息实体类
 */

public class PayInfor {

    public String ResultCode;   //处理结果编码
    public String ErrorMsg;     //处理结果
    public String PatientID;   //交易流水号
    public String PatName;      //病人姓名
    public String PatAge;       //病人年龄
    public String PatSex;       //病人年龄
    public String AdmReason;
    public String Department;       //科室
    public String Ward;       //病区
    public String BedNO;       //床号
    public String PatFee;       //花费
    public String Deposit;       //预存余额
    public String VisitStatus;
    public String PrintFlag;
    public List<TarItemDetail> TarItemDetail;

    public List<TarItemDetail> getTarItemDetail() {
        return TarItemDetail;
    }

    public void setTarItemDetail(List<TarItemDetail> tarItemDetails) {
        TarItemDetail = tarItemDetails;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public String getPatientID() {
        return PatientID;
    }

    public String getPatName() {
        return PatName;
    }

    public String getPatAge() {
        return PatAge;
    }

    public String getPatSex() {
        return PatSex;
    }

    public String getAdmReason() {
        return AdmReason;
    }

    public String getDepartment() {
        return Department;
    }

    public String getWard() {
        return Ward;
    }

    public String getBedNO() {
        return BedNO;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public void setPatName(String patName) {
        PatName = patName;
    }

    public void setPatAge(String patAge) {
        PatAge = patAge;
    }

    public void setPatSex(String patSex) {
        PatSex = patSex;
    }

    public void setAdmReason(String admReason) {
        AdmReason = admReason;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public void setWard(String ward) {
        Ward = ward;
    }

    public void setBedNO(String bedNO) {
        BedNO = bedNO;
    }

    public void setPatFee(String patFee) {
        PatFee = patFee;
    }

    public void setDeposit(String deposit) {
        Deposit = deposit;
    }

    public void setVisitStatus(String visitStatus) {
        VisitStatus = visitStatus;
    }

    public void setPrintFlag(String printFlag) {
        PrintFlag = printFlag;
    }



    public String getPatFee() {
        return PatFee;
    }

    public String getDeposit() {
        return Deposit;
    }

    public String getVisitStatus() {
        return VisitStatus;
    }

    public String getPrintFlag() {
        return PrintFlag;
    }

}
