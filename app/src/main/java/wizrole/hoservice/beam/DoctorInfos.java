package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by a on 2017/5/12.
 * 专家介绍
 */

public class DoctorInfos implements Serializable{

    public String DocName;//医生姓名  张三
    public String Position;//职位   心内科主任
    public String Department;//科室   心内科
    public String Title;//职称    主任医师
    public String VisitTime;//出诊时间   周二
    public String Intrduce;//介绍
    public String RecordSchool;//学历   本科
    public String DocHeadImage;//头像   image

    public void setDocName(String docName) {
        DocName = docName;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setVisitTime(String visitTime) {
        VisitTime = visitTime;
    }

    public void setIntrduce(String intrduce) {
        Intrduce = intrduce;
    }

    public void setRecordSchool(String recordSchool) {
        RecordSchool = recordSchool;
    }

    public void setDocHeadImage(String docHeadImage) {
        DocHeadImage = docHeadImage;
    }

    public String getDocName() {

        return DocName;
    }

    public String getPosition() {
        return Position;
    }

    public String getDepartment() {
        return Department;
    }

    public String getTitle() {
        return Title;
    }

    public String getVisitTime() {
        return VisitTime;
    }

    public String getIntrduce() {
        return Intrduce;
    }

    public String getRecordSchool() {
        return RecordSchool;
    }

    public String getDocHeadImage() {
        return DocHeadImage;
    }
}
