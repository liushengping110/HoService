package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by a on 2017/8/16.
 * 推送消息信息类
 * 根据type 确定数据
 */

public class HosMsg implements Serializable{

    public String type;//1---检查医嘱   2---检验   3---自定义推送消息

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
