package wizrole.hoservice.beam;

/**
 * Created by a on 2017/4/6.
 * 订单时间选择
 */

public class OrderTimeSel {

    public String time;
    public int isSelect;    //是否选中   0  ---未选中    1  ----选中


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSelect(int select) {
        isSelect = select;
    }

    public int isSelect() {
        return isSelect;
    }
}
