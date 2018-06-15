package wizrole.hoservice.beam;

import java.io.Serializable;

/**
 * Created by liushengping on 2017/11/8/008.
 * 何人执笔？
 * 自定义消息
 */

public class CustMessage  implements Serializable{

    public String CustMess_imgurl;
    public String CustMess_title;
    public String CustMess_content;

    public String getCustMess_imgurl() {
        return CustMess_imgurl;
    }

    public String getCustMess_title() {
        return CustMess_title;
    }

    public String getCustMess_content() {
        return CustMess_content;
    }

    public void setCustMess_imgurl(String custMess_imgurl) {
        CustMess_imgurl = custMess_imgurl;
    }

    public void setCustMess_title(String custMess_title) {
        CustMess_title = custMess_title;
    }

    public void setCustMess_content(String custMess_content) {
        CustMess_content = custMess_content;
    }
}
