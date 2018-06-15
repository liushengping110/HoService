package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by a on 2017/9/6.
 * 最新公告
 */

public class AnnounceMsg implements Serializable{

    public String Announce_img;//最新公告图片
    public String Announce_title;//最新公告标题
    public String Announce_msg;//最新公告内容

    public String getAnnounce_img() {
        return Announce_img;
    }

    public String getAnnounce_title() {
        return Announce_title;
    }

    public String getAnnounce_msg() {
        return Announce_msg;
    }

    public void setAnnounce_img(String announce_img) {
        Announce_img = announce_img;
    }

    public void setAnnounce_title(String announce_title) {
        Announce_title = announce_title;
    }

    public void setAnnounce_msg(String announce_msg) {
        Announce_msg = announce_msg;
    }
}
