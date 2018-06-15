package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by a on 2017/3/13.
 */

public class OrderList implements Serializable{
    public String ordName;  //项目名称
    public String ordRecDep;    //执行科室
    public String ordDate;  //执行日期
    public String ordTime;  //执行时间
    public String ordDoc;   //执行医生
    public boolean status_today;
    public boolean status_tomorrow;

    public boolean isStatus_tomorrow() {
        return status_tomorrow;
    }

    public void setStatus_tomorrow(boolean status_tomorrow) {
        this.status_tomorrow = status_tomorrow;
    }

    public boolean isStatus_today() {
        return status_today;
    }

    public void setStatus_today(boolean status_today) {
        this.status_today = status_today;
    }

    public OrderList(){
        super();
    }


    public void setOrdName(String ordName) {
        this.ordName = ordName;
    }

    public void setOrdRecDep(String ordRecDep) {
        this.ordRecDep = ordRecDep;
    }

    public void setOrdDate(String ordDate) {
        this.ordDate = ordDate;
    }

    public void setOrdTime(String ordTime) {
        this.ordTime = ordTime;
    }

    public void setOrdDoc(String ordDoc) {
        this.ordDoc = ordDoc;
    }

    public String getOrdName() {

        return ordName;
    }

    public String getOrdRecDep() {
        return ordRecDep;
    }

    public String getOrdDate() {
        return ordDate;
    }

    public String getOrdTime() {
        return ordTime;
    }

    public String getOrdDoc() {
        return ordDoc;
    }
}
