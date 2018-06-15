package wizrole.hoservice.util;

/**
 * Created by a on 2017/3/16.
 *事件总线
 * 发送数据集合
 */

public class EventUtil {

    private String msg;
    public String free;
    public String yue;

    public EventUtil(){
        super();
    }
    public EventUtil(String msg) {
        this.msg = msg;
    }

    public EventUtil(String free, String yue){
        this.free=free;
        this.yue=yue;
    }

    public String getFree() {
        return free;
    }

    public String getYue() {
        return yue;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg(){
        return this.msg;
    }
}
