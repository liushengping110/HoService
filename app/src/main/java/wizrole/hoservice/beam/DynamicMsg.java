package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by a on 2017/9/4.
 * 医院动态
 */

public class DynamicMsg implements Serializable {
    public String Dynamic_img;//医院动态图片
    public String Dynamic_title;//医院动态标题
    public String Dynamic_msg;//医院动态内容

    public void setDynamic_img(String dynamic_img) {
        Dynamic_img = dynamic_img;
    }

    public void setDynamic_title(String dynamic_title) {
        Dynamic_title = dynamic_title;
    }

    public void setDynamic_msg(String dynamic_msg) {
        Dynamic_msg = dynamic_msg;
    }

    public String getDynamic_img() {

        return Dynamic_img;
    }

    public String getDynamic_title() {
        return Dynamic_title;
    }

    public String getDynamic_msg() {
        return Dynamic_msg;
    }
}
